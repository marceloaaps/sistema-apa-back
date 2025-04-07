package com.apa.back.core.use_cases.auth;

import com.apa.back.core.domain.entities.User;
import com.apa.back.core.domain.repositories.UserRepository;
import com.apa.back.infra.security.service.PasswordBcrypt;
import com.apa.back.presentation.dtos.AuthDto;
import com.apa.back.presentation.dtos.LoginRequest;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
public class AuthUseCase {

    private final PasswordBcrypt passwordBcrypt;
    private final UserRepository userRepository;
    private final JwtEncoder jwtEncoder;

    private final Long expirationTime = 3600L;

    public AuthUseCase(PasswordBcrypt passwordBcrypt, UserRepository userRepository, JwtEncoder jwtEncoder) {
        this.passwordBcrypt = passwordBcrypt;
        this.userRepository = userRepository;
        this.jwtEncoder = jwtEncoder;
    }



    @Transactional
    public void register(AuthDto authDto) {
        if (userRepository.verifyEmail(authDto.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado.");
        }

        var password = passwordBcrypt.hashPassword(authDto.senha());

        User user = new User();
        user.setNome(authDto.nome());
        user.setEmail(authDto.email());
        user.setSenha(password);
        user.setDataNascimento(authDto.dataNascimento());

        userRepository.save(user);
    }

    public String login(LoginRequest loginRequest) {

        var now = Instant.now();

        var user = userRepository.findByEmail(loginRequest.email());

        if (user.isEmpty() || !passwordBcrypt.verifyPassword(loginRequest.senha(), user.get().getSenha())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Credencial inválida.");

        }

        var claims = JwtClaimsSet.builder().issuer("mybackend").subject(user
                .get()
                .getId()
                .toString())
                .issuedAt(now)
                .expiresAt(
                        now.plusSeconds(expirationTime))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }





}