//Nümunə olaraq hazırlanıb.
//27.06.2026 (by Nijat)

package com.bootbank.card.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "user_cards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_cif", nullable = false, length = 50)
    private String clientCif;

    @Column(name = "client_name", nullable = false, length = 50)
    private String clientName;

    @Column(name = "client_lastname", nullable = false, length = 50)
    private String clientLastName;

    @Column(name = "card_product_code", nullable = false, length = 50)
    private String cardProductCode;

    @Column(name = "card_number", nullable = false, unique = true, length = 30)
    private String cardNumber;

    @Column(name = "expiry_date", nullable = false, length = 5)
    private String expiryDate;

    @Column(name = "card_type", nullable = false, length = 20)
    private String cardType;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "salary", nullable = false, precision = 15, scale = 2)
    private Double salary;

    @Column(name = "balance", nullable = false, precision = 15, scale = 2)
    private Double balance;

    @Column(name = "credit_limit", precision = 15, scale = 2)
    private Double creditLimit;

    @Column(name = "used_limit", precision = 15, scale = 2)
    private Double usedLimit;

    @Column(name = "created_date", nullable = false)
    private java.time.LocalDateTime createdDate;

    @Column(name = "updated_date")
    private java.time.LocalDateTime updatedDate;
}
