package com.bootbank.template.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.ALWAYS)
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
