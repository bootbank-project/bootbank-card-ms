package com.bootbank.card.service;

import com.bootbank.card.dto.request.CardOrderRequest;
import com.bootbank.card.dto.response.CardOrderResponse;
import com.bootbank.card.dto.response.CardResponse;
import com.bootbank.card.dto.response.TransactionResponse;
import java.util.List;

public interface CardService {

    CardOrderResponse orderCard(String clientCif, String clientName, String clientLastname, CardOrderRequest request);

    TransactionResponse getTransactionDetails(Long id, String clientCif);

    List<CardResponse> getCardsByCif(String clientCif);
}
