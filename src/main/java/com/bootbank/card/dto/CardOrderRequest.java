package com.bootbank.card.dto;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardOrderRequest {
    @NotBlank(message = "Card product code is required")
    private String cardProductCode;

    @NotBlank(message = "Currency is required")
    private String currency;

    private BigDecimal salary;
}
