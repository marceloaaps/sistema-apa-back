package com.apa.back.core.use_cases.auth;

import com.apa.back.core.domain.entities.PasswordResetToken;
import com.apa.back.core.domain.entities.User;
import com.apa.back.core.domain.repositories.PasswordResetTokenRepository;
import com.apa.back.core.domain.repositories.UserRepository;
import com.apa.back.core.exceptions.DomainUsedTokenException;
import com.apa.back.infra.utils.EmailUseCase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class PasswordResetUseCase {

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
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email não encontrado"));

        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiresAt(Instant.now().plus(Duration.ofHours(1)));
        resetToken.setCreatedAt(Instant.now());

        tokenRepository.save(resetToken);

        String link = hostnameUrl + "/redefinir-senha?token=" + token;

        emailUseCase.sendEmail(user.getEmail(), "Redefinição de senha",
                "Clique no link para redefinir sua senha: " + link);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        if (resetToken.getExpiresAt().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Token expirado");
        }

        if (resetToken.isUsed()) {
            throw new DomainUsedTokenException("Esse token ja foi utilizado");
        }

        User user = resetToken.getUser();

        var senhaNova = new BCryptPasswordEncoder().encode(newPassword);


        userRepository.updateSenhaByUserId(user.getId(), senhaNova);

        tokenRepository.updateUsedByResetId(resetToken.getId());
    }


}
