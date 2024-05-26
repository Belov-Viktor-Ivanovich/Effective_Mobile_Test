package ru.below.effective_modile_test.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.below.effective_modile_test.models.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUsersByName(String name);

    @Query("select ac from User ac where ac.birthday>= :birthDay")
    List<User> indAllSuitableForInterestBirthDay(LocalDate birthDay);

    @Query("SELECT ac FROM User ac WHERE ac.name LIKE :username%")
    List<User> findByUsernameStartsWith(@Param("username") String username);

}
