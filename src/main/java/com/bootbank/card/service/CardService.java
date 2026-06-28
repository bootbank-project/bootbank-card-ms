package com.bootbank.card.service;

import com.bootbank.card.dto.CardOrderRequest;
import com.bootbank.card.dto.CardOrderResponse;
import com.bootbank.card.dto.CardProductListResponse;
import com.bootbank.card.dto.TransactionListResponse;
import com.bootbank.card.dto.TransactionResponse;

public interface CardService {
    CardProductListResponse getCardProducts();
    CardOrderResponse createCardOrder(String clientCif, String clientFirstName, String clientLastName, CardOrderRequest request);
    TransactionListResponse getTransactionHistory(String clientCif);
    TransactionResponse getTransactionDetails(Long id, String clientCif);
}
