package com.bootbank.template.dto.response;

import com.bootbank.template.model.enums.CardProductCode;

public record CardProductsResponse(
                                   CardProductCode cardProductCode,
                                   String name,
                                   String cardType
                                   ) {
}
