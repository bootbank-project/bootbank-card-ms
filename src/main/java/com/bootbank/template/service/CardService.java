package com.bootbank.template.service;

import com.bootbank.template.dto.CardStatusRequest;
import com.bootbank.template.dto.CardStatusResponse;

public interface CardService {

    // Müştəri yoxlanışı üçün cif parametrini də bura əlavə etdik
    CardStatusResponse updateCardStatus(String cardNumber, CardStatusRequest request, String cif);
}