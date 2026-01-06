package com.apa.back.infra.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
public class RateLimiterReachedException extends RuntimeException {
    public RateLimiterReachedException(String message) {
        super(message);
    }
}
