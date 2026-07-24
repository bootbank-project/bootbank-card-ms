package com.bootbank.card.model.entity;

import com.bootbank.card.model.enums.CardType;
import com.bootbank.card.model.enums.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user_cards")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCard {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_seq_generator")
    @SequenceGenerator(name = "card_seq_generator", sequenceName = "CARD_SEQ", allocationSize = 1)
    Long id;

    @NotBlank(message = "Client CIF is required")
    @Size(min = 7, max = 7, message = "Client CIF must be exactly 7 characters")
    @Column(name = "cif", nullable = false, length = 7)
    String clientCif;

    @NotBlank(message = "Client name cannot be blank")
    @Column(name = "name", nullable = false)
    String clientName;

    @NotBlank(message = "Client lastname cannot be blank")
    @Column(name = "surname", nullable = false)
    String clientLastname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_product_code", referencedColumnName = "code", nullable = false)
    CardProduct cardProductCode;

    @NotBlank(message = "Card number is required")
    @Column(name = "card_number", unique = true, nullable = false)
    String cardNumber;

    @NotBlank(message = "Expiry date is required")
    @Pattern(regexp = "^(0[1-9]|1[0-2])\\/([0-9]{2})$", message = "Expiry date must be in MM/YY format")
    @Column(name = "expiry_date", nullable = false, length = 5)
    String expiryDate;

    @NotNull(message = "Card type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false, length = 30)
    CardType cardType;

    @NotNull(message = "Currency is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 3)
    Currency currency;

    @NotNull(message = "Salary cannot be null")
    @DecimalMin(value = "0.0", message = "Salary cannot be negative")
    @Column(nullable = false, precision = 15, scale = 2)
    BigDecimal salary;

    @NotNull(message = "Balance cannot be null")
    @Column(nullable = false, precision = 15, scale = 2)
    BigDecimal balance;

    @Column(name = "credit_limit", precision = 15, scale = 2)
    BigDecimal creditLimit;

    @Column(name = "used_limit", precision = 15, scale = 2)
    BigDecimal usedLimit;

    @Column(name = "created_date", updatable = false, nullable = false)
    Timestamp createdDate;

    @Column(name = "updated_date", nullable = false)
    Timestamp updatedDate;
}
