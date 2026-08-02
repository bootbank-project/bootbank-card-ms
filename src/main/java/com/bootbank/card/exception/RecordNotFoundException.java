package com.bootbank.card.exception;

import com.bootbank.card.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class RecordNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public RecordNotFoundException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
