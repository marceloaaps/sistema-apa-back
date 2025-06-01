package com.apa.back.infra.configurations;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    @Value("${cors.originPatterns:default}")
    private String corsOrigin = "";

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        var allowedOrigins = corsOrigin.split("http://localhost:3000, http://localhost:5173");
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("*")
                .allowCredentials(true);
        WebMvcConfigurer.super.addCorsMappings(registry);
    }
}
