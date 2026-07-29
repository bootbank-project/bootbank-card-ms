package com.bootbank.card.dto.response;

import com.bootbank.card.model.enums.CardStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardStatusResponse {

    private String cardNumber;
    private CardStatus status;
    private LocalDateTime updatedAt;
}