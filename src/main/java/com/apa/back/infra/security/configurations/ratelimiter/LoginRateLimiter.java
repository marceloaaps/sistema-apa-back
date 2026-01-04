package com.apa.back.infra.security.configurations.ratelimiter;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class LoginRateLimiter {

    private final ProxyManager<byte[]> proxyManager;

    public LoginRateLimiter(ProxyManager<byte[]> proxyManager) {
        this.proxyManager = proxyManager;
    }

    public Bucket resolveBucket(String ip) {

        byte[] key = ("bucket4j:login:" + ip)
                .getBytes(StandardCharsets.UTF_8);

        BucketConfiguration configuration = BucketConfiguration.builder()
                .addLimit(RateLimiterConfig.loginLimit())
                .build();

        return proxyManager.builder()
                .build(key, configuration);
    }
}