package com.apa.back.infra.exceptions;

import lombok.experimental.StandardException;

@StandardException
public class UsedTokenException extends RuntimeException {
    public UsedTokenException(String message) {
        super(message);
    }
}
