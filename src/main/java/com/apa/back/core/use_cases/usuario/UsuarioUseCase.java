package com.apa.back.core.use_cases.usuario;

import com.apa.back.core.domain.entities.User;
import com.apa.back.core.domain.repositories.UserRepository;
import com.apa.back.presentation.v1.dtos.user.UsuarioDto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioUseCase {

    private final UserRepository userRepository;

    public UsuarioUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UsuarioDto> getUsuario(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        return userOptional.map(user -> new UsuarioDto(
                user.getNome(),
                user.getEmail(),
                user.getDataNascimento().toString()
        ));
    }
}
