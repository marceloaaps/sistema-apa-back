package com.apa.back.core.domain.repositories;

import com.apa.back.core.domain.entities.EventAnimal;
import com.apa.back.core.domain.entities.EventAnimalId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventAnimalRepository extends JpaRepository<EventAnimal, EventAnimalId> {
}