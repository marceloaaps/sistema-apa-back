package com.apa.back.infra.security.configurations;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import org.springframework.core.convert.converter.Converter;
import java.util.Collection;
import java.util.Collections;

public class CustomJwtAuthenticationConverter extends JwtAuthenticationConverter {

    public CustomJwtAuthenticationConverter() {
        this.setJwtGrantedAuthoritiesConverter(new CustomGrantedAuthoritiesConverter());
    }

    static class CustomGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
        @Override
        public Collection<GrantedAuthority> convert(Jwt jwt) {
            String scope = jwt.getClaimAsString("scope");

            if (scope == null) {
                return Collections.emptyList();
            }
            return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + scope.toUpperCase()));
        }
    }
}
