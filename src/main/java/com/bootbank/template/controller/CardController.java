package com.bootbank.template.controller;

import com.bootbank.template.dto.request.CardOrderRequest;
import com.bootbank.template.dto.response.CardOrderResponse;
import com.bootbank.template.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @PostMapping("/order")
    @ResponseStatus(HttpStatus.CREATED)
    public CardOrderResponse orderCard(@Valid @RequestBody CardOrderRequest request) {
        return cardService.orderCard(request);
    }
}
