package com.apa.back.core.use_cases.usuario;

import com.apa.back.core.domain.entities.User;
import com.apa.back.core.domain.enums.UserRole;
import com.apa.back.core.domain.enums.UserStatus;
import com.apa.back.core.domain.repositories.UserRepository;
import com.apa.back.infra.exceptions.UserApprovalErrorException;
import com.apa.back.presentation.v1.dtos.user.UsuarioDto;
import com.apa.back.presentation.v1.dtos.user.UsuarioRoleDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioUseCase {

    private static final Logger logger = LogManager.getLogger(UsuarioUseCase.class);

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
        logger.info("Iniciando aprovação de usuário ID: {}", id);

        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Usuário não encontrado para aprovação - ID: {}", id);
                        return new EntityNotFoundException("Usuário não encontrado");
                    });

            user.setUserStatus(UserStatus.APPROVED);
            userRepository.save(user);

            logger.info("Usuário aprovado com sucesso - ID: {}, Email: {}", id, user.getEmail());

        } catch (EntityNotFoundException e) {
            logger.error("Erro ao aprovar usuário: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao aprovar usuário ID: {}", id, e);
            throw new UserApprovalErrorException("Erro ao aprovar usuário");
        }
    }

    @Transactional
    public UsuarioDto updateUsuario(Long id, UsuarioRoleDto usuarioRoleDto) {
        logger.info("Iniciando atualização de role do usuário ID: {} para role: {}", id, usuarioRoleDto.role());

        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> {
                        logger.warn("Usuário não encontrado para atualização - ID: {}", id);
                        return new EntityNotFoundException("Usuário não encontrado");
                    });

            user.setUserRole(UserRole.fromValue(usuarioRoleDto.role()));
            userRepository.updateUserRoleById(user.getUserRole(), id);

            logger.info("Role de usuário atualizado com sucesso - ID: {}, Email: {}, Nova role: {}",
                    id, user.getEmail(), user.getUserRole());

            return new UsuarioDto(user.getNome(), user.getEmail(), user.getDataNascimento().toString());

        } catch (EntityNotFoundException e) {
            logger.error("Erro ao atualizar usuário: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Erro inesperado ao atualizar usuário ID: {}", id, e);
            throw new RuntimeException("Erro ao atualizar role do usuário", e);
        }
    }
}
