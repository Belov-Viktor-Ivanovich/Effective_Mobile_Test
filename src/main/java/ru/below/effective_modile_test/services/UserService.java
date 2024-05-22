package ru.below.effective_modile_test.services;

import org.springframework.expression.AccessException;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.below.effective_modile_test.dto.EmailDTO;
import ru.below.effective_modile_test.dto.PhoneDTO;
import ru.below.effective_modile_test.models.Phone;
import ru.below.effective_modile_test.models.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    String addPhone(String phone) throws AccessException;
    String updatePhone(PhoneDTO phoneDTO) throws AccessException;
    String updateEmail(EmailDTO emailDTO) throws AccessException;
    String addEmail(String email) throws AccessException;
    String deleteEmail(String email) throws AccessException;
    String deletePhone(String phone) throws AccessException;
}
