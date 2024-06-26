package ru.below.effective_modile_test.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Entity
@EqualsAndHashCode
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "_phone")
public class Phone {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(unique = true, nullable = false)
    @Pattern(regexp = "^(0|[1-9][0-9]*)$", message = "not is number")
    @Size(min = 7, max = 11)
    private String phone;
}
