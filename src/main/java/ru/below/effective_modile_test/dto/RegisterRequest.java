package ru.below.effective_modile_test.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.below.effective_modile_test.models.Account;
import ru.below.effective_modile_test.models.Email;
import ru.below.effective_modile_test.models.Phone;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Сущность регистрации")
public class RegisterRequest {
    private String name;
    private String password;
    private BigDecimal accountBalance;
    private String emailsEmail;
    private String phonesPhone;
    private LocalDate birthDay;
}
