package ru.below.effective_modile_test.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Schema(description = "Сущность для транзакции")
@Data
@Component
public class AccountTransfer {
    private String fromName;
    private String name;
    private BigDecimal amount;
}
