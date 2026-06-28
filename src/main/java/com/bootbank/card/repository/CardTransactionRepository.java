package com.bootbank.card.repository;

import com.bootbank.card.entity.CardTransaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CardTransactionRepository extends JpaRepository<CardTransaction, Long> {

    @Query("SELECT t FROM CardTransaction t JOIN FETCH t.card c WHERE c.clientCif = :clientCif ORDER BY t.createdAt DESC")
    List<CardTransaction> findAllByClientCifOrderByCreatedAtDesc(@Param("clientCif") String clientCif);
}
