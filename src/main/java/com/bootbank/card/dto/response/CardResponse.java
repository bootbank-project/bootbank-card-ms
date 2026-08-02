package com.bootbank.card.dto.response;

import java.math.BigDecimal;

public record CardResponse(
        Long id,
        String cardProductCode,
        String cardNumber,
        String expiryDate,
        String cardType,
        String currency,
        BigDecimal balance,
        BigDecimal creditLimit,
        BigDecimal usedLimit,
        String status,
        boolean onlinePayment,
        boolean contactless
) {}
