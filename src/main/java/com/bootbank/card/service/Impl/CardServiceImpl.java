package com.bootbank.card.service.Impl;

import com.bootbank.card.dto.request.CardOrderRequest;
import com.bootbank.card.dto.request.CardStatusUpdateRequest;
import com.bootbank.card.dto.response.CardOrderResponse;
import com.bootbank.card.dto.response.CardStatusResponse;
import com.bootbank.card.exception.RecordNotFoundException;
import com.bootbank.card.exception.enums.ErrorCode;
import com.bootbank.card.model.entity.UserCard;
import com.bootbank.card.model.entity.CardProduct;
import com.bootbank.card.model.enums.CardProductCode;
import com.bootbank.card.model.enums.CardType;
import com.bootbank.card.model.enums.Currency;
import com.bootbank.card.repository.CardRepository;
import com.bootbank.card.repository.CardProductRepository;
import com.bootbank.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private static final BigDecimal MIN_SALARY_FOR_CREDIT = new BigDecimal("1000");
    private static final BigDecimal CREDIT_LIMIT_RATIO = new BigDecimal("0.45");

    private final CardProductRepository cardProductRepository;
    private final CardRepository cardRepository;
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    @Transactional
    public CardOrderResponse orderCard(String clientCif, String clientName, String clientLastname,
                                       CardOrderRequest request) {
        CardProductCode productCode = request.cardProductCode();
        CardProduct cardProduct = cardProductRepository.findByCode(productCode.name())
                .orElseThrow(() -> new RecordNotFoundException(
                        ErrorCode.CARD_PRODUCT_NOT_FOUND,
                        "Kart məhsulu tapılmadı: " + productCode.name()
                ));

        CardType cardType = cardProduct.getType();
        if (cardType != productCode.getCardType()) {
            throw new RecordNotFoundException(
                    ErrorCode.INVALID_CARD_TYPE,
                    "Kart məhsulu tipi uyğun deyil: " + productCode.name()
            );
        }

        boolean isCredit = cardType == CardType.CREDIT;

        BigDecimal balance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal creditLimit = null;
        BigDecimal usedLimit = null;

        if (isCredit) {
            validateCreditCardRules(clientCif, request);
            creditLimit = request.salary()
                    .multiply(CREDIT_LIMIT_RATIO)
                    .setScale(2, RoundingMode.HALF_DOWN);
            usedLimit = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_DOWN);
        }

        String rawCardNumber = generateUniqueCardNumber();
        String maskedCardNumber = maskCardNumber(rawCardNumber);
        String expiryDate = calculateExpiryDate();

        Timestamp now = new Timestamp(System.currentTimeMillis());

        UserCard issuedCard = UserCard.builder()
                .clientCif(clientCif)
                .clientName(clientName)
                .clientLastname(clientLastname)
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

        cardRepository.save(issuedCard);

        return new CardOrderResponse(
                productCode.name(),
                maskedCardNumber,
                expiryDate,
                cardType.name(),
                request.currency().name(),
                balance,
                creditLimit,
                usedLimit
        );
    }

    @Override
    @Transactional
    public CardStatusResponse updateCardStatus(String clientCif, String cardNumber, CardStatusUpdateRequest request) {
        // 1. Kartın DB-dən tapılması
        UserCard card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new RecordNotFoundException(
                        ErrorCode.CARD_NOT_FOUND,
                        "Kart tapılmadı: " + cardNumber
                ));

        // 2. Ownership Check (CIF yoxlanışı)
        if (!clientCif.equals(card.getClientCif())) {
            throw new RecordNotFoundException(
                    ErrorCode.FORBIDDEN,
                    "Bu kart üzrə əməliyyat aparmaq icazəniz yoxdur."
            );
        }

        // 3. Eyni status yoxlanışı (Lazımsız DB update-in qarşısını almaq üçün)
        if (card.getStatus() == request.getStatus()) {
            return mapToCardStatusResponse(card);
        }

        // 4. Statusun yenilənməsi
        card.setStatus(request.getStatus());
        card.setUpdatedDate(new Timestamp(System.currentTimeMillis()));
        UserCard updatedCard = cardRepository.save(card);

        return mapToCardStatusResponse(updatedCard);
    }

    private CardStatusResponse mapToCardStatusResponse(UserCard card) {
        LocalDateTime updatedAt = card.getUpdatedDate() != null
                ? card.getUpdatedDate().toLocalDateTime()
                : LocalDateTime.now();

        return CardStatusResponse.builder()
                .cardNumber(maskCardNumber(card.getCardNumber()))
                .status(card.getStatus())
                .updatedAt(updatedAt)
                .build();
    }

    private void validateCreditCardRules(String clientCif, CardOrderRequest request) {
        if (request.currency() != Currency.AZN) {
            throw new RecordNotFoundException(
                    ErrorCode.CREDIT_CARD_CURRENCY_MUST_BE_AZN,
                    "Kredit kartı yalnız AZN valyutasında sifariş edilə bilər."
            );
        }

        if (request.salary().compareTo(MIN_SALARY_FOR_CREDIT) < 0) {
            throw new RecordNotFoundException(
                    ErrorCode.INSUFFICIENT_SALARY,
                    "Kredit kartı üçün minimum maaş 1000 AZN olmalıdır."
            );
        }

        if (cardRepository.existsByClientCifAndCardType(clientCif, CardType.CREDIT)) {
            throw new RecordNotFoundException(
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
        } while (cardRepository.existsByCardNumber(cardNumber));
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