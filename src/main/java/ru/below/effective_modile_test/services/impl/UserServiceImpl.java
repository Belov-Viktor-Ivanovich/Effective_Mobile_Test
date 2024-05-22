package ru.below.effective_modile_test.services.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.below.effective_modile_test.dto.EmailDTO;
import ru.below.effective_modile_test.dto.PhoneDTO;
import ru.below.effective_modile_test.models.Email;
import ru.below.effective_modile_test.models.Phone;
import ru.below.effective_modile_test.models.User;
import ru.below.effective_modile_test.repositories.EmailRepository;
import ru.below.effective_modile_test.repositories.PhoneRepository;
import ru.below.effective_modile_test.repositories.UserRepository;
import ru.below.effective_modile_test.services.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final EmailRepository emailRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUsersByName(username).orElseThrow(()->new UsernameNotFoundException("user not found"));
    }

    @Override
    @Transactional
    public String addPhone(String phone) throws AccessException {
        User user = userRepository.findUsersByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new AccessException("user not found"));

        user.getPhones().add(new Phone().builder().phone(phone).build());
        userRepository.save(user);
        return phone;
    }

    @Override
    @Transactional
    public String updatePhone(PhoneDTO phoneDTO) throws AccessException {
        User user = userRepository.findUsersByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new AccessException("user not found"));
        Phone phone = phoneRepository.findPhonesByPhone(phoneDTO.getPhone()).orElseThrow(()->new AccessException("phone not found"));
        phone.setPhone(phoneDTO.getNewPhone());
        phoneRepository.save(phone);
        return "update phone number";
    }

    @Override
    @Transactional
    public String updateEmail(EmailDTO emailDTO) throws AccessException {
        User user = userRepository.findUsersByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new AccessException("user not found"));
        Email email = emailRepository.findEmailsByEmail(emailDTO.getEmail()).orElseThrow(()->new AccessException("email not found"));
        email.setEmail(emailDTO.getNewEmail());
        emailRepository.save(email);
        return "update email";
    }
    @Override
    @Transactional
    public String addEmail(String email) throws AccessException {
        User user = userRepository.findUsersByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new AccessException("user not found"));
        user.getEmails().add(new Email().builder().email(email).build());
        userRepository.save(user);
        return email;
    }

    @Override
    @Transactional
    public String deleteEmail(String email) throws AccessException {
        User user = userRepository.findUsersByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new AccessException("user not found"));
        Email owner = emailRepository.findEmailsByEmail(email).orElseThrow(()->new AccessException("email not found"));
        if (user.getEmails().size()>1) {
            user.getEmails().remove(owner);
            userRepository.save(user);
            emailRepository.delete(owner);
            return "delete email "+owner.getEmail();
        }
        return "One email left";
    }

    @Override
    @Transactional
    public String deletePhone(String phone) throws AccessException {
        User user = userRepository.findUsersByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(()->new AccessException("user not found"));
        Phone owner = phoneRepository.findPhonesByPhone(phone).orElseThrow(()->new AccessException("phone not found"));
        if (user.getPhones().size()>1) {
            user.getPhones().remove(owner);
            userRepository.save(user);
            phoneRepository.delete(owner);
            return "delete phone "+owner.getPhone();
        }
        return "One phone left";
    }
}
