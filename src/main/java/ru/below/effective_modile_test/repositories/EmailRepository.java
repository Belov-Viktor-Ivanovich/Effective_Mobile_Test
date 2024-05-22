package ru.below.effective_modile_test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.below.effective_modile_test.models.Email;
import ru.below.effective_modile_test.models.Phone;

import java.util.Optional;
import java.util.UUID;

public interface EmailRepository extends JpaRepository<Email, UUID> {
    Optional<Email> findEmailsByEmail(String email);
}
