package com.athenix.backend.service;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.athenix.backend.model.Utente;
import com.athenix.backend.repository.UtenteRepository;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    public Utente registra(String nome, String email, String password) {
        if (utenteRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email già registrata");
        }
        Utente utente = new Utente();
        utente.setNome(nome);
        utente.setEmail(email);
        utente.setPassword(passwordEncoder.encode(password));
        return utenteRepository.save(utente);
    }

    public Optional<Utente> login(String email, String password) {
        return utenteRepository.findByEmail(email).filter(utente ->
            passwordEncoder.matches(password, utente.getPassword())
        );
    }

    // Bottone "Ho studiato oggi" — con blocco giornaliero e streak
    public Utente aggiungiXp(Long id, int xpDaAggiungere) {
        Utente utente = utenteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        String oggi = java.time.LocalDate.now().toString();
        String ieri = java.time.LocalDate.now().minusDays(1).toString();

        // Blocco giornaliero
        if (oggi.equals(utente.getUltimoStudio())) {
            throw new IllegalArgumentException("Hai già studiato oggi");
        }

        int nuoviXp = utente.getXp() + xpDaAggiungere;
        utente.setXp(nuoviXp);
        utente.setLivello(calcolaLivello(nuoviXp));

        // Aggiorna streak
        if (ieri.equals(utente.getUltimoStudio())) {
            utente.setStreak(utente.getStreak() + 1);
        } else {
            utente.setStreak(1);
        }
        utente.setGiorniTotali(utente.getGiorniTotali() + 1);
        utente.setUltimoStudio(oggi);
        return utenteRepository.save(utente);
    }

    // Pomodoro — senza blocco giornaliero
    public Utente aggiungiXpPomodoro(Long id, int xpDaAggiungere) {
        Utente utente = utenteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));

        int nuoviXp = utente.getXp() + xpDaAggiungere;
        utente.setXp(nuoviXp);
        utente.setLivello(calcolaLivello(nuoviXp));
        return utenteRepository.save(utente);
    }

    public Utente resetStreak(Long id) {
        Utente utente = utenteRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        utente.setStreak(0);
        utente.setUltimoStudio("2000-01-01");
        return utenteRepository.save(utente);
    }

    private int calcolaLivello(int xp) {
        int livello = 1;
        int xpNecessari = 0;
        while (true) {
            xpNecessari += (int)(100 * Math.pow(livello, 1.5));
            if (xp < xpNecessari) break;
            livello++;
        }
        return livello;
    }

    public String getTitolo(int livello) {
        if (livello <= 3)  return "Matricola";
        if (livello <= 6)  return "Studente";
        if (livello <= 10) return "Applicato";
        if (livello <= 15) return "Determinato";
        if (livello <= 20) return "Studioso";
        if (livello <= 30) return "Esperto";
        if (livello <= 40) return "Ricercatore";
        if (livello <= 50) return "Accademico";
        if (livello <= 75) return "Ingegniere";
        return "Professore";
    }

    public Optional<Utente> trovaPerId(Long id) {
        return utenteRepository.findById(id);
    }
}