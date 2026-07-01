package com.bootbank.template.controller;

import com.bootbank.template.dto.CardStatusRequest;
import com.bootbank.template.dto.CardStatusResponse;
import com.bootbank.template.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bootbank-card-ms/api/v1/cards")
public class HelloBootbankers {

    private final CardService cardService;

    public HelloBootbankers(CardService cardService) {
        this.cardService = cardService;
    }

    @PatchMapping("/{cardNumber}/status")
    public ResponseEntity<CardStatusResponse> updateCardStatus(
            @PathVariable String cardNumber,
            @RequestHeader("X-Client-CIF") String cif,
            @Valid @RequestBody CardStatusRequest request) {

        CardStatusResponse response = cardService.updateCardStatus(cardNumber, request, cif);
        return ResponseEntity.ok(response);
    }
}
