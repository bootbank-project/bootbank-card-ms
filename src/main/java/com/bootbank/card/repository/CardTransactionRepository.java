package com.bootbank.card.repository;

import com.bootbank.card.model.entity.CardTransaction;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CardTransactionRepository extends JpaRepository<CardTransaction, Long> {

    @Query("SELECT t FROM CardTransaction t " +
            "JOIN t.userCard c " +
            "WHERE c.clientCif = :clientCif " +
            "ORDER BY t.createdAt DESC")
    List<CardTransaction> findByClientCifOrderByCreatedAtDesc(@Param("clientCif") String clientCif);
}
