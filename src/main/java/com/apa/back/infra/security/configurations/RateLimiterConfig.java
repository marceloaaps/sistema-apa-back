package com.apa.back.infra.security.configurations;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;

public class RateLimiterConfig {

    Bandwidth getLoginLimit() {
        int loginMaxRequests = 10;
        int loginTimeWindowSeconds = 60;
        return Bandwidth.classic(loginMaxRequests,
                Refill.intervally(loginMaxRequests, java.time.Duration.ofSeconds(loginTimeWindowSeconds)));

    }
}
