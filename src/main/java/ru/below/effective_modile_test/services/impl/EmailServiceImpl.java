package ru.below.effective_modile_test.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.AccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.below.effective_modile_test.dto.EmailDTO;
import ru.below.effective_modile_test.models.Email;
import ru.below.effective_modile_test.models.User;
import ru.below.effective_modile_test.repositories.EmailRepository;
import ru.below.effective_modile_test.repositories.UserRepository;
import ru.below.effective_modile_test.services.EmailService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmailServiceImpl implements EmailService {

    private final UserRepository userRepository;
    private final EmailRepository emailRepository;

    @Override
    @Transactional
    public String updateEmail(EmailDTO emailDTO) throws AccessException {
        //User user = userRepository.findUsersByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new AccessException("user not found"));
        Email email = emailRepository.findEmailsByEmail(emailDTO.getEmail()).orElseThrow(() -> new AccessException("email not found"));
        email.setEmail(emailDTO.getNewEmail());
        emailRepository.save(email);
        return "update email";
    }

    @Override
    @Transactional
    public String addEmail(String email) throws AccessException {
        User user = userRepository.findUsersByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new AccessException("user not found"));
        user.getEmails().add(new Email().builder().email(email).build());
        userRepository.save(user);
        return email;
    }

    @Override
    @Transactional
    public String deleteEmail(String email) throws AccessException {
        User user = userRepository.findUsersByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new AccessException("user not found"));
        Email owner = emailRepository.findEmailsByEmail(email).orElseThrow(() -> new AccessException("email not found"));
        if (user.getEmails().size() > 1) {
            user.getEmails().remove(owner);
            userRepository.save(user);
            emailRepository.delete(owner);
            return "delete email " + owner.getEmail();
        }
        log.warn("User {} One email left {} ,failed to delete", user.getName(), email);
        return "One email left";
    }
}
