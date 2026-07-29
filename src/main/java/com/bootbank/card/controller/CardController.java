package com.bootbank.card.controller;

import com.bootbank.card.dto.request.CardOrderRequest;
import com.bootbank.card.dto.request.CardStatusUpdateRequest;
import com.bootbank.card.dto.response.CardOrderResponse;
import com.bootbank.card.dto.response.CardStatusResponse;
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

    @PatchMapping("/{cardNumber}/status")
    @ResponseStatus(HttpStatus.OK)
    public CardStatusResponse updateCardStatus(
            @RequestHeader("cif")
            @NotBlank(message = "X-Client-Cif header is required")
            @Pattern(regexp = "^C\\d{6}$", message = "Invalid CIF format")
            String cif,

            @PathVariable String cardNumber,

            @Valid @RequestBody CardStatusUpdateRequest request
    ) {
        return cardService.updateCardStatus(cif, cardNumber, request);
    }
}