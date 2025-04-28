package com.apa.back.core.domain.repositories;

import com.apa.back.core.domain.entities.Animais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimaisRepository extends JpaRepository<Animais, Long> {

}
