package com.bootbank.template.controller;

import com.bootbank.template.dto.request.CardOrderRequest;
import com.bootbank.template.dto.response.CardOrderResponse;
import com.bootbank.template.service.CardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
@Validated
public class CardController {

    private final CardService cardService;

    @PostMapping("/order")
    @ResponseStatus(HttpStatus.CREATED)
    public CardOrderResponse orderCard(
            @RequestHeader("X-Client-Cif")
            @NotBlank(message = "X-Client-Cif header is required")
            @Size(min = 6, max = 6, message = "X-Client-Cif must be exactly 6 characters")
            String clientCif,

            @RequestHeader("X-Client-Name")
            @NotBlank(message = "X-Client-Name header is required")
            String clientName,

            @RequestHeader("X-Client-Lastname")
            @NotBlank(message = "X-Client-Lastname header is required")
            String clientLastname,

            @Valid @RequestBody CardOrderRequest request
    ) {
        return cardService.orderCard(clientCif, clientName, clientLastname, request);
    }
}
