package com.bootbank.card.controller;

import com.bootbank.card.dto.request.CardOrderRequest;
import com.bootbank.card.dto.response.CardOrderResponse;
import com.bootbank.card.service.CardService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
            @RequestHeader("cif")
            @NotBlank(message = "X-Client-Cif header is required")
            @Pattern(regexp = "^C\\d{6}$", message = "Invalid CIF format")
            String cif,

            @RequestHeader("name")
            @NotBlank(message = "name header is required")
            String name,

            @RequestHeader("surname")
            @NotBlank(message = "surname header is required")
            String surname,

            @Valid @RequestBody CardOrderRequest request
    ) {
        return cardService.orderCard(cif, name, surname, request);
    }

    @GetMapping("/transactions/{id}")
    public com.bootbank.card.dto.response.TransactionResponse getTransactionDetails(
            @PathVariable("id") Long id,
            @RequestHeader("X-Client-CIF") String clientCif
    ) {
        return cardService.getTransactionDetails(id, clientCif);
    }
}
