package com.bootbank.card.dto.request;

import com.bootbank.card.model.enums.CardStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CardStatusUpdateRequest {

    @NotNull(message = "Status bos ola bilmez")
    private CardStatus status;
}