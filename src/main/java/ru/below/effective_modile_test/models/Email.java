package ru.below.effective_modile_test.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Entity
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "_email")
public class Email {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true, nullable = false)
    @org.hibernate.validator.constraints.Email
    @Size(min = 3, max = 30)
    private String email;
}
