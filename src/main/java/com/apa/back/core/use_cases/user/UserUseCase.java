package com.apa.back.core.use_cases.user;

import com.apa.back.core.domain.entities.User;
import com.apa.back.core.domain.repositories.UserRepository;
import com.apa.back.presentation.dtos.AuthDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;

@Service
public class UserUseCase {

    private final UserRepository userRepository;

    public UserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void register(@Valid @RequestBody AuthDto authDto) {
        User user = new User();

        user.setNome(authDto.nome());
        user.setEmail(authDto.email());
        user.setSenha(authDto.senha());
        user.setDataNascimento(LocalDate.parse("1998-12-21"));
        userRepository.save(user);
    }


}