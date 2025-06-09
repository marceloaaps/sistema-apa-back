package com.apa.back.core.domain.repositories;

import com.apa.back.core.domain.entities.Event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Event getEventById(Long id);
}