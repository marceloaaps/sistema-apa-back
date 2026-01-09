package com.apa.back.infra.exceptions;

import com.apa.back.core.exceptions.DomainConflictException;
import com.apa.back.core.exceptions.DomainNotFoundException;
import com.apa.back.core.exceptions.DomainUsedTokenException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(UsedTokenException.class)
    public ResponseEntity<String> handleUsedTokenException(UsedTokenException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex){
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(DomainNotFoundException.class)
    public ResponseEntity<String> handleDomainNotFoundException(DomainNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(DomainConflictException.class)
    public ResponseEntity<String> handleDomainConflictException(DomainConflictException ex) {
        return ResponseEntity.status(409).body(ex.getMessage());
    }

    @ExceptionHandler(DomainUsedTokenException.class)
    public ResponseEntity<String> handleDomainUsedTokenException(DomainUsedTokenException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity.status(ex.getStatusCode())
                .body(Map.of("error", ex.getReason()));
    }

    @ExceptionHandler(RateLimiterReachedException.class)
    public ResponseEntity<String> handleRateLimiterReachedException(RateLimiterReachedException ex) {
        return ResponseEntity.status(429).body(ex.getMessage());
    }

    @ExceptionHandler(UserApprovalErrorException.class)
    public ResponseEntity<String> handleUserApprovalErrorException(UserApprovalErrorException ex) {
        return ResponseEntity.status(500).body(ex.getMessage());
    }
}
