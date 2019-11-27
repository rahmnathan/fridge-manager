package com.nathanrahm.fridge.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FridgeManagerCode {
    FRIDGE_EXISTS(HttpStatus.BAD_REQUEST),
    SODA_CANS_EXCEEDED(HttpStatus.BAD_REQUEST),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    FRIDGE_NOT_FOUND(HttpStatus.NOT_FOUND);

    private final HttpStatus status;
}
