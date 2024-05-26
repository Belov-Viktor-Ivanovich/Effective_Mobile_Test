package ru.below.effective_modile_test.services;

import org.springframework.expression.AccessException;
import ru.below.effective_modile_test.dto.PhoneDTO;

public interface PhoneService {
    String addPhone(String phone) throws AccessException;

    String updatePhone(PhoneDTO phoneDTO) throws AccessException;

    String deletePhone(String phone) throws AccessException;
}
