package com.bootbank.card.repository;

import com.bootbank.card.model.entity.UserCard;
import com.bootbank.card.model.enums.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<UserCard, Long> {

    boolean existsByCardNumber(String cardNumber);

    boolean existsByClientCifAndCardType(String clientCif, CardType cardType);

    Optional<UserCard> findByCardNumber(String cardNumber);
}