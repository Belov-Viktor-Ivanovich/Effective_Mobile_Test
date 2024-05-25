package ru.below.effective_modile_test.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.AccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.below.effective_modile_test.dto.PhoneDTO;
import ru.below.effective_modile_test.models.Phone;
import ru.below.effective_modile_test.models.User;
import ru.below.effective_modile_test.repositories.PhoneRepository;
import ru.below.effective_modile_test.repositories.UserRepository;
import ru.below.effective_modile_test.services.PhoneService;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PhoneServiceImpl implements PhoneService {
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;

    @Override
    @Transactional
    public String addPhone(String phone) throws AccessException {
        User user = userRepository.findUsersByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new AccessException("user not found"));

        user.getPhones().add(new Phone().builder().phone(phone).build());
        userRepository.save(user);
        return phone;
    }

    @Override
    @Transactional
    public String updatePhone(PhoneDTO phoneDTO) throws AccessException {
        User user = userRepository.findUsersByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new AccessException("user not found"));
        Phone phone = phoneRepository.findPhonesByPhone(phoneDTO.getPhone()).orElseThrow(() -> new AccessException("phone not found"));
        phone.setPhone(phoneDTO.getNewPhone());
        phoneRepository.save(phone);
        return "update phone number";
    }

    @Override
    @Transactional
    public String deletePhone(String phone) throws AccessException {
        User user = userRepository.findUsersByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new AccessException("user not found"));
        Phone owner = phoneRepository.findPhonesByPhone(phone).orElseThrow(() -> new AccessException("phone not found"));
        if (user.getPhones().size() > 1) {
            user.getPhones().remove(owner);
            userRepository.save(user);
            phoneRepository.delete(owner);
            return "delete phone " + owner.getPhone();
        }
        log.warn("User {} One phone left {} ,failed to delete", user.getName(), phone);
        return "One phone left";
    }
}
