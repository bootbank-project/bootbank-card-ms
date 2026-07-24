package com.bootbank.card.repository;

import com.bootbank.card.model.entity.CardProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardProductRepository extends JpaRepository<CardProduct, Long> {

    Optional<CardProduct> findByCode(String code);
}
