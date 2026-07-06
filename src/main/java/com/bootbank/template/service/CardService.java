package com.bootbank.template.service;

import com.bootbank.template.dto.request.CardOrderRequest;
import com.bootbank.template.dto.response.CardOrderResponse;

public interface CardService {

    CardOrderResponse orderCard(CardOrderRequest request);
}
