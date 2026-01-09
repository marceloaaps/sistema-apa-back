package com.apa.back.core.use_cases.auth;

import com.apa.back.core.domain.entities.PasswordResetToken;
import com.apa.back.core.domain.entities.User;
import com.apa.back.core.domain.repositories.PasswordResetTokenRepository;
import com.apa.back.core.domain.repositories.UserRepository;
import com.apa.back.core.exceptions.DomainUsedTokenException;
import com.apa.back.infra.utils.EmailUseCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class PasswordResetUseCase {

    private static final Logger logger = LogManager.getLogger(PasswordResetUseCase.class);

    private final PasswordResetTokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final EmailUseCase emailUseCase;

    @Value("${hostname.url}")
    private String hostnameUrl;

    public PasswordResetUseCase(PasswordResetTokenRepository tokenRepository, UserRepository userRepository, EmailUseCase emailUseCase) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
        this.emailUseCase = emailUseCase;
    }

    public void sendResetToken(String email) {
        logger.info("Solicitação de redefinição de senha para email: {}", email);

        try {
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        logger.warn("Tentativa de redefinir senha para email não cadastrado: {}", email);
                        return new UsernameNotFoundException("Email não encontrado");
                    });

            String token = UUID.randomUUID().toString();

            PasswordResetToken resetToken = new PasswordResetToken();
            resetToken.setToken(token);
            resetToken.setUser(user);
            resetToken.setExpiresAt(Instant.now().plus(Duration.ofHours(1)));
            resetToken.setCreatedAt(Instant.now());

            tokenRepository.save(resetToken);
            logger.info("Token de redefinição gerado para usuário: {}", email);

            String link = hostnameUrl + "/redefinir-senha?token=" + token;

            emailUseCase.sendEmail(user.getEmail(), "Redefinição de senha",
                    "Clique no link para redefinir sua senha: " + link);

            logger.info("Email de redefinição enviado com sucesso para: {}", email);

        } catch (UsernameNotFoundException e) {
            logger.error("Erro ao enviar token: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao enviar token de redefinição para: {}", email, e);
            throw new RuntimeException("Erro ao processar solicitação de redefinição de senha", e);
        }
    }

    public void resetPassword(String token, String newPassword) {
        logger.info("Tentativa de redefinição de senha com token");

        try {
            PasswordResetToken resetToken = tokenRepository.findByToken(token)
                    .orElseThrow(() -> {
                        logger.warn("Tentativa de redefinir senha com token inválido");
                        return new IllegalArgumentException("Token inválido");
                    });

            if (resetToken.getExpiresAt().isBefore(Instant.now())) {
                logger.warn("Tentativa de uso de token expirado para usuário: {}", resetToken.getUser().getEmail());
                throw new IllegalArgumentException("Token expirado");
            }

            if (resetToken.isUsed()) {
                logger.warn("Tentativa de reutilizar token já usado para usuário: {}", resetToken.getUser().getEmail());
                throw new DomainUsedTokenException("Esse token ja foi utilizado");
            }

            User user = resetToken.getUser();
            var senhaNova = new BCryptPasswordEncoder().encode(newPassword);

            userRepository.updateSenhaByUserId(user.getId(), senhaNova);
            tokenRepository.updateUsedByResetId(resetToken.getId());

            logger.info("Senha redefinida com sucesso para usuário: {}", user.getEmail());

        } catch (IllegalArgumentException | DomainUsedTokenException e) {
            logger.error("Erro de validação ao redefinir senha: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao redefinir senha", e);
            throw new RuntimeException("Erro ao processar redefinição de senha", e);
        }
    }


}
