package com.apa.back.core.domain.repositories.event;

import com.apa.back.core.domain.entities.Event.Event;
import com.apa.back.core.domain.entities.Event.EventWorker;
import com.apa.back.core.domain.entities.Event.EventWorkerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventWorkerRepository extends JpaRepository<EventWorker, EventWorkerId> {
    List<EventWorker> findByFeirinha(Event event);

    void deleteByFeirinha_Id(Long id);
}
