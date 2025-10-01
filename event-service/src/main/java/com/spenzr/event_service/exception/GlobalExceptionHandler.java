package com.spenzr.event_service.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<Map<String, String>> handleFeignNotFoundException(FeignException.NotFound ex) {
        // You can parse the error message from the Feign exception body if you want,
        // but a generic message is often sufficient.
        return new ResponseEntity<>(
                Map.of("error", "The requested resource could not be found. The user may not exist."),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleGenericRuntimeException(RuntimeException ex) {
        return new ResponseEntity<>(
                Map.of("error", ex.getMessage()),
                HttpStatus.BAD_REQUEST // Or another appropriate status
        );
    }
}
