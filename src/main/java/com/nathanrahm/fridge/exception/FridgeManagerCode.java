package com.nathanrahm.fridge.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum FridgeManagerCode {
    FRIDGE_EXISTS(HttpStatus.CONFLICT),
    ITEM_LIMIT_EXCEEDED(HttpStatus.BAD_REQUEST),
    FRIDGE_NAME_EXISTS(HttpStatus.CONFLICT),
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    FRIDGE_NOT_FOUND(HttpStatus.NOT_FOUND);

    private final HttpStatus status;
}
