package com.apa.back.core.domain.repositories;

import com.apa.back.core.domain.entities.Event;
import com.apa.back.core.domain.entities.EventAnimal;
import com.apa.back.core.domain.entities.EventAnimalId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventAnimalRepository extends JpaRepository<EventAnimal, EventAnimalId> {

    List<EventAnimal> findByFeirinha(Event feirinha);

    void deleteByFeirinha_Id(Long feirinhaId);

}