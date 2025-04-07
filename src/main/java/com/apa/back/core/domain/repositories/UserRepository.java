package com.apa.back.core.domain.repositories;

import com.apa.back.core.domain.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT COUNT(e) > 0 FROM User e WHERE e.email = :email")
    boolean verifyEmail(@Param("email") String email);

    Optional<User> findByEmail(String email);
}
