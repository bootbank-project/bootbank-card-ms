package com.bootbank.card.service;

import com.bootbank.card.dto.request.CardOrderRequest;
import com.bootbank.card.dto.request.CardStatusUpdateRequest;
import com.bootbank.card.dto.response.CardOrderResponse;
import com.bootbank.card.dto.response.CardStatusResponse;

public interface CardService {

    CardOrderResponse orderCard(String clientCif, String clientName, String clientLastname, CardOrderRequest request);

    CardStatusResponse updateCardStatus(String clientCif, String cardNumber, CardStatusUpdateRequest request);
}