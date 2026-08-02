package com.bootbank.card.exception.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    Instant timestamp;

    int status;

    String error;

    String message;

    public static ErrorResponse of(int status, String error, String message) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(status)
                .error(error)
                .message(message)
                .build();
    }
}
