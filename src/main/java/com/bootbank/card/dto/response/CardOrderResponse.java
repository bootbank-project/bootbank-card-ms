package com.bootbank.card.dto.response;

import java.math.BigDecimal;

public record CardOrderResponse(
        String cardProductCode,
        String cardNumber,
        String expiryDate,
        String cardType,
        String currency,
        BigDecimal balance,
        BigDecimal creditLimit,
        BigDecimal usedLimit
) {
}
