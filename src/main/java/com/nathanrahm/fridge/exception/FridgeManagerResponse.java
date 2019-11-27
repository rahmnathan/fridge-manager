package com.nathanrahm.fridge.exception;

import lombok.AllArgsConstructor;
import lombok.Value;

@Value
@AllArgsConstructor
public class FridgeManagerResponse {
    private final FridgeManagerCode code;
    private final String message;
}
