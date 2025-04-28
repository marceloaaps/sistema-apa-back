package com.apa.back.core.domain.repositories;

import com.apa.back.core.domain.entities.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    List<Animal> findAllByDisponivelParaAdocaoTrue();

}
