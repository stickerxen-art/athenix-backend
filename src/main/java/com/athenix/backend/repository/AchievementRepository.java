package com.athenix.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.athenix.backend.model.Achievement;

@Repository
public interface AchievementRepository extends JpaRepository<Achievement, String> {

    // Tutti gli achievement di un utente
    List<Achievement> findByUtenteId(Long utenteId);

    // Un achievement specifico di un utente
    Optional<Achievement> findByIdAndUtenteId(String id, Long utenteId);

    // Conta gli achievement sbloccati di un utente
    int countByUtenteIdAndSbloccatoTrue(Long utenteId);
}