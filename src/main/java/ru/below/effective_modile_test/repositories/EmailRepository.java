package ru.below.effective_modile_test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.below.effective_modile_test.models.Email;
import ru.below.effective_modile_test.models.Phone;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface EmailRepository extends JpaRepository<Email, UUID> {
    List<Email>findAllBy();
    Optional<Email> findEmailsByEmail(String email);
}
