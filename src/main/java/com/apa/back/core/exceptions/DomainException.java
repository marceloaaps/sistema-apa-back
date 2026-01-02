package com.apa.back.core.exceptions;

/**
 * Exceção genérica para violações de regras de negócio do domínio.
 */
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}

