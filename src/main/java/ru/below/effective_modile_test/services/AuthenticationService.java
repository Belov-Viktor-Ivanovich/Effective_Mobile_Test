package ru.below.effective_modile_test.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.below.effective_modile_test.dto.AuthenticationRequest;
import ru.below.effective_modile_test.dto.AuthenticationResponse;
import ru.below.effective_modile_test.dto.RegisterRequest;
import ru.below.effective_modile_test.models.Account;
import ru.below.effective_modile_test.models.Email;
import ru.below.effective_modile_test.models.Phone;
import ru.below.effective_modile_test.models.User;
import ru.below.effective_modile_test.repositories.AccountRepository;
import ru.below.effective_modile_test.repositories.EmailRepository;
import ru.below.effective_modile_test.repositories.PhoneRepository;
import ru.below.effective_modile_test.repositories.UserRepository;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final AccountRepository accountRepository;
    private final EmailRepository emailRepository;
    private final PhoneRepository phoneRepository;
    private final JwtService jwtService;
    private final ModelMapper modelMapper;
    private Converter<String, String> isbnRemover = (src) -> src.getSource().replaceAll("ISBN: ", "");
    public Account convertAccountDTOToAccount(RegisterRequest registerRequest) {

        return modelMapper.map(registerRequest, Account.class);
    }
    public Email convertEmailDTOToEmail(RegisterRequest registerRequest) {

        return modelMapper.map(registerRequest, Email.class);
    }
    public Phone convertPhoneDTOToPhone(RegisterRequest registerRequest) {

        return modelMapper.map(registerRequest, Phone.class);
    }
    private final AuthenticationManager authenticationManager;
    @Transactional
    public AuthenticationResponse register(RegisterRequest request){
        Account account = convertAccountDTOToAccount(request);
        account.setInitialBalance(account.getBalance());
        Phone phone = convertPhoneDTOToPhone(request);
        Email email = convertEmailDTOToEmail(request);
        var user = User.builder()
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .account(account)
                .emails(Set.of(email))
                .phones(Set.of(phone))
                .birthday(request.getBirthDay())
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        System.out.println(request.getName());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getName(),
                        request.getPassword()
                )
        );
        var user = repository.findUsersByName(request.getName())
                .orElseThrow(()->new UsernameNotFoundException("user not found"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
