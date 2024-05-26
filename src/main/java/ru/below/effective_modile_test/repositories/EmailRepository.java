package ru.below.effective_modile_test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.below.effective_modile_test.models.Email;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailRepository extends JpaRepository<Email, UUID> {
    Optional<Email> findEmailsByEmail(String email);
}
