package com.apa.back.infra.security.configurations;

import com.apa.back.core.domain.enums.UserRole;
import com.apa.back.infra.security.filters.TokenValidationFilter;
import com.apa.back.infra.security.service.TokenCache;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import jakarta.servlet.DispatcherType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${jwt.private.key}")
    private RSAPrivateKey privateKey;

    @Value("${jwt.public.key}")
    private RSAPublicKey publicKey;

    private static final String ADMIN = "ADMIN";
    private static final String USER = "USER";
    private static final String GUEST = "GUEST";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, TokenValidationFilter tokenValidationFilter) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .addFilterBefore(tokenValidationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth

                        // Endpoints liberados para todes
                        .requestMatchers("/auth/v1/login", "/auth/v1/register", "/auth/v1/forgot-password", "/auth/v1/reset-password").permitAll()

                        // Endpoints mistos
                        .requestMatchers(HttpMethod.GET, "/animals/v1/**").hasAnyRole(ADMIN, USER)
                        .requestMatchers(HttpMethod.GET, "/usuarios/v1/**").hasAnyRole(ADMIN, USER)

                        // Ações restritas
                        .requestMatchers(HttpMethod.POST, "/animals/v1/create").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.PUT, "/animals/v1/**").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.DELETE, "/animals/v1/**/delete").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.PATCH, "/animals/v1/**/restore").hasRole(ADMIN)

                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                );
        return http.build();
    }


    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        return new CustomJwtAuthenticationConverter();
    }

    @Bean
    public TokenValidationFilter tokenValidationFilter(TokenCache tokenCache, JwtDecoder jwtDecoder) {
        return new TokenValidationFilter(tokenCache, jwtDecoder);
    }

    @Bean
    public JwtEncoder jwtEncoder(){
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }
}
