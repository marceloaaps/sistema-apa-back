package com.apa.back.core.domain.repositories;

import com.apa.back.core.domain.entities.HistoricoSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoSaudeRepository extends JpaRepository<HistoricoSaude, Long> {

    List<HistoricoSaude> findByAnimalIdOrderByDataEventoDesc(Long animalId);

    List<HistoricoSaude> findByAnimalIdAndTipoEvento(Long animalId, String tipoEvento);
}

