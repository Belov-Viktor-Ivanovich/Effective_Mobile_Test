package ru.below.effective_modile_test.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Сущность для нового Email")
public class EmailDTO {
    private String email;
    private String newEmail;
}
