package com.bootbank.card.controller;

import com.bootbank.card.entity.CardTransaction;
import com.bootbank.card.service.CardTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cards")
public class CardTransactionController {

    private final CardTransactionService cardTransactionService;

    public CardTransactionController(CardTransactionService cardTransactionService) {
        this.cardTransactionService = cardTransactionService;
    }

    @Operation(
            summary = "Müştərinin tranzaksiya tarixçəsi",
            description = "Verilən CIF koduna görə müştərinin bütün tranzaksiyalarını qaytarır.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Uğurlu cavab — tranzaksiya siyahısı qaytarıldı"),
                    @ApiResponse(responseCode = "403", description = "Müştərinin kartına aidiyyatı yox"),
                    @ApiResponse(responseCode = "500", description = "Server xətası")
            }
    )
    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(@RequestHeader("X-Client-CIF") String clientCif) {
        List<CardTransaction> transactions = cardTransactionService.getTransactionsByClientCif(clientCif);

        if (transactions.isEmpty()) {
            return ResponseEntity.ok(new TransactionResponse(List.of(), 0));
        }

        return ResponseEntity.ok(new TransactionResponse(transactions, transactions.size()));
    }

    static class TransactionResponse {
        private List<CardTransaction> items;
        private int total;

        public TransactionResponse(List<CardTransaction> items, int total) {
            this.items = items;
            this.total = total;
        }

        public List<CardTransaction> getItems() {
            return items;
        }

        public int getTotal() {
            return total;
        }
    }
}
