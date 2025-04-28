package com.apa.back.core.domain.repositories;

import com.apa.back.core.domain.entities.Animais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animais, Long> {

    List<Animais> findAllByDisponivelParaAdocaoTrue();

}
