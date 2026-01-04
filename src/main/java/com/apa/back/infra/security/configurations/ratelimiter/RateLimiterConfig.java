package com.apa.back.infra.security.configurations.ratelimiter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Refill;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {

    @Bean
    public static Bandwidth loginLimit() {
        int loginMaxRequests = 10;
        int loginTimeWindowSeconds = 60;
        return Bandwidth.classic(loginMaxRequests,
                Refill.intervally(loginMaxRequests, java.time.Duration.ofSeconds(loginTimeWindowSeconds)));

    }
}
