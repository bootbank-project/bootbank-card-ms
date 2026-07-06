package com.bootbank.template.dto.request;

import com.bootbank.template.model.enums.Currency;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CardOrderRequest(

        @NotBlank(message = "Client CIF is required")
        @Size(max = 6, message = "Client CIF must be at most 6 characters")
        String clientCif,

        @NotBlank(message = "Client name is required")
        String clientName,

        @NotBlank(message = "Client lastname is required")
        String clientLastname,

        @NotBlank(message = "Card product code is required")
        String cardProductCode,

        @NotNull(message = "Currency is required")
        Currency currency,

        @NotNull(message = "Salary is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "Salary cannot be negative")
        @Digits(integer = 15, fraction = 2, message = "Salary format is invalid (max 15 digits and 2 decimals allowed)")
        BigDecimal salary
) {
}
