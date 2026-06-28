package com.bootbank.card.service;

import com.bootbank.card.dto.CardOrderRequest;
import com.bootbank.card.dto.CardOrderResponse;
import com.bootbank.card.dto.CardProductListResponse;
import com.bootbank.card.dto.CardProductResponse;
import com.bootbank.card.dto.TransactionListResponse;
import com.bootbank.card.dto.TransactionResponse;
import com.bootbank.card.entity.CardProduct;
import com.bootbank.card.entity.CardTransaction;
import com.bootbank.card.entity.UserCard;
import com.bootbank.card.exception.BadRequestException;
import com.bootbank.card.exception.ForbiddenException;
import com.bootbank.card.exception.NotFoundException;
import com.bootbank.card.repository.CardProductRepository;
import com.bootbank.card.repository.CardTransactionRepository;
import com.bootbank.card.repository.UserCardRepository;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private static final List<String> BINS = List.of("5190", "4127", "5490", "5522");
    private final CardProductRepository cardProductRepository;
    private final UserCardRepository userCardRepository;
    private final CardTransactionRepository cardTransactionRepository;
    private final Random random = new Random();

    @Override
    @Transactional(readOnly = true)
    public CardProductListResponse getCardProducts() {
        log.info("Fetching all card products");
        List<CardProduct> products = cardProductRepository.findAll();
        List<CardProductResponse> items = products.stream()
                .map(product -> CardProductResponse.builder()
                        .code(product.getCode())
                        .name(product.getName())
                        .cardType(product.getCardType())
                        .build())
                .collect(Collectors.toList());

        return CardProductListResponse.builder()
                .items(items)
                .total(items.size())
                .build();
    }

    @Override
    @Transactional
    public CardOrderResponse createCardOrder(String clientCif, String clientFirstName, String clientLastName, CardOrderRequest request) {
        log.info("Creating card order for client CIF: {}, Product: {}", clientCif, request.getCardProductCode());

        validateHeaders(clientCif, clientFirstName, clientLastName);

        CardProduct product = cardProductRepository.findByCode(request.getCardProductCode())
                .orElseThrow(() -> new BadRequestException("Card product not found: " + request.getCardProductCode()));

        boolean isCredit = "CREDIT".equalsIgnoreCase(product.getCardType());
        BigDecimal salary = request.getSalary();
        BigDecimal balance = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal creditLimit = null;
        BigDecimal usedLimit = null;

        if (isCredit) {
            validateCreditCardOrder(clientCif, request);
            creditLimit = salary.multiply(new BigDecimal("0.45")).setScale(2, RoundingMode.HALF_UP);
            usedLimit = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        } else {
            // For DEBIT, salary is optional. Default to ZERO if null to satisfy DB NOT NULL constraint.
            if (salary == null) {
                salary = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
            }
        }

        String maskedCardNumber = generateUniqueMaskedCardNumber();
        String expiryDate = LocalDate.now().plusYears(3).format(DateTimeFormatter.ofPattern("MM/yy"));

        UserCard userCard = UserCard.builder()
                .clientCif(clientCif)
                .clientName(clientFirstName)
                .clientLastName(clientLastName)
                .cardProductCode(product.getCode())
                .cardNumber(maskedCardNumber)
                .expiryDate(expiryDate)
                .cardType(product.getCardType())
                .currency(request.getCurrency())
                .salary(salary)
                .balance(balance)
                .creditLimit(creditLimit)
                .usedLimit(usedLimit)
                .createdDate(LocalDateTime.now())
                .build();

        UserCard savedCard = userCardRepository.save(userCard);
        log.info("Successfully created card order with ID: {} and masked number: {}", savedCard.getId(), savedCard.getCardNumber());

        return CardOrderResponse.builder()
                .cardProductCode(savedCard.getCardProductCode())
                .cardNumber(savedCard.getCardNumber())
                .expiryDate(savedCard.getExpiryDate())
                .cardType(savedCard.getCardType())
                .currency(savedCard.getCurrency())
                .balance(savedCard.getBalance())
                .creditLimit(savedCard.getCreditLimit())
                .usedLimit(savedCard.getUsedLimit())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionListResponse getTransactionHistory(String clientCif) {
        log.info("Fetching transaction history for client CIF: {}", clientCif);
        if (clientCif == null || clientCif.trim().isEmpty()) {
            throw new BadRequestException("X-Client-CIF header is required");
        }

        List<CardTransaction> transactions = cardTransactionRepository.findAllByClientCifOrderByCreatedAtDesc(clientCif);
        List<TransactionResponse> items = transactions.stream()
                .map(t -> TransactionResponse.builder()
                        .id(t.getId())
                        .cardNumber(t.getCard().getCardNumber())
                        .title(t.getTitle())
                        .category(t.getCategory())
                        .amount(t.getAmount())
                        .status(t.getStatus())
                        .createdAt(t.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return TransactionListResponse.builder()
                .items(items)
                .total(items.size())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionDetails(Long id, String clientCif) {
        log.info("Fetching transaction details for ID: {}, client CIF: {}", id, clientCif);
        if (clientCif == null || clientCif.trim().isEmpty()) {
            throw new BadRequestException("X-Client-CIF header is required");
        }

        CardTransaction transaction = cardTransactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found with ID: " + id));

        if (!transaction.getCard().getClientCif().equals(clientCif)) {
            log.warn("Access denied for client CIF: {} to transaction ID: {}", clientCif, id);
            throw new ForbiddenException("You do not have access to view this transaction");
        }

        return TransactionResponse.builder()
                .id(transaction.getId())
                .cardNumber(transaction.getCard().getCardNumber())
                .title(transaction.getTitle())
                .category(transaction.getCategory())
                .amount(transaction.getAmount())
                .status(transaction.getStatus())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

    private void validateHeaders(String cif, String firstName, String lastName) {
        if (cif == null || cif.trim().isEmpty()) {
            throw new BadRequestException("X-Client-CIF header is required");
        }
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new BadRequestException("X-Client-FirstName header is required");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new BadRequestException("X-Client-LastName header is required");
        }
    }

    private void validateCreditCardOrder(String clientCif, CardOrderRequest request) {
        if (!"AZN".equalsIgnoreCase(request.getCurrency())) {
            throw new BadRequestException("Credit card order is only allowed in AZN currency");
        }
        if (request.getSalary() == null) {
            throw new BadRequestException("Salary is required for credit card order");
        }
        if (request.getSalary().compareTo(new BigDecimal("1000")) < 0) {
            throw new BadRequestException("Minimum salary for credit card order is 1000 AZN");
        }
        if (userCardRepository.existsByClientCifAndCardType(clientCif, "CREDIT")) {
            throw new BadRequestException("Customer already has an active credit card");
        }
    }

    private String generateUniqueMaskedCardNumber() {
        String maskedCardNumber = null;
        boolean isUnique = false;
        int attempts = 0;

        while (!isUnique && attempts < 100) {
            String bin = BINS.get(random.nextInt(BINS.size()));
            String last4 = String.format("%04d", random.nextInt(10000));
            maskedCardNumber = bin + " **** **** " + last4;

            if (!userCardRepository.existsByCardNumber(maskedCardNumber)) {
                isUnique = true;
            }
            attempts++;
        }

        if (!isUnique) {
            log.error("Failed to generate a unique card number after 100 attempts");
            throw new BadRequestException("Failed to generate a unique card number. Please try again.");
        }

        return maskedCardNumber;
    }
}
