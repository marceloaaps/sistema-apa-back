package com.apa.back.core.domain.repositories;

import com.apa.back.core.domain.entities.Vacinacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VacinacaoRepository extends JpaRepository<Vacinacao, Long> {

    List<Vacinacao> findByAnimalIdOrderByDataAplicacaoDesc(Long animalId);

    List<Vacinacao> findByAnimalIdAndValidadeAfter(Long animalId, LocalDate data);

    List<Vacinacao> findByValidadeBefore(LocalDate data);
}

