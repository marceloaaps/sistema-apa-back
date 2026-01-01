package com.apa.back.core.domain.repositories.event;

import com.apa.back.core.domain.entities.Event.Event;
import com.apa.back.core.domain.entities.Event.EventAnimal;
import com.apa.back.core.domain.entities.Event.EventAnimalId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventAnimalRepository extends JpaRepository<EventAnimal, EventAnimalId> {

    List<EventAnimal> findByFeirinha(Event feirinha);

    void deleteByFeirinha_Id(Long feirinhaId);

}