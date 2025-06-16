package com.apa.back.core.use_cases.config;

import com.apa.back.core.domain.entities.User;
import com.apa.back.core.domain.enums.UserRole;
import com.apa.back.core.domain.repositories.UserRepository;
import com.apa.back.infra.exceptions.ResourceNotFoundException;
import com.apa.back.presentation.v1.dtos.user.UsuarioDto;
import com.apa.back.presentation.v1.dtos.user.UsuarioRoleDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service
public class ConfigUseCase {

    private final UserRepository userRepository;

    public ConfigUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public UsuarioDto updateUsuario(Long id, UsuarioRoleDto usuarioRoleDto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
        user.setUserRole(UserRole.fromValue(usuarioRoleDto.role()));

        userRepository.updateUserRoleById(user.getUserRole(), id);


        return new UsuarioDto(user.getNome(), user.getEmail(), user.getDataNascimento().toString());
    }
}
