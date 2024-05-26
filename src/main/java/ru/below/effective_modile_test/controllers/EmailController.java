package ru.below.effective_modile_test.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.AccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.below.effective_modile_test.dto.EmailDTO;
import ru.below.effective_modile_test.services.EmailService;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "EmailController", description = "CRUD Email")
@RequestMapping("/api/v1/emails")
public class EmailController {
    private final EmailService emailService;

    @PutMapping("/email")
    @Operation(description = "Позволяет изменять email")
    public ResponseEntity<String> updateEmail(@Parameter(description = "Новый email, кол-во символов до 30") @RequestBody EmailDTO emailDTO) throws AccessException {
        var result = emailService.updateEmail(emailDTO);
        log.warn("updated email {}", emailDTO.getNewEmail());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/email/{email}")
    @Operation(description = "Позволяет добавлять email")
    public ResponseEntity<String> addEmail(@Parameter(description = "Новый email, кол-во символов до 30") @PathVariable String email) throws AccessException {
        var result = emailService.addEmail(email);
        log.warn("added email {}", email);
        return ResponseEntity.ok("Added email " + result);
    }

    @DeleteMapping("/email/{email}")
    @Operation(description = "Позволяет удалять email")
    public ResponseEntity<String> deleteEmail(@Parameter(description = "email для удаления") @PathVariable String email) throws AccessException {
        var result = emailService.deleteEmail(email);
        log.warn("deleted email {}", email);
        return ResponseEntity.ok(result);
    }
}
