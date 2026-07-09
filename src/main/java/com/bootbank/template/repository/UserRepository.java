package com.bootbank.template.repository;

import com.bootbank.template.model.entity.User;
import com.bootbank.template.model.enums.CardType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByCardNumber(String cardNumber);

    boolean existsByClientCifAndCardType(String clientCif, CardType cardType);
}
