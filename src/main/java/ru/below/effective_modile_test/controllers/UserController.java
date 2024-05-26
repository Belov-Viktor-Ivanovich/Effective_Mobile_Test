package ru.below.effective_modile_test.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.below.effective_modile_test.dto.SearchUser;
import ru.below.effective_modile_test.models.User;
import ru.below.effective_modile_test.services.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "UserController", description = "CRUD, Пагинация, Фильтрация, Сортировка данных")
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/search")
    @Operation(description = "Позволяет фильтровать данные")
    public ResponseEntity<List<User>> filterSearch(@Parameter(description = "информация по фильтрации") @RequestBody SearchUser searchUser) throws AccessException {
        return ResponseEntity.ok(userService.searchUsers(searchUser));
    }

    @GetMapping("/search/{field}")
    @Operation(description = "Позволяет фильтровать данные и выставлять пагинацию по имени и дате рождения")
    public ResponseEntity<List<User>> filterSearchFieldAndPage(@Parameter(description = "информация по фильтрации") @RequestBody SearchUser searchUser,
                                                               @Parameter(description = "поле для фильтрации") @PathVariable(required = false) String field,
                                                               @Parameter(description = "колл-во страниц") @RequestParam(required = false, value = "offset", defaultValue = "0") Integer offset,
                                                               @Parameter(description = "колл-во эл-ов на странице") @RequestParam(required = false, value = "pageSize", defaultValue = "10") Integer pageSize
    ) throws AccessException {
        return ResponseEntity.ok(userService.findUserWithSorting(searchUser, field, offset, pageSize));
    }

}
