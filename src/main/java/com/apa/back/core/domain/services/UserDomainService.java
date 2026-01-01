package com.apa.back.core.domain.services;

import com.apa.back.core.domain.entities.User;
import com.apa.back.core.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserDomainService {

    private final UserRepository userRepository;

    public UserDomainService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(Long idResponsavel) {
        return userRepository.findById(idResponsavel)
                .orElseThrow(() -> new RuntimeException("Usuário responsável não encontrado"));
    }
}
