package com.bootbank.card.service;

import com.bootbank.card.dto.request.CardOrderRequest;
import com.bootbank.card.dto.response.CardOrderResponse;
import com.bootbank.card.dto.response.TransactionResponse;

public interface CardService {

    CardOrderResponse orderCard(String clientCif, String clientName, String clientLastname, CardOrderRequest request);

    TransactionResponse getTransactionDetails(Long id, String clientCif);
}
