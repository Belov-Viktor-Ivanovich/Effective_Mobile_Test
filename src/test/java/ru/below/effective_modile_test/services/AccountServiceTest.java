package ru.below.effective_modile_test.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.expression.AccessException;
import org.springframework.transaction.annotation.Transactional;
import ru.below.effective_modile_test.EffectiveModileTestApplicationTests;
import ru.below.effective_modile_test.dto.AccountTransfer;
import ru.below.effective_modile_test.models.Account;
import ru.below.effective_modile_test.models.User;
import ru.below.effective_modile_test.repositories.AccountRepository;
import ru.below.effective_modile_test.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@Transactional(readOnly = true)
class AccountServiceTest extends EffectiveModileTestApplicationTests {
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountTransfer accountTransfer;



    @Test
    void accountHaveFunds() throws AccessException {

        accountTransfer.setName("user");
        accountTransfer.setFromName("user2");
        accountTransfer.setAmount(BigDecimal.valueOf(100));
        String test = accountService.transfer(accountTransfer);
        Assertions.assertEquals("success transfer",test);
    }
    @Test
    void accountNotHaveFunds() throws AccessException {
        accountTransfer.setName("user");
        accountTransfer.setFromName("user2");
        accountTransfer.setAmount(BigDecimal.valueOf(1000));
        Assertions.assertThrows(RuntimeException.class,()->accountService.transfer(accountTransfer));
    }
}