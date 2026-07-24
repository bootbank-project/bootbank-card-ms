package com.bootbank.card.exception.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    INVALID_INPUT("Bad Request", HttpStatus.BAD_REQUEST),
    DUPLICATE_CARD_NUMBER("Bad Request", HttpStatus.BAD_REQUEST),
    DUPLICATE_CLIENT_CIF("Bad Request", HttpStatus.BAD_REQUEST),
    INVALID_CARD_TYPE("Bad Request", HttpStatus.BAD_REQUEST),
    INVALID_EXPIRY_DATE("Bad Request", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_SALARY("Bad Request", HttpStatus.BAD_REQUEST),
    ACTIVE_CREDIT_CARD_EXISTS("Bad Request", HttpStatus.BAD_REQUEST),
    CREDIT_CARD_CURRENCY_MUST_BE_AZN("Bad Request", HttpStatus.BAD_REQUEST),

    CARD_NOT_FOUND("Not Found", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND("Not Found", HttpStatus.NOT_FOUND),
    CARD_PRODUCT_NOT_FOUND("Not Found", HttpStatus.NOT_FOUND),

    RESOURCE_ALREADY_EXISTS("Conflict", HttpStatus.CONFLICT),

    INTERNAL_ERROR("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String error;
    private final HttpStatus httpStatus;

    ErrorCode(String error, HttpStatus httpStatus) {
        this.error = error;
        this.httpStatus = httpStatus;
    }

    public String getError() {
        return error;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public int getStatus() {
        return httpStatus.value();
    }
}
