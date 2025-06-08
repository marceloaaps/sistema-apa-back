package com.apa.back.core.use_cases.auth;

import com.apa.back.core.domain.entities.User;
import com.apa.back.core.domain.enums.UserRole;
import com.apa.back.core.domain.repositories.UserRepository;
import com.apa.back.infra.security.service.PasswordBcrypt;
import com.apa.back.infra.security.service.TokenCache;
import com.apa.back.presentation.dtos.AuthDto;
import com.apa.back.presentation.dtos.LoginRequestDto;
import com.apa.back.presentation.dtos.LoginResponseDto;
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
    private LoginResponseDto loginResponseDto;
    private final TokenCache tokenCache;
    private final PasswordBcrypt passwordEncoder;


    private final Long expirationTime = 3600L;

    public AuthUseCase(PasswordBcrypt passwordBcrypt, UserRepository userRepository, JwtEncoder jwtEncoder, TokenCache tokenCache, PasswordBcrypt passwordEncoder) {
        this.passwordBcrypt = passwordBcrypt;
        this.userRepository = userRepository;
        this.jwtEncoder = jwtEncoder;
        this.tokenCache = tokenCache;
        this.passwordEncoder = passwordEncoder;
    }



    public String registerAndGenerateToken(AuthDto authDto) {

        User usuario = new User();
        usuario.setNome(authDto.nome());
        usuario.setEmail(authDto.email());
        usuario.setDataNascimento(authDto.dataNascimento());
        usuario.setSenha(passwordBcrypt.hashPassword(authDto.senha()));
        usuario.setUserRole(UserRole.user);
        userRepository.save(usuario);

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("apa-api")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .subject(usuario.getId().toString())
                .claim("scope", usuario.getId().toString())
                .build();


        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        tokenCache.storeToken(usuario.getId().toString(), token);


        return token;
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        var now = Instant.now();

        var user = userRepository.findByEmail(loginRequestDto.email());

        if (user.isEmpty() || !passwordBcrypt.verifyPassword(loginRequestDto.senha(), user.get().getSenha())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Credencial inválida.");
        }

        var claims = JwtClaimsSet.builder()
                .issuer("apa-api")
                .subject(user.get().getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expirationTime))
                .claim("scope", user.get().getUserRole().toString())
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        tokenCache.storeToken(user.get().getId().toString(), token);

        return new LoginResponseDto(token, expirationTime);
    }


}