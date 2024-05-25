package ru.below.effective_modile_test.services;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.AccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.below.effective_modile_test.dto.AccountTransfer;
import ru.below.effective_modile_test.models.Account;
import ru.below.effective_modile_test.models.User;
import ru.below.effective_modile_test.repositories.AccountRepository;
import ru.below.effective_modile_test.repositories.UserRepository;
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    @Transactional
    public String transfer(AccountTransfer request) throws AccessException {
        User user ;
        if(request.getFromName()==null) {
            user = userRepository.findUsersByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new AccessException("user not found"));
        }
        else {
            user = userRepository.findUsersByName(request.getFromName()).orElseThrow(() -> new AccessException("FromUser Transfer not found"));
        }
        User userTo = userRepository.findUsersByName(request.getName()).orElseThrow(() -> new AccessException("ToUser Transfer not found"));
        Account from = user.getAccount();
        Account to = userTo.getAccount();
        from.subtractFromBalance(request.getAmount());
        to.addBalance(request.getAmount());
        accountRepository.save(from);
        accountRepository.save(to);
        log.warn("the {} transferred {} to the {}",user.getName(),request.getAmount(),userTo.getName());
        return "success transfer";
    }
}
