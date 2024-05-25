package ru.below.effective_modile_test.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Component
public class AccountTransfer {
    private String fromName;
    private String name;
    private BigDecimal amount;
}
