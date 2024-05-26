package ru.below.effective_modile_test.services.impl;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
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
import ru.below.effective_modile_test.repositories.UserRepository;
import ru.below.effective_modile_test.services.AuthenticationService;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository repository;
    private final JwtServiceImpl jwtServiceImpl;
    private final ModelMapper modelMapper;
    private Converter<String, String> isbnRemover = (src) -> src.getSource().replaceAll("ISBN: ", "");

    @Override
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

    @Operation(
            summary = "Регистрация пользователя",
            description = "Позволяет зарегистрировать пользователя"
    )
    @Override
    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
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
        var jwtToken = jwtServiceImpl.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Operation(
            summary = "Аутентификация пользователя",
            description = "Позволяет авторизовать пользователя"
    )
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        System.out.println(request.getName());
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getName(),
                        request.getPassword()
                )
        );
        var user = repository.findUsersByName(request.getName())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        var jwtToken = jwtServiceImpl.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
