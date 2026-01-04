package com.apa.back.core.domain.services.interfaces;

public interface AuthRateLimitService {
    void validateAttempt(String ip);
}
