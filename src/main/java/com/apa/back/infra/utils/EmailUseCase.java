package com.apa.back.infra.utils;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import java.util.Properties;

@Service
public class EmailUseCase {

    private static final Logger logger = LogManager.getLogger(EmailUseCase.class);

    public void sendEmail(String to, String subject, String body) {
        logger.info("Iniciando envio de email - Para: {}, Assunto: {}", to, subject);

        final String from = "no-reply@apa.com";

        Properties props = new Properties();
        props.put("mail.smtp.host", "localhost");
        props.put("mail.smtp.port", "1025");
        props.put("mail.smtp.auth", "false");
        props.put("mail.smtp.starttls.enable", "false");

        Session session = Session.getInstance(props);

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            logger.info("Email enviado com sucesso - Para: {}, Assunto: {}", to, subject);

        } catch (MessagingException e) {
            logger.error("Falha ao enviar email - Para: {}, Assunto: {}, Erro: {}",
                    to, subject, e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar e-mail: " + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("Erro inesperado ao enviar email - Para: {}", to, e);
            throw new RuntimeException("Erro inesperado ao enviar e-mail", e);
        }
    }
}


