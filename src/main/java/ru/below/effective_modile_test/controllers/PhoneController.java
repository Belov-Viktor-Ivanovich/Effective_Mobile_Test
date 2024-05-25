package ru.below.effective_modile_test.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.AccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.below.effective_modile_test.dto.PhoneDTO;
import ru.below.effective_modile_test.services.PhoneService;
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name="PhoneController", description="CRUD Phones")
@RequestMapping("/api/v1/phones")
public class PhoneController {
    private final PhoneService phoneService;
    @PostMapping("/addPhone/{phone}")
    @Operation(description = "Позволяет добавлять телефон")
    public ResponseEntity<String> addPhone(@Parameter(description = "Новый номер телефона") @PathVariable String phone) throws AccessException {
        phoneService.addPhone(phone);
        log.warn("added phone {}", phone);
        return ResponseEntity.ok("Added phone successfully");
    }
    @PutMapping("/updatePhone")
    @Operation(description = "Позволяет изменять телефон")
    public ResponseEntity<String> updatePhone(@Parameter(description = "Новый номер телефона")@RequestBody PhoneDTO phoneDTO) throws AccessException {
        var result = phoneService.updatePhone(phoneDTO);
        log.warn("updated phone {}", phoneDTO.getNewPhone());
        return ResponseEntity.ok(result);
    }
    @DeleteMapping("/deletePhone/{phone}")
    @Operation(description = "Позволяет удалять телефон")
    public ResponseEntity<String> deletePhone(@Parameter(description = "телефон для удаления")@PathVariable String phone) throws AccessException {
        var result = phoneService.deletePhone(phone);
        log.warn("deleted phone {}", phone);
        return ResponseEntity.ok(result);
    }
}
