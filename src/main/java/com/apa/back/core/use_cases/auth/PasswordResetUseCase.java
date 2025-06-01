package com.apa.back.core.use_cases.auth;

import com.apa.back.core.domain.entities.PasswordResetToken;
import com.apa.back.core.domain.entities.User;
import com.apa.back.core.domain.repositories.PasswordResetTokenRepository;
import com.apa.back.core.domain.repositories.UserRepository;
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
        resetToken.setExpirationDate(Instant.now().plus(Duration.ofHours(1)));

        tokenRepository.save(resetToken);

        String link = "https://seusite.com/reset-password?token=" + token;

        emailUseCase.sendEmail(user.getEmail(), "Redefinição de senha",
                "Clique no link para redefinir sua senha: " + link);
    }

    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        if (resetToken.getExpirationDate().isBefore(Instant.now())) {
            throw new IllegalArgumentException("Token expirado");
        }

        User user = resetToken.getUser();
        user.setSenha(new BCryptPasswordEncoder().encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(resetToken); // invalidar token
    }
}
