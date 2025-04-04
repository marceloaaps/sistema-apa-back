package com.apa.back.core.use_cases.user;

import com.apa.back.core.domain.repositories.UserRepository;
import com.apa.back.presentation.dtos.AuthDto;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UserUseCase {

    private final UserRepository userRepository;

    public UserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Falta logica e implementação
    public void register(@Valid @RequestBody AuthDto authDto) {
        userRepository.insert(user);
    }


}
