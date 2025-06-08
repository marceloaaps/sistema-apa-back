package com.apa.back.infra.configurations;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Value("${cors.originPatterns:default}")
    private String corsOrigin = "";

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        String[] allowedOrigins = corsOrigin.split(",");

        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("*")
                .allowCredentials(true);
        WebMvcConfigurer.super.addCorsMappings(registry);
    }

}
