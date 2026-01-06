package com.apa.back.core.use_cases.usuario;

import com.apa.back.core.domain.entities.User;
import com.apa.back.core.domain.enums.UserRole;
import com.apa.back.core.domain.enums.UserStatus;
import com.apa.back.core.domain.repositories.UserRepository;
import com.apa.back.presentation.v1.dtos.user.UsuarioDto;
import com.apa.back.presentation.v1.dtos.user.UsuarioRoleDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<UsuarioDto> getAllPending() {
        return userRepository.findAllByUserStatus(UserStatus.PENDING)
                .stream()
                .map(user -> new UsuarioDto(
                        user.getNome(),
                        user.getEmail(),
                        user.getDataNascimento().toString()
                ))
                .toList();
    }

    public void approveUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        user.setUserStatus(UserStatus.APPROVED);
        userRepository.save(user);
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
