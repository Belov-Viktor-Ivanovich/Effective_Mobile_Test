package ru.below.effective_modile_test.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
@Data
@Schema(description = "Сущность фильтрации")
public class SearchUser {
    String name;
    LocalDate dateOfBirth;
    String phone;
    String email;

}
