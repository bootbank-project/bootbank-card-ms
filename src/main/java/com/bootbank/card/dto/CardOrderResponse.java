package com.bootbank.card.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardOrderResponse {
    private String cardProductCode;
    private String cardNumber;
    private String expiryDate;
    private String cardType;
    private String currency;
    private BigDecimal balance;
    private BigDecimal creditLimit;
    private BigDecimal usedLimit;
}
