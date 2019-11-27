package com.nathanrahm.fridge.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    private final ObjectMapper MAPPER = new ObjectMapper();

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception exception) throws JsonProcessingException {
        FridgeManagerResponse response = new FridgeManagerResponse(FridgeManagerCode.UNKNOWN_ERROR, "Unknown error occurred. " + getRootMessage(exception));
        return ResponseEntity.status(FridgeManagerCode.UNKNOWN_ERROR.getStatus()).body(MAPPER.writeValueAsString(response));
    }

    @ExceptionHandler(value = FridgeManagerException.class)
    public ResponseEntity<String> handleException(FridgeManagerException exception) throws JsonProcessingException {
        FridgeManagerResponse response = new FridgeManagerResponse(exception.getCode(), getRootMessage(exception));
        return ResponseEntity.status(response.getCode().getStatus()).body(MAPPER.writeValueAsString(response));
    }

    private String getRootMessage(Exception e) {
        Throwable rootException = e;
        while(rootException.getCause() != null) {
            rootException = e.getCause();
        }

        return rootException.getMessage();
    }
}
