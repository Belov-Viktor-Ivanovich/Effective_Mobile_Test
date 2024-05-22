package ru.below.effective_modile_test.services;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.below.effective_modile_test.dto.AccountTransfer;
import ru.below.effective_modile_test.models.Account;
import ru.below.effective_modile_test.models.User;
import ru.below.effective_modile_test.repositories.AccountRepository;
import ru.below.effective_modile_test.repositories.UserRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    @Transactional
    public String transfer(AccountTransfer request) throws AccessException {
        User user = userRepository.findUsersByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new AccessException("user not found"));
        User userTo = userRepository.findUsersByName(request.getName()).orElseThrow(() -> new AccessException("usetTransfer not found"));
        Account from = user.getAccount();
        Account to = userTo.getAccount();
        from.subtractFromBalance(request.getAmount());
        to.addBalance(request.getAmount());
        accountRepository.save(from);
        accountRepository.save(to);
        return "success transfer";
    }
}
