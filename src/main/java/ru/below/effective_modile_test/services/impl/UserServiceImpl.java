package ru.below.effective_modile_test.services.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.expression.AccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.below.effective_modile_test.dto.SearchUser;
import ru.below.effective_modile_test.models.User;
import ru.below.effective_modile_test.repositories.UserRepository;
import ru.below.effective_modile_test.services.UserService;
import java.util.*;
import static java.util.stream.Collectors.toList;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUsersByName(username).orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }

    //Применение всех фильтров
    @Override
    public List<User> searchUsers(SearchUser searchUser) {
        List<User> birthdays, phone, email, name;
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
    public List<User> findUserWithSorting(SearchUser searchUer, String field) {
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
    public List<User> searchUsers(SearchUser searchUser, int offset, int pageSize) {
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
