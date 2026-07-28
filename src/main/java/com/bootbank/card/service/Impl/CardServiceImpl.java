package com.bootbank.card.service.Impl;

import com.bootbank.card.dto.request.CardOrderRequest;
import com.bootbank.card.dto.response.CardOrderResponse;
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
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private static final BigDecimal MIN_SALARY_FOR_CREDIT = new BigDecimal("1000");
    private static final BigDecimal CREDIT_LIMIT_RATIO = new BigDecimal("0.45");

    private final CardProductRepository cardProductRepository;
    private final CardRepository cardRepository;
    private final com.bootbank.card.repository.CardTransactionRepository cardTransactionRepository;
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

    @Override
    @Transactional(readOnly = true)
    public com.bootbank.card.dto.response.TransactionResponse getTransactionDetails(Long id, String clientCif) {
        if (clientCif == null || clientCif.trim().isEmpty()) {
            throw new RecordNotFoundException(ErrorCode.INVALID_INPUT, "X-Client-CIF header is required");
        }

        com.bootbank.card.model.entity.CardTransaction transaction = cardTransactionRepository.findByIdWithCard(id)
                .orElseThrow(() -> new RecordNotFoundException(ErrorCode.TRANSACTION_NOT_FOUND, "Transaction not found with ID: " + id));

        if (!transaction.getCard().getClientCif().equals(clientCif)) {
            throw new RecordNotFoundException(ErrorCode.FORBIDDEN, "You do not have access to view this transaction");
        }

        return com.bootbank.card.dto.response.TransactionResponse.builder()
                .id(transaction.getId())
                .cardNumber(maskCardNumber(transaction.getCard().getCardNumber()))
                .title(transaction.getTitle())
                .category(transaction.getCategory())
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
