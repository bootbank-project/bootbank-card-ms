package com.bootbank.card.repository;

import com.bootbank.card.entity.CardProduct;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardProductRepository extends JpaRepository<CardProduct, Long> {
    Optional<CardProduct> findByCode(String code);
}
