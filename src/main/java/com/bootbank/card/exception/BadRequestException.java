package com.bootbank.card.exception;

import com.bootbank.card.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException {

    private final ErrorCode errorCode;

    public BadRequestException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
