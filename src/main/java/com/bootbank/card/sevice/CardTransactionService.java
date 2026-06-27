package com.bootbank.card.service;

import com.bootbank.card.entity.CardTransaction;
import com.bootbank.card.repository.CardTransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardTransactionService {

    private final CardTransactionRepository cardTransactionRepository;

    public CardTransactionService(CardTransactionRepository cardTransactionRepository) {
        this.cardTransactionRepository = cardTransactionRepository;
    }

    public List<CardTransaction> getTransactionsByClientCif(String clientCif) {
        List<CardTransaction> transactions =
                cardTransactionRepository.findByClientCifOrderByCreatedAtDesc(clientCif);

        if (transactions == null || transactions.isEmpty()) {
            return List.of();
        }

        return transactions;
    }
}
