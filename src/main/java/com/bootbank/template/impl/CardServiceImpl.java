package com.bootbank.template.service.impl;

import com.bootbank.template.dto.CardStatusRequest;
import com.bootbank.template.dto.CardStatusResponse;
import com.bootbank.template.service.CardService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class CardServiceImpl implements CardService {

    @Override
    public CardStatusResponse updateCardStatus(String cardNumber, CardStatusRequest request, String cif) {

        // Kart nömrəsinin maskalanması (Məsələn: 416973******4532)
        String maskedCardNumber = cardNumber.substring(0, 6) + "******" + cardNumber.substring(cardNumber.length() - 4);

        // Statusu gələn request-ə uyğun yeniləyib cavab qaytarırıq
        return new CardStatusResponse(
                maskedCardNumber,
                request.getStatus(),
                LocalDateTime.now()
        );
    }
}