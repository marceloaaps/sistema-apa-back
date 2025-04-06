package com.apa.back.core.use_cases.user;

import com.apa.back.core.domain.entities.User;
import com.apa.back.core.domain.repositories.UserRepository;
import com.apa.back.infra.security.configurations.PasswordBcrypt;
import com.apa.back.presentation.dtos.AuthDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Service
public class UserUseCase {

    private final PasswordBcrypt passwordBcrypt;
    private final UserRepository userRepository;

    public UserUseCase(PasswordBcrypt passwordBcrypt, UserRepository userRepository) {
        this.passwordBcrypt = passwordBcrypt;
        this.userRepository = userRepository;
    }


    public void register(@Valid @RequestBody AuthDto authDto) {
        User user = new User();

        if (userRepository.verifyEmail(authDto.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "E-mail já cadastrado.");
        }

        var password = passwordBcrypt.hashPassword(authDto.senha());

        user.setNome(authDto.nome());
        user.setEmail(authDto.email());
        user.setSenha(password);
        user.setDataNascimento(authDto.dataNascimento());
        userRepository.save(user);
    }


}