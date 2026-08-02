package com.bootbank.template.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(
        Long id,
        String cardNumber,
        String title,
        String category,
        BigDecimal amount,
        String status,
        LocalDateTime createdAt
) {
}
