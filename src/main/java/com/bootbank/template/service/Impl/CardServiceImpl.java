package com.bootbank.template.service.Impl;

import com.bootbank.template.dto.request.CardOrderRequest;
import com.bootbank.template.dto.response.CardOrderResponse;
import com.bootbank.template.exception.BusinessException;
import com.bootbank.template.exception.enums.ErrorCode;
import com.bootbank.template.model.entity.Card;
import com.bootbank.template.model.entity.User;
import com.bootbank.template.model.enums.CardType;
import com.bootbank.template.model.enums.Currency;
import com.bootbank.template.repository.CardRepository;
import com.bootbank.template.repository.UserRepository;
import com.bootbank.template.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private static final BigDecimal MIN_SALARY_FOR_CREDIT = new BigDecimal("1000");
    private static final BigDecimal CREDIT_LIMIT_RATIO = new BigDecimal("0.45");

    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    @Transactional
    public CardOrderResponse orderCard(CardOrderRequest request) {
        Card cardProduct = cardRepository.findByCode(request.cardProductCode())
                .orElseThrow(() -> new BusinessException(
                        ErrorCode.CARD_PRODUCT_NOT_FOUND,
                        "Kart məhsulu tapılmadı: " + request.cardProductCode()
                ));

        CardType cardType = cardProduct.getType();
        boolean isCredit = cardType.getCategory() == CardType.CardCategory.CREDIT;

        BigDecimal balance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal creditLimit = null;
        BigDecimal usedLimit = null;

        if (isCredit) {
            validateCreditCardRules(request);
            creditLimit = request.salary()
                    .multiply(CREDIT_LIMIT_RATIO)
                    .setScale(2, RoundingMode.HALF_DOWN);
            usedLimit = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN);
        }

        String rawCardNumber = generateUniqueCardNumber();
        String maskedCardNumber = maskCardNumber(rawCardNumber);
        String expiryDate = calculateExpiryDate();

        Timestamp now = new Timestamp(System.currentTimeMillis());

        User user = User.builder()
                .clientCif(request.clientCif())
                .clientName(request.clientName())
                .clientLastname(request.clientLastname())
                .cardProductCode(cardProduct)
                .cardNumber(rawCardNumber)
                .expiryDate(expiryDate)
                .cardType(cardType)
                .currency(request.currency())
                .salary(request.salary())
                .balance(balance)
                .creditLimit(creditLimit)
                .usedLimit(usedLimit)
                .createdDate(now)
                .updatedDate(now)
                .build();

        userRepository.save(user);

        return new CardOrderResponse(
                cardProduct.getCode(),
                maskedCardNumber,
                expiryDate,
                cardType.name(),
                request.currency().name(),
                balance,
                creditLimit,
                usedLimit
        );
    }

    private void validateCreditCardRules(CardOrderRequest request) {
        if (request.currency() != Currency.AZN) {
            throw new BusinessException(
                    ErrorCode.CREDIT_CARD_CURRENCY_MUST_BE_AZN,
                    "Kredit kartı yalnız AZN valyutasında sifariş edilə bilər."
            );
        }

        if (request.salary().compareTo(MIN_SALARY_FOR_CREDIT) < 0) {
            throw new BusinessException(
                    ErrorCode.INSUFFICIENT_SALARY,
                    "Kredit kartı üçün minimum maaş 1000 AZN olmalıdır."
            );
        }

        List<CardType> creditCardTypes = Arrays.stream(CardType.values())
                .filter(ct -> ct.getCategory() == CardType.CardCategory.CREDIT)
                .collect(Collectors.toList());

        if (userRepository.existsByClientCifAndCardTypeIn(request.clientCif(), creditCardTypes)) {
            throw new BusinessException(
                    ErrorCode.ACTIVE_CREDIT_CARD_EXISTS,
                    "Bu CIF ilə artıq aktiv kredit kartı mövcuddur."
            );
        }
    }

    private String generateUniqueCardNumber() {
        String cardNumber;
        do {
            StringBuilder sb = new StringBuilder(16);
            for (int i = 0; i < 16; i++) {
                sb.append(secureRandom.nextInt(10));
            }
            cardNumber = sb.toString();
        } while (userRepository.existsByCardNumber(cardNumber));
        return cardNumber;
    }

    private String maskCardNumber(String cardNumber) {
        return "•••• •••• •••• " + cardNumber.substring(12);
    }

    private String calculateExpiryDate() {
        LocalDate expiryLocalDate = LocalDate.now().plusYears(3);
        String year = String.valueOf(expiryLocalDate.getYear()).substring(2);
        return String.format("%02d/%s", expiryLocalDate.getMonthValue(), year);
    }
}
