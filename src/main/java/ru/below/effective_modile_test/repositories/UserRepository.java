package ru.below.effective_modile_test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.below.effective_modile_test.models.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    public Optional<User> findUsersByName(String name);
    //public Optional<User> findUsersByPhones(String email);
}
