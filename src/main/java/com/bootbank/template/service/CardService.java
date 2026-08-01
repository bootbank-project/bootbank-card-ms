package com.bootbank.template.service;

import com.bootbank.template.dto.request.CardOrderRequest;
import com.bootbank.template.dto.response.CardOrderResponse;
import com.bootbank.template.dto.response.CardProductsResponse;

import java.util.List;

public interface CardService {

    CardOrderResponse orderCard(String clientCif, String clientName, String clientLastname, CardOrderRequest request);

    List<CardProductsResponse> getAllCardProducts();
}
