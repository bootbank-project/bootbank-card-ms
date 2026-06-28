package com.bootbank.card.repository;

import com.bootbank.card.entity.UserCard;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCardRepository extends JpaRepository<UserCard, Long> {
    boolean existsByClientCifAndCardType(String clientCif, String cardType);
    boolean existsByCardNumber(String cardNumber);
    List<UserCard> findByClientCif(String clientCif);
}
