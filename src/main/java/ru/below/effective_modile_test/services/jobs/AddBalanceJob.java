package ru.below.effective_modile_test.services.jobs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.below.effective_modile_test.models.Account;
import ru.below.effective_modile_test.repositories.AccountRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
@EnableScheduling
public class AddBalanceJob {

    private final BigDecimal MAX_LIMIT = BigDecimal.valueOf(2.07);
    private final BigDecimal PERCENT_BONUS = BigDecimal.valueOf(1.05);

    private final AccountRepository accountRepository;

    @Transactional
    @Scheduled(fixedDelayString = "60", timeUnit = TimeUnit.SECONDS)
    public void jobIncreasing() {

        List<Account> accounts = accountRepository.findAll();

        for (Account account : accounts) {
            BigDecimal maxBalance = account.getInitialBalance().multiply(MAX_LIMIT);
            BigDecimal balance = account.getBalance().multiply(PERCENT_BONUS);
            if (balance.compareTo(maxBalance) <= 0) {
                account.setBalance(balance);
                log.info("Balance update, new balance {}", account.getBalance());
            }
        }
        accountRepository.saveAll(accounts);
    }
}