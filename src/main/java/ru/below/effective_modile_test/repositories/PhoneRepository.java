package ru.below.effective_modile_test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.below.effective_modile_test.models.Phone;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, UUID> {
    Optional<Phone> findPhonesByPhone(String phone);
}
