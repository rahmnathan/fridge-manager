package com.nathanrahm.fridge.exception;

import lombok.Getter;

@Getter
public class FridgeManagerException extends Exception {
    private final FridgeManagerCode code;

    public FridgeManagerException(FridgeManagerCode code, String message) {
        super(message);
        this.code = code;
    }

    public FridgeManagerException(FridgeManagerCode code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }
}
