package com.apa.back.infra.security.configurations.ratelimiter;

import com.apa.back.core.domain.services.interfaces.AuthRateLimitService;
import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Service;

@Service
public class AuthRateLimitServiceImpl implements AuthRateLimitService {

    private final LoginRateLimiter loginRateLimiter;

    public AuthRateLimitServiceImpl(LoginRateLimiter loginRateLimiter) {
        this.loginRateLimiter = loginRateLimiter;
    }

    @Override
    public void validateAttempt(String key) {
        Bucket bucket = loginRateLimiter.resolveBucket(key);

        if (!bucket.tryConsume(1)) {
            throw new RuntimeException("Muitas tentativas de login. Tente novamente mais tarde.");
        }
    }



}
