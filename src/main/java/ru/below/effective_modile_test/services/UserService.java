package ru.below.effective_modile_test.services;

import org.springframework.expression.AccessException;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.below.effective_modile_test.dto.SearchUser;
import ru.below.effective_modile_test.models.User;

import java.util.List;


public interface UserService extends UserDetailsService {
    List<User> searchUsers(SearchUser searchUser) throws AccessException;

    //List<User> findUserWithSorting(SearchUser searchUer, String field) throws AccessException;

    List<User> searchUsers(SearchUser searchUser, int offset, int pageSize) throws AccessException;

    List<User> findUserWithSorting(SearchUser searchUer, String field, int offset, int pageSize) throws AccessException;
}
