package ru.below.effective_modile_test.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.expression.AccessException;
import org.springframework.transaction.annotation.Transactional;
import ru.below.effective_modile_test.EffectiveModileTestApplicationTests;
import ru.below.effective_modile_test.dto.AccountTransfer;
import ru.below.effective_modile_test.services.impl.AccountServiceImpl;

import java.math.BigDecimal;

@AutoConfigureMockMvc
@Transactional(readOnly = true)
class AccountServiceImplTest extends EffectiveModileTestApplicationTests {
    @Autowired
    private AccountServiceImpl accountServiceImpl;
    @Autowired
    private AccountTransfer accountTransfer;



    @Test
    void accountHaveFunds() throws AccessException {
        accountTransfer.setName("user");
        accountTransfer.setFromName("user2");
        accountTransfer.setAmount(BigDecimal.valueOf(1));
        String test = accountServiceImpl.transfer(accountTransfer);
        Assertions.assertEquals("success transfer",test);
    }
    @Test
    void accountNotHaveFunds() {
        accountTransfer.setName("user");
        accountTransfer.setFromName("user2");
        accountTransfer.setAmount(BigDecimal.valueOf(Integer.MAX_VALUE));
        Assertions.assertThrows(RuntimeException.class,()-> accountServiceImpl.transfer(accountTransfer));
    }
    @Test
    void userNotFound() {
        accountTransfer.setName("bla-bla-bla-bla");
        accountTransfer.setFromName("user2");
        accountTransfer.setAmount(BigDecimal.valueOf(1));
        Assertions.assertThrows(AccessException.class,()-> accountServiceImpl.transfer(accountTransfer));
    }
}