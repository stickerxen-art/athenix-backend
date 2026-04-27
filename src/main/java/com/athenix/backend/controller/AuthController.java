package com.athenix.backend.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.athenix.backend.model.Utente;
import com.athenix.backend.service.UtenteService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UtenteService utenteService;

    public AuthController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @PostMapping("/registra")
    public ResponseEntity<Map<String, Object>> registra(@RequestBody Map<String, String> body) {
        try {
            Utente utente = utenteService.registra(
                body.get("nome"), body.get("email"), body.get("password")
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(rispostaSuccesso(utente));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(rispostaErrore(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        return utenteService.login(body.get("email"), body.get("password"))
            .map(utente -> ResponseEntity.ok(rispostaSuccesso(utente)))
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(rispostaErrore("Email o password non corretti")));
    }

    @PostMapping("/studia")
    public ResponseEntity<Map<String, Object>> studia(@RequestBody Map<String, Object> body) {
        try {
            Long id = Long.valueOf(body.get("utenteId").toString());
            int xp = body.containsKey("xp") ? Integer.parseInt(body.get("xp").toString()) : 50;
            Utente utente = utenteService.aggiungiXp(id, xp);
            return ResponseEntity.ok(rispostaSuccesso(utente));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(rispostaErrore(e.getMessage()));
        }
    }

    @PostMapping("/studia/pomodoro")
    public ResponseEntity<Map<String, Object>> studiaPomodoro(@RequestBody Map<String, Object> body) {
        try {
            Long id = Long.valueOf(body.get("utenteId").toString());
            int xp = body.containsKey("xp") ? Integer.parseInt(body.get("xp").toString()) : 1;
            Utente utente = utenteService.aggiungiXpPomodoro(id, xp);
            return ResponseEntity.ok(rispostaSuccesso(utente));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(rispostaErrore(e.getMessage()));
        }
    }

    @PostMapping("/reset-streak/{id}")
    public ResponseEntity<Map<String, Object>> resetStreak(@PathVariable Long id) {
        try {
            Utente utente = utenteService.resetStreak(id);
            return ResponseEntity.ok(rispostaSuccesso(utente));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(rispostaErrore(e.getMessage()));
        }
    }

    // Include ultimoStudio nella risposta — fondamentale per il cooldown nel frontend
    private Map<String, Object> rispostaSuccesso(Utente utente) {
        Map<String, Object> risposta = new HashMap<>();
        risposta.put("success", true);
        risposta.put("id", utente.getId());
        risposta.put("nome", utente.getNome());
        risposta.put("email", utente.getEmail());
        risposta.put("xp", utente.getXp());
        risposta.put("livello", utente.getLivello());
        risposta.put("streak", utente.getStreak());
        risposta.put("titolo", utenteService.getTitolo(utente.getLivello()));
        risposta.put("ultimoStudio", utente.getUltimoStudio());
        risposta.put("giorniTotali", utente.getGiorniTotali());
        return risposta;
    }

    private Map<String, Object> rispostaErrore(String messaggio) {
        Map<String, Object> risposta = new HashMap<>();
        risposta.put("success", false);
        risposta.put("message", messaggio);
        return risposta;
    }
}