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

import java.util.Comparator;
import java.util.List;

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

    @Override
    public List<User> searchUsers(SearchUser searchUser) {
        List<User> temporary;
        List<User> allUser = userRepository.findAll();
        if (searchUser.getDateOfBirth() != null) {
            temporary = userRepository.indAllSuitableForInterestBirthDay(searchUser.getDateOfBirth());
            allUser = allUser.stream().filter(temporary::contains).collect(toList());
        }

        if (searchUser.getPhone() != null) {
            temporary = allUser.stream().filter(user -> user.getPhones().stream().anyMatch(phone -> phone.getPhone().equals(searchUser.getPhone()))).toList();
            allUser = allUser.stream().filter(temporary::contains).collect(toList());
        }
        if (searchUser.getEmail() != null) {
            temporary = allUser.stream().filter(user -> user.getEmails().stream().anyMatch(email -> email.getEmail().equals(searchUser.getEmail()))).toList();
            allUser = allUser.stream().filter(temporary::contains).collect(toList());
        }
        if (searchUser.getName() != null) {
            temporary = userRepository.findByUsernameStartsWith(searchUser.getName());
            allUser = allUser.stream().filter(temporary::contains).collect(toList());
        }
        return allUser;
    }

    @Override
    public List<User> findUserWithSorting(SearchUser searchUer, String field, int offset, int pageSize) throws AccessException {
        if (field.equals("name"))
            return searchUsers(searchUer, offset, pageSize).stream().sorted(Comparator.comparing(User::getName)).collect(toList());
        else if (field.equals("birthday"))
            return searchUsers(searchUer, offset, pageSize).stream().sorted(Comparator.comparing(User::getBirthday)).collect(toList());
        return searchUsers(searchUer);
    }

    @Override
    public List<User> searchUsers(SearchUser searchUser, int offset, int pageSize) {
        List<User> temporary;
        Page<User> allUserPage = userRepository.findAll(PageRequest.of(offset, pageSize));
        List<User> allUser = allUserPage.getContent();
        if (searchUser.getDateOfBirth() != null) {
            temporary = userRepository.indAllSuitableForInterestBirthDay(searchUser.getDateOfBirth());
            allUser = allUser.stream().filter(temporary::contains).collect(toList());
        }

        if (searchUser.getPhone() != null) {
            temporary = allUser.stream().filter(user -> user.getPhones().stream().anyMatch(phone -> phone.getPhone().equals(searchUser.getPhone()))).toList();
            allUser = allUser.stream().filter(temporary::contains).collect(toList());
        }
        if (searchUser.getEmail() != null) {
            temporary = allUser.stream().filter(user -> user.getEmails().stream().anyMatch(phone -> phone.getEmail().equals(searchUser.getEmail()))).toList();
            allUser = allUser.stream().filter(temporary::contains).collect(toList());
        }
        if (searchUser.getName() != null) {
            temporary = userRepository.findByUsernameStartsWith(searchUser.getName());
            allUser = allUser.stream().filter(temporary::contains).collect(toList());
        }
        return allUser;
    }
}
