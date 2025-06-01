package com.apa.back.core.use_cases.auth;

import org.springframework.stereotype.Service;

@Service
public class EmailUseCase {
    public void sendEmail(String to, String subject, String body) {
        // Integração com SMTP real ou apenas log para debug
        System.out.printf("Enviar email para: %s\nAssunto: %s\nCorpo: %s\n", to, subject, body);
    }
}
