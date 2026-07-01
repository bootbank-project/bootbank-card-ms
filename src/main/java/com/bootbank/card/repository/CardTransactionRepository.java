package com.bootbank.card.repository;

import com.bootbank.card.entity.CardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CardTransactionRepository extends JpaRepository<CardTransaction, Long> {

    @Query("SELECT t FROM CardTransaction t " +
            "JOIN user_cards c ON t.card_id = c.id " +
            "WHERE c.client_cif = :clientCif " +
            "ORDER BY t.created_at DESC")
    List<CardTransaction> findByClientCifOrderByCreatedAtDesc(@Param("clientCif") String clientCif);
}