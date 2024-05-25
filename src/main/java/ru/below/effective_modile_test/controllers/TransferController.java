package ru.below.effective_modile_test.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.expression.AccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.below.effective_modile_test.dto.AccountTransfer;
import ru.below.effective_modile_test.services.impl.AccountServiceImpl;

@RestController
@RequiredArgsConstructor
@Tag(name="UserController", description="Money transfer")
@RequestMapping("/api/v1/transfer")
public class TransferController {
    private final AccountServiceImpl accountServiceImpl;
    @PutMapping("/transferAmount")
    @Operation(description = "Позволяет отправить деньги другому пользователю")
    public ResponseEntity<String> transferAmount(@Parameter(description = "информация по переводу денег")@RequestBody AccountTransfer accountTransfer) throws AccessException {
        return  ResponseEntity.ok(accountServiceImpl.transfer(accountTransfer));
    }
}
