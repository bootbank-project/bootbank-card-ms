package com.bootbank.template.exception;

import com.bootbank.template.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class RecordNotFoundException extends RuntimeException {

    private final ErrorCode errorCode;

    public RecordNotFoundException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
