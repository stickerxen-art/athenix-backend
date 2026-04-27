package com.athenix.backend.repository;

import com.athenix.backend.model.Esame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EsameRepository extends JpaRepository<Esame, Long> {

    // Restituisce solo gli esami di quell'utente
    List<Esame> findByUtenteId(Long utenteId);

    // Cerca per nome solo tra gli esami di quell'utente
    List<Esame> findByNomeContainingIgnoreCaseAndUtenteId(String nome, Long utenteId);
}