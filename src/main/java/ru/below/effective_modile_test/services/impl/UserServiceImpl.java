package ru.below.effective_modile_test.services.impl;


import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.atn.SemanticContext;
import org.hibernate.sql.ast.tree.predicate.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.expression.AccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.below.effective_modile_test.dto.EmailDTO;
import ru.below.effective_modile_test.dto.PhoneDTO;
import ru.below.effective_modile_test.dto.SearchUser;
import ru.below.effective_modile_test.models.Email;
import ru.below.effective_modile_test.models.Phone;
import ru.below.effective_modile_test.models.User;
import ru.below.effective_modile_test.repositories.EmailRepository;
import ru.below.effective_modile_test.repositories.PhoneRepository;
import ru.below.effective_modile_test.repositories.UserRepository;
import ru.below.effective_modile_test.services.UserService;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;
    private final EmailRepository emailRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUsersByName(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

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
    public String updateEmail(EmailDTO emailDTO) throws AccessException {
        User user = userRepository.findUsersByName(SecurityContextHolder.getContext().getAuthentication().getName()).orElseThrow(() -> new AccessException("user not found"));
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
        return "One email left";
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
        return "One phone left";
    }

    //Применение всех фильтров
    @Override
    public List<User> searchUsers(SearchUser searchUser) throws AccessException {
        List<User> birthdays = List.of(), phone = List.of(), email = List.of(), name = List.of();
        List<User> allUser = userRepository.findAll();
        if (searchUser.getDateOfBirth() != null) {
            birthdays = userRepository.indAllSuitableForInterestBirthDay(searchUser.getDateOfBirth());
            allUser = allUser.stream().filter(birthdays::contains).collect(toList());
        }

        if (searchUser.getPhone() != null) {
            phone = userListPhone(allUser, searchUser);
            allUser = allUser.stream().filter(phone::contains).collect(toList());
        }
        if (searchUser.getEmail() != null) {
            email = userListEmail(allUser, searchUser);
            allUser = allUser.stream().filter(email::contains).collect(toList());
        }
        if (searchUser.getName() != null) {
            name = userRepository.findByUsernameStartsWith(searchUser.getName());
            allUser = allUser.stream().filter(name::contains).collect(toList());
        }

        List<User> result = birthdays.stream().filter(phone::contains).filter(email::contains).filter(name::contains).collect(toList());
        return allUser;
    }

    //Находим User по номеру
    public List<User> userListPhone(List<User> list, SearchUser searchUser) {
        List<User> users = new ArrayList<>();
        for (var e : list) {
            for (var ee : e.getPhones()) {
                if (ee.getPhone().equals(searchUser.getPhone())) {
                    users.add(e);
                    return users;
                }
            }
        }
        return users;
    }

    //Находим User по email
    public List<User> userListEmail(List<User> list, SearchUser searchUser) {
        List<User> users = new ArrayList<>();
        for (var e : list) {
            for (var ee : e.getPhones()) {
                if (ee.getPhone().equals(searchUser.getPhone())) {
                    users.add(e);
                    return users;
                }
            }
        }
        return users;
    }

    @Override
    public List<User> findUserWithSorting(SearchUser searchUer, String field) throws AccessException {
        if (field.equals("name"))
            return searchUsers(searchUer).stream().sorted(Comparator.comparing(User::getName)).collect(toList());
        else if (field.equals("birthday"))
            return searchUsers(searchUer).stream().sorted(Comparator.comparing(User::getBirthday)).collect(toList());
        return searchUsers(searchUer);
    }

    @Override
    public List<User> findUserWithSorting(SearchUser searchUer, String field, int offset, int pageSize) throws AccessException {
        if (field.equals("name"))
            return searchUsers(searchUer, offset, pageSize).stream().sorted(Comparator.comparing(User::getName)).collect(toList());
        else if (field.equals("birthday"))
            return searchUsers(searchUer,offset,pageSize).stream().sorted(Comparator.comparing(User::getBirthday)).collect(toList());
        return searchUsers(searchUer);
    }

    @Override
    public List<User> searchUsers(SearchUser searchUser, int offset, int pageSize) throws AccessException {
        List<User> birthdays = List.of(), phone = List.of(), email = List.of(), name = List.of();
        Page<User> allUserPage = userRepository.findAll(PageRequest.of(offset, pageSize));
        List<User> allUser = allUserPage.getContent();
        if (searchUser.getDateOfBirth() != null) {
            birthdays = userRepository.indAllSuitableForInterestBirthDay(searchUser.getDateOfBirth());
            allUser = allUser.stream().filter(birthdays::contains).collect(toList());
        }

        if (searchUser.getPhone() != null) {
            phone = userListPhone(allUser, searchUser);
            allUser = allUser.stream().filter(phone::contains).collect(toList());
        }
        if (searchUser.getEmail() != null) {
            email = userListEmail(allUser, searchUser);
            allUser = allUser.stream().filter(email::contains).collect(toList());
        }
        if (searchUser.getName() != null) {
            name = userRepository.findByUsernameStartsWith(searchUser.getName());
            allUser = allUser.stream().filter(name::contains).collect(toList());
        }

        List<User> result = birthdays.stream().filter(phone::contains).filter(email::contains).filter(name::contains).collect(toList());
        return allUser;
    }


}
