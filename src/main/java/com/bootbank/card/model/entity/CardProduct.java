package com.bootbank.card.model.entity;

import com.bootbank.card.model.enums.CardType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "card_products")
@FieldDefaults(level = AccessLevel.PRIVATE)
@EntityListeners(AuditingEntityListener.class)
public class CardProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_products_seq_generator")
    @SequenceGenerator(name = "card_products_seq_generator", sequenceName = "card_products_seq", allocationSize = 1)
    Long id;

    @NotBlank(message = "Card code cannot be null or blank")
    @Column(nullable = false, unique = true)
    String code;

    @NotBlank(message = "Card name cannot be null or blank")
    @Column(nullable = false)
    String name;

    @NotNull(message = "Card type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false, length = 30)
    CardType type;

    @CreatedDate
    @Column(name = "created_date", updatable = false, nullable = false)
    Instant createdAt;

    @LastModifiedDate
    @Column(name = "updated_date", nullable = false)
    Instant updatedAt;
}
