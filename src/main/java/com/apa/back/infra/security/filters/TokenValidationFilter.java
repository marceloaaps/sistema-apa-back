package com.apa.back.infra.security.filters;

import com.apa.back.infra.security.service.TokenCache;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TokenValidationFilter extends OncePerRequestFilter {

    private final TokenCache tokenCache;

    private final JwtDecoder jwtDecoder;


    public TokenValidationFilter(TokenCache tokenCache, JwtDecoder jwtDecoder) {
        this.tokenCache = tokenCache;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (isSwaggerOrPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

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

    private boolean isSwaggerOrPublicPath(String path) {
        return path.startsWith("/v3/api-docs")
                || path.startsWith("/swagger-ui")
                || path.equals("/swagger-ui.html");
    }

    private String extractUsernameFromToken(String token) {
        return jwtDecoder.decode(token).getSubject();
    }
}
