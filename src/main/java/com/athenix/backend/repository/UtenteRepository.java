package com.athenix.backend.repository;

import com.athenix.backend.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {

    // Cerca un utente per email — serve per il login
    Optional<Utente> findByEmail(String email);

    // Controlla se esiste già un utente con quell'email — serve per la registrazione
    boolean existsByEmail(String email);
}