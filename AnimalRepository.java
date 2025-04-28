package com.apa.back.core.domain.repositories;

import com.apa.back.core.domain.entities.Animal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    @Query("SELECT new com.apa.back.presentation.dtos.AnimalDTO(a.id, a.nome, a.raca, a.idade, a.disponivelParaAdocao) FROM Animal a")
    List<AnimalDTO> findAllAnimalsForAdoption();

    @Query("SELECT new com.apa.back.presentation.dtos.AnimalDTO(a.id, a.nome, a.raca, a.idade, a.disponivelParaAdocao) " +
            "FROM Animal a WHERE a.id = :id")
    Optional<AnimalDTO> findAnimalById(Long id);
}
