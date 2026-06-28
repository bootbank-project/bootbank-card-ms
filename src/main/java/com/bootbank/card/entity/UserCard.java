package com.bootbank.card.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_cards")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_cif", nullable = false, length = 50)
    private String clientCif;

    @Column(name = "client_name", nullable = false, length = 50)
    private String clientName;

    @Column(name = "client_last_name", nullable = false, length = 50)
    private String clientLastName;

    @Column(name = "card_product_code", nullable = false, length = 50)
    private String cardProductCode;

    @Column(name = "card_number", nullable = false, unique = true, length = 30)
    private String cardNumber;

    @Column(name = "expiry_date", nullable = false, length = 5)
    private String expiryDate;

    @Column(name = "card_type", nullable = false, length = 20)
    private String cardType;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal salary;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    @Column(name = "credit_limit", precision = 15, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "used_limit", precision = 15, scale = 2)
    private BigDecimal usedLimit;

    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Column(name = "updated_date")
    private LocalDateTime updatedDate;
}
