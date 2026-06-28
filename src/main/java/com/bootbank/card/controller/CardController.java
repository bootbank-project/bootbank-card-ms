package com.bootbank.card.controller;

import com.bootbank.card.dto.CardOrderRequest;
import com.bootbank.card.dto.CardOrderResponse;
import com.bootbank.card.dto.CardProductListResponse;
import com.bootbank.card.dto.TransactionListResponse;
import com.bootbank.card.dto.TransactionResponse;
import com.bootbank.card.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
@Tag(name = "Card Service API", description = "APIs for managing bank cards, products, and transactions")
public class CardController {

    private final CardService cardService;

    @GetMapping("/products")
    @Operation(summary = "List Card Products", description = "Retrieves all available debit and credit card products.")
    public CardProductListResponse getCardProducts() {
        return cardService.getCardProducts();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Order New Card", description = "Creates a new debit or credit card order for a customer based on headers and request body.")
    public CardOrderResponse createCardOrder(
            @Parameter(description = "Customer's unique CIF number", required = true)
            @RequestHeader("X-Client-CIF") String clientCif,
            @Parameter(description = "Customer's first name", required = true)
            @RequestHeader("X-Client-FirstName") String clientFirstName,
            @Parameter(description = "Customer's last name", required = true)
            @RequestHeader("X-Client-LastName") String clientLastName,
            @Valid @RequestBody CardOrderRequest request) {
        return cardService.createCardOrder(clientCif, clientFirstName, clientLastName, request);
    }

    @GetMapping("/transactions")
    @Operation(summary = "List Transaction History", description = "Retrieves the transaction history for all cards owned by the customer identified by CIF.")
    public TransactionListResponse getTransactionHistory(
            @Parameter(description = "Customer's unique CIF number", required = true)
            @RequestHeader("X-Client-CIF") String clientCif) {
        return cardService.getTransactionHistory(clientCif);
    }

    @GetMapping("/transactions/{id}")
    @Operation(summary = "Get Transaction Details", description = "Retrieves details of a specific transaction by ID, with cross-client security verification.")
    public TransactionResponse getTransactionDetails(
            @Parameter(description = "Transaction unique database ID", required = true)
            @PathVariable("id") Long id,
            @Parameter(description = "Customer's unique CIF number", required = true)
            @RequestHeader("X-Client-CIF") String clientCif) {
        return cardService.getTransactionDetails(id, clientCif);
    }
}
