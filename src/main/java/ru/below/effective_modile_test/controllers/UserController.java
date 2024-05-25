package ru.below.effective_modile_test.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.below.effective_modile_test.dto.AccountTransfer;
import ru.below.effective_modile_test.dto.EmailDTO;
import ru.below.effective_modile_test.dto.PhoneDTO;
import ru.below.effective_modile_test.dto.SearchUser;
import ru.below.effective_modile_test.models.User;
import ru.below.effective_modile_test.services.AccountService;
import ru.below.effective_modile_test.services.UserService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name="UserController", description="CRUD, Пагинация, Фильтрация, Сортировка данных")
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final AccountService accountService;
    @PostMapping("/addPhone/{phone}")
    @Operation(description = "Позволяет добавлять телефон")
    public ResponseEntity<String> addPhone(@Parameter(description = "Новый номер телефона") @PathVariable String phone) throws AccessException {
        userService.addPhone(phone);
        return ResponseEntity.ok("Added phone successfully");
    }
    @PutMapping("/updatePhone")
    @Operation(description = "Позволяет изменять телефон")
    public ResponseEntity<String> updatePhone(@Parameter(description = "Новый номер телефона")@RequestBody PhoneDTO phoneDTO) throws AccessException {

        return ResponseEntity.ok(userService.updatePhone(phoneDTO));
    }
    @PutMapping("/updateEmail")
    @Operation(description = "Позволяет изменять email")
    public ResponseEntity<String> updateEmail(@Parameter(description = "Новый email")@RequestBody EmailDTO emailDTO) throws AccessException {

        return ResponseEntity.ok(userService.updateEmail(emailDTO));
    }
    @PostMapping("/addEmail/{email}")
    @Operation(description = "Позволяет добавлять email")
    public ResponseEntity<String> addEmail(@Parameter(description = "Новый email")@PathVariable String email) throws AccessException {
        userService.addEmail(email);
        return ResponseEntity.ok("Added email successfully");
    }
    @DeleteMapping("/deleteEmail/{email}")
    @Operation(description = "Позволяет удалять email")
    public ResponseEntity<String> deleteEmail(@Parameter(description = "email для удаления")@PathVariable String email) throws AccessException {
        return ResponseEntity.ok(userService.deleteEmail(email));
    }
    @DeleteMapping("/deletePhone/{phone}")
    @Operation(description = "Позволяет удалять телефон")
    public ResponseEntity<String> deletePhone(@Parameter(description = "телефон для удаления")@PathVariable String phone) throws AccessException {
        return ResponseEntity.ok(userService.deletePhone(phone));
    }
    @PutMapping("/transferAmount")
    @Operation(description = "Позволяет отправить деньги другому пользователю")
    public ResponseEntity<String>transferAmount(@Parameter(description = "информация по переводу денег")@RequestBody AccountTransfer accountTransfer) throws AccessException {
        return  ResponseEntity.ok(accountService.transfer(accountTransfer));
    }
    @GetMapping("/search")
    @Operation(description = "Позволяет фильтровать данные")
    public ResponseEntity<List<User>>filterSearch(@Parameter(description = "информация по фильтрации")@RequestBody SearchUser searchUser) throws AccessException {
        return ResponseEntity.ok(userService.searchUsers(searchUser));
    }
   /* @GetMapping("/search/{field}")
    public ResponseEntity<List<User>>filterSearchField(@RequestBody SearchUser searchUser,@PathVariable(required = false) String field) throws AccessException {
        return ResponseEntity.ok(userService.findUserWithSorting(searchUser,field));
    }*/
    @GetMapping("/search/{field}")
    @Operation(description = "Позволяет фильтровать данные и выставлять пагинацию")
    public ResponseEntity<List<User>>filterSearchFieldAndPage(@Parameter(description = "информация по фильтрации")@RequestBody SearchUser searchUser,
                                                              @Parameter(description = "поле для фильтрации")@PathVariable(required = false) String field,
                                                              @Parameter(description = "колл-во страниц")@RequestParam(required = false,value = "offset",defaultValue = "0")Integer offset,
                                                              @Parameter(description = "колл-во эл-ов на странице")@RequestParam(required = false,value = "pageSize",defaultValue = "2")Integer pageSize
                                                              ) throws AccessException {
        return ResponseEntity.ok(userService.findUserWithSorting(searchUser,field,offset,pageSize));
    }

}
