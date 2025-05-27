package com.apa.back.infra.security.service;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TokenCache {
    private final Map<String, String> tokenStore = new ConcurrentHashMap<>();

    public void storeToken(String userId, String token) {
        tokenStore.put(userId, token);
    }

    public String getToken(String userId) {
        return tokenStore.get(userId);
    }

    public void invalidateToken(String userId) {
        tokenStore.remove(userId);
    }

    public boolean isTokenValid(String userId, String token) {
        return token.equals(tokenStore.get(userId));
    }
}
