package com.bootbank.card;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.bootbank.card.dto.CardOrderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:bootbank_card_db;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect",
    "spring.jpa.properties.hibernate.default_schema=",
    "spring.liquibase.enabled=true"
})
class BootbankCardApplicationTests {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void contextLoads() {
    }

    @Test
    void testGetCardProducts() throws Exception {
        mockMvc.perform(get("/api/v1/cards/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.total").isNumber());
    }

    @Test
    void testCreateDebitCardOrder() throws Exception {
        CardOrderRequest request = CardOrderRequest.builder()
                .cardProductCode("DIGICARD")
                .currency("AZN")
                .salary(new BigDecimal("1500.00"))
                .build();

        mockMvc.perform(post("/api/v1/cards")
                        .header("X-Client-CIF", "1234567")
                        .header("X-Client-FirstName", "Ali")
                        .header("X-Client-LastName", "Aliyev")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cardProductCode").value("DIGICARD"))
                .andExpect(jsonPath("$.cardNumber").isNotEmpty())
                .andExpect(jsonPath("$.cardType").value("DEBIT"))
                .andExpect(jsonPath("$.currency").value("AZN"))
                .andExpect(jsonPath("$.balance").value(0.00))
                .andExpect(jsonPath("$.creditLimit").isEmpty())
                .andExpect(jsonPath("$.usedLimit").isEmpty());
    }

    @Test
    void testCreateCreditCardOrder_Success() throws Exception {
        CardOrderRequest request = CardOrderRequest.builder()
                .cardProductCode("GOLD_CREDIT")
                .currency("AZN")
                .salary(new BigDecimal("2000.00"))
                .build();

        // Using a unique client_cif to avoid credit card limit constraint check failure if mock data is already present.
        mockMvc.perform(post("/api/v1/cards")
                        .header("X-Client-CIF", "987654")
                        .header("X-Client-FirstName", "Veli")
                        .header("X-Client-LastName", "Veliyev")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.cardProductCode").value("GOLD_CREDIT"))
                .andExpect(jsonPath("$.cardType").value("CREDIT"))
                .andExpect(jsonPath("$.currency").value("AZN"))
                .andExpect(jsonPath("$.creditLimit").value(900.00))
                .andExpect(jsonPath("$.usedLimit").value(0.00));
    }

    @Test
    void testCreateCreditCardOrder_LowSalary() throws Exception {
        CardOrderRequest request = CardOrderRequest.builder()
                .cardProductCode("GOLD_CREDIT")
                .currency("AZN")
                .salary(new BigDecimal("800.00"))
                .build();

        mockMvc.perform(post("/api/v1/cards")
                        .header("X-Client-CIF", "111222")
                        .header("X-Client-FirstName", "Veli")
                        .header("X-Client-LastName", "Veliyev")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Minimum salary for credit card order is 1000 AZN"));
    }

    @Test
    void testGetTransactionHistory() throws Exception {
        // Using CIF "123456" which has mock transactions inserted in Liquibase changeset 6
        mockMvc.perform(get("/api/v1/cards/transactions")
                        .header("X-Client-CIF", "123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.total").value(3))
                .andExpect(jsonPath("$.items[0].title").value("Bravo Supermarket"))
                .andExpect(jsonPath("$.items[0].category").value("Supermarket"))
                .andExpect(jsonPath("$.items[1].title").value("Wolt - Papa Johns"))
                .andExpect(jsonPath("$.items[2].title").value("Baku Metro"));
    }

    @Test
    void testGetTransactionDetails_Success() throws Exception {
        // ID 15402 is inserted in Liquibase changeset 6, belongs to CIF 123456
        mockMvc.perform(get("/api/v1/cards/transactions/15402")
                        .header("X-Client-CIF", "123456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(15402))
                .andExpect(jsonPath("$.title").value("Bravo Supermarket"))
                .andExpect(jsonPath("$.category").value("Supermarket"))
                .andExpect(jsonPath("$.status").value("SUCCESS"));
    }

    @Test
    void testGetTransactionDetails_Forbidden() throws Exception {
        // ID 15402 belongs to CIF 123456. Requesting with CIF 999999 should return 403 Forbidden.
        mockMvc.perform(get("/api/v1/cards/transactions/15402")
                        .header("X-Client-CIF", "999999"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403))
                .andExpect(jsonPath("$.error").value("Forbidden"))
                .andExpect(jsonPath("$.message").value("You do not have access to view this transaction"));
    }

    @Test
    void testGetTransactionDetails_NotFound() throws Exception {
        mockMvc.perform(get("/api/v1/cards/transactions/999999")
                        .header("X-Client-CIF", "123456"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Transaction not found with ID: 999999"));
    }
}
