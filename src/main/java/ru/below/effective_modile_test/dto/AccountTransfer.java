package ru.below.effective_modile_test.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountTransfer {
    private String name;
    private BigDecimal amount;
}
