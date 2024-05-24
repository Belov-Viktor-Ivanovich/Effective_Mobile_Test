package ru.below.effective_modile_test.controllers;

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
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;
    private final AccountService accountService;
    @PostMapping("/addPhone/{phone}")
    public ResponseEntity<String> addPhone(@PathVariable String phone) throws AccessException {
        userService.addPhone(phone);
        return ResponseEntity.ok("Added phone successfully");
    }
    @PutMapping("/updatePhone")
    public ResponseEntity<String> updatePhone(@RequestBody PhoneDTO phoneDTO) throws AccessException {

        return ResponseEntity.ok(userService.updatePhone(phoneDTO));
    }
    @PutMapping("/updateEmail")
    public ResponseEntity<String> updateEmail(@RequestBody EmailDTO emailDTO) throws AccessException {

        return ResponseEntity.ok(userService.updateEmail(emailDTO));
    }
    @PostMapping("/addEmail/{email}")
    public ResponseEntity<String> addEmail(@PathVariable String email) throws AccessException {
        userService.addEmail(email);
        return ResponseEntity.ok("Added email successfully");
    }
    @DeleteMapping("/deleteEmail/{email}")
    public ResponseEntity<String> deleteEmail(@PathVariable String email) throws AccessException {
        return ResponseEntity.ok(userService.deleteEmail(email));
    }
    @DeleteMapping("/deletePhone/{phone}")
    public ResponseEntity<String> deletePhone(@PathVariable String phone) throws AccessException {
        return ResponseEntity.ok(userService.deletePhone(phone));
    }
    @PutMapping("/transferAmount")
    public ResponseEntity<String>transferAmount(@RequestBody AccountTransfer accountTransfer) throws AccessException {
        return  ResponseEntity.ok(accountService.transfer(accountTransfer));
    }
    @GetMapping("/search")
    public ResponseEntity<List<User>>filterSearch(@RequestBody SearchUser searchUser) throws AccessException {
        return ResponseEntity.ok(userService.searchUsers(searchUser));
    }
   /* @GetMapping("/search/{field}")
    public ResponseEntity<List<User>>filterSearchField(@RequestBody SearchUser searchUser,@PathVariable(required = false) String field) throws AccessException {
        return ResponseEntity.ok(userService.findUserWithSorting(searchUser,field));
    }*/
    @GetMapping("/search/{field}")
    public ResponseEntity<List<User>>filterSearchFieldAndPage(@RequestBody SearchUser searchUser,
                                                              @PathVariable(required = false) String field,
                                                              @RequestParam(required = false,value = "offset",defaultValue = "0")Integer offset,
                                                              @RequestParam(required = false,value = "pageSize",defaultValue = "2")Integer pageSize
                                                              ) throws AccessException {
        return ResponseEntity.ok(userService.findUserWithSorting(searchUser,field,offset,pageSize));
    }

}
