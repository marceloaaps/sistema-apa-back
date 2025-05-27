package com.apa.back.infra.security.filters;

import com.apa.back.infra.security.service.TokenCache;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenValidationFilter extends OncePerRequestFilter {

    private final TokenCache tokenCache;

    public TokenValidationFilter(TokenCache tokenCache) {
        this.tokenCache = tokenCache;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String username = extractUsernameFromToken(token);

            if (!tokenCache.isTokenValid(username, token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private String extractUsernameFromToken(String token) {
        // Pode usar JwtDecoder aqui
        return ""; // lógica aqui
    }
}
