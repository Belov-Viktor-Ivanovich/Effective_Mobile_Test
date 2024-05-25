package ru.below.effective_modile_test.services;

import org.springframework.expression.AccessException;
import ru.below.effective_modile_test.dto.EmailDTO;

public interface EmailService {
    String updateEmail(EmailDTO emailDTO) throws AccessException;
    String addEmail(String email) throws AccessException;
    String deleteEmail(String email) throws AccessException;
}
