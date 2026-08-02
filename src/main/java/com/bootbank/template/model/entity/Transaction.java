package com.bootbank.template.model.entity;

import com.bootbank.template.model.enums.TransactionStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "card_transactions")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotNull(message = "Card is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    Card card;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    String title;

    @NotBlank(message = "Category is required")
    @Column(nullable = false)
    String category;

    @NotNull(message = "Amount is required")
    @Column(nullable = false, precision = 15, scale = 2)
    BigDecimal amount;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    TransactionStatus status;

    @Column(name = "created_at", nullable = false)
    LocalDateTime createdAt;
}
