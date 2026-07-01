package com.bootbank.template.dto;

import java.time.LocalDateTime;

public class CardStatusResponse {
    private String cardNumber;
    private String status;
    private LocalDateTime updatedAt;

    public CardStatusResponse(String cardNumber, String status, LocalDateTime updatedAt) {
        this.cardNumber = cardNumber;
        this.status = status;
        this.updatedAt = updatedAt;
    }

    // Getter-lər
    public String getCardNumber() { return cardNumber; }
    public String getStatus() { return status; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}