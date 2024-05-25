package ru.below.effective_modile_test.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Сущность для нового Phone")
public class PhoneDTO {
    private String phone;
    private String newPhone;
}
