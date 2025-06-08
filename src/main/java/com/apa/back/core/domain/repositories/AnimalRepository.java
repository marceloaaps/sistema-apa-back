package com.apa.back.core.domain.repositories;

import com.apa.back.core.domain.entities.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    Page<Animal> findAllByDisponivelParaAdocaoTrue(Pageable pageable);


}
