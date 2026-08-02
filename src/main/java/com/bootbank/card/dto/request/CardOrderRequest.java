package com.bootbank.card.dto.request;

import com.bootbank.card.model.enums.CardProductCode;
import com.bootbank.card.model.enums.Currency;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CardOrderRequest(

        @NotNull(message = "Card product code is required")
        CardProductCode cardProductCode,

        @NotNull(message = "Currency is required")
        Currency currency,

        @NotNull(message = "Salary is required")
        @DecimalMin(value = "0.0", message = "Salary cannot be negative")
        @Digits(integer = 15, fraction = 2, message = "Salary format is invalid (max 15 digits and 2 decimals allowed)")
        BigDecimal salary
) {
}
