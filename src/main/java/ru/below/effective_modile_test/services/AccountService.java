package ru.below.effective_modile_test.services;

import org.springframework.expression.AccessException;
import ru.below.effective_modile_test.dto.AccountTransfer;

public interface AccountService {
    String transfer(AccountTransfer request) throws AccessException;
}
