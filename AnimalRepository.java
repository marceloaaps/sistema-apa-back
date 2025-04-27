package com.apa.back.core.domain.repositories;

import com.apa.back.core.domain.entities.Animal;
import com.apa.back.presentation.dtos.AnimalDTO;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    @Query("SELECT new com.apa.back.presentation.dtos.AnimalDTO(a.id, a.nome, a.raca, a.idade, a.disponivelParaAdocao) FROM Animal a")
    List<AnimalDTO> findAllAnimalsForAdoption();
}
