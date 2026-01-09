package com.apa.back.core.use_cases.auth;

import com.apa.back.core.domain.entities.User;
import com.apa.back.core.domain.enums.UserRole;
import com.apa.back.core.domain.repositories.UserRepository;
import com.apa.back.core.exceptions.DomainConflictException;
import com.apa.back.infra.security.service.PasswordBcrypt;
import com.apa.back.infra.security.service.TokenCache;
import com.apa.back.presentation.v1.dtos.auth.RegisterDto;
import com.apa.back.presentation.v1.dtos.auth.login.LoginRequestDto;
import com.apa.back.presentation.v1.dtos.auth.login.LoginResponseDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthUseCase {

    private static final Logger logger = LogManager.getLogger(AuthUseCase.class);

    private final PasswordBcrypt passwordBcrypt;
    private final UserRepository userRepository;
    private final JwtEncoder jwtEncoder;
    private final TokenCache tokenCache;


    private static final Long EXPIRATION_TIME = 3600L;

    public AuthUseCase(PasswordBcrypt passwordBcrypt, UserRepository userRepository, JwtEncoder jwtEncoder, TokenCache tokenCache) {
        this.passwordBcrypt = passwordBcrypt;
        this.userRepository = userRepository;
        this.jwtEncoder = jwtEncoder;
        this.tokenCache = tokenCache;
    }



    public LoginResponseDto registerAndGenerateToken(RegisterDto registerDto) {
        logger.info("Iniciando registro de novo usuário com email: {}", registerDto.email());

        try {
            if (registerDto.senha().length() > 72) {
                logger.warn("Tentativa de registro com senha muito longa para email: {}", registerDto.email());
                throw new DomainConflictException("A senha não pode ter mais de 72 caracteres.");
            }

            User usuario = new User();
            usuario.setNome(registerDto.nome());
            usuario.setEmail(registerDto.email());
            usuario.setDataNascimento(registerDto.dataNascimento());
            usuario.setSenha(passwordBcrypt.hashPassword(registerDto.senha()));
            usuario.setUserRole(UserRole.USER);

            if (userRepository.findByEmail(usuario.getEmail()).isPresent()) {
                logger.warn("Tentativa de registro com email já existente: {}", registerDto.email());
                throw new DomainConflictException("Esse email já existe.");
            }

            userRepository.save(usuario);
            logger.info("Usuário registrado com sucesso: {}", usuario.getEmail());

            Instant now = Instant.now();
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("apa-api")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(EXPIRATION_TIME))
                    .subject(usuario.getId().toString())
                    .claim("scope", usuario.getId().toString())
                    .build();

            String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
            tokenCache.storeToken(usuario.getId().toString(), token);

            logger.info("Token gerado com sucesso para usuário: {}", usuario.getEmail());
            return new LoginResponseDto(token, EXPIRATION_TIME);

        } catch (DomainConflictException e) {
            logger.error("Erro de validação no registro: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao registrar usuário: {}", registerDto.email(), e);
            throw new RuntimeException("Erro ao processar registro do usuário", e);
        }
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        logger.info("Tentativa de login para email: {}", loginRequestDto.email());

        try {
            var now = Instant.now();
            var user = userRepository.findByEmail(loginRequestDto.email());

            if (user.isEmpty() || !passwordBcrypt.verifyPassword(loginRequestDto.senha(), user.get().getSenha())){
                logger.warn("Falha na autenticação para email: {}", loginRequestDto.email());
                throw new DomainConflictException("Credencial inválida.");
            }

            var claims = JwtClaimsSet.builder()
                    .issuer("apa-api")
                    .subject(user.get().getId().toString())
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(EXPIRATION_TIME))
                    .claim("scope", user.get().getUserRole().toString())
                    .claim("email", user.get().getEmail())
                    .claim("name", user.get().getNome())
                    .build();

            String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
            tokenCache.storeToken(user.get().getId().toString(), token);

            logger.info("Login realizado com sucesso para usuário: {}", user.get().getEmail());
            return new LoginResponseDto(token, EXPIRATION_TIME);

        } catch (DomainConflictException e) {
            logger.error("Erro de autenticação: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado durante login para email: {}", loginRequestDto.email(), e);
            throw new RuntimeException("Erro ao processar login", e);
        }
    }


}