package com.apa.back.core.domain.repositories;

import com.apa.back.core.domain.entities.EventWorker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventWorkerRepository extends JpaRepository<Long, EventWorker> {
}
