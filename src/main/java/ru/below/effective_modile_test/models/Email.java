package ru.below.effective_modile_test.models;

import jakarta.persistence.*;
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
    private String email;
}
