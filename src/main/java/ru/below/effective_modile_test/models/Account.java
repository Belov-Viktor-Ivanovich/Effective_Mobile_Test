package ru.below.effective_modile_test.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
@Component
@Table(name = "_account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Min(value = 0,message = "The balance cannot be negative")
    private BigDecimal balance;
    private BigDecimal initialBalance;

    public void addBalance(@NonNull BigDecimal amount) {
        this.balance = this.balance.add(amount);
    }
    public void subtractFromBalance(@NonNull BigDecimal amount) {
        var result = this.balance.subtract(amount);
        if (result.signum() < 0) {
            throw new RuntimeException("Недостаточно средств для проведения транзакции.");
        }

        this.balance = result;
    }


}
