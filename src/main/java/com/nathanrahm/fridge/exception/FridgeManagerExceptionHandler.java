package com.nathanrahm.fridge.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class FridgeManagerExceptionHandler extends ResponseEntityExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(FridgeManagerExceptionHandler.class);
    private final ObjectMapper MAPPER = new ObjectMapper();

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> handleException(Exception exception) throws JsonProcessingException {
        logger.error("Exception occurred while processing request.", exception);
        FridgeManagerResponse response = new FridgeManagerResponse(FridgeManagerCode.UNKNOWN_ERROR, getRootMessage(exception));
        return ResponseEntity.status(response.getCode().getStatus()).body(MAPPER.writeValueAsString(response));
    }

    @ExceptionHandler(value = FridgeManagerException.class)
    public ResponseEntity<String> handleException(FridgeManagerException exception) throws JsonProcessingException {
        logger.error("Exception occurred while processing request.", exception);
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
