package ru.below.effective_modile_test.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class SearchUser {
    String name;
    LocalDate dateOfBirth;
    String phone;
    String email;

}
