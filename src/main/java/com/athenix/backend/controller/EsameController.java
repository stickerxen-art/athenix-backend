package com.athenix.backend.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.athenix.backend.model.Esame;
import com.athenix.backend.model.PianoStudioResponse;
import com.athenix.backend.service.EsameService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/esami")
public class EsameController {

    private final EsameService esameService;

    public EsameController(EsameService esameService) {
        this.esameService = esameService;
    }

    // POST /api/esami — crea un nuovo esame
    // Il body deve contenere anche utenteId
    @PostMapping
    public ResponseEntity<Esame> creaEsame(@Valid @RequestBody Esame esame) {
        Esame salvato = esameService.salvaEsame(esame);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvato);
    }

    // GET /api/esami?utenteId=1 — lista esami dell'utente
    @GetMapping
    public List<Esame> getTuttiGliEsami(@RequestParam Long utenteId) {
        return esameService.getTuttiGliEsami(utenteId);
    }

    // GET /api/esami/{id} — leggi un singolo esame
    @GetMapping("/{id}")
    public ResponseEntity<Esame> getEsameById(@PathVariable Long id) {
        return esameService.getEsameById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /api/esami/{id} — aggiorna un esame
    @PutMapping("/{id}")
    public ResponseEntity<Esame> aggiornaEsame(
            @PathVariable Long id,
            @Valid @RequestBody Esame datiNuovi) {
        return esameService.aggiornaEsame(id, datiNuovi)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE /api/esami/{id} — elimina un esame
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminaEsame(@PathVariable Long id) {
        if (esameService.eliminaEsame(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    // GET /api/esami/search?nome=analisi&utenteId=1 — cerca per nome
    @GetMapping("/search")
    public List<Esame> cercaPerNome(
            @RequestParam String nome,
            @RequestParam Long utenteId) {
        return esameService.cercaPerNome(nome, utenteId);
    }

    // GET /api/esami/{id}/piano — calcola piano di studio
    @GetMapping("/{id}/piano")
    public ResponseEntity<PianoStudioResponse> calcolaPiano(@PathVariable Long id) {
        return esameService.getEsameById(id)
                .map(esame -> {
                    try {
                        double oreGiornaliere = esameService.calcolaOreGiornaliere(
                            esame.getCfu(), esame.getData()
                        );
                        double oreTotali = esame.getCfu() * 25.0;
                        long giorniDisponibili = ChronoUnit.DAYS.between(
                            LocalDate.now(), esame.getData()
                        );
                        PianoStudioResponse risposta = new PianoStudioResponse(
                            true,
                            new PianoStudioResponse.Data(oreTotali, giorniDisponibili, oreGiornaliere),
                            "Piano calcolato con successo"
                        );
                        return ResponseEntity.ok(risposta);
                    } catch (IllegalArgumentException e) {
                        PianoStudioResponse errore = new PianoStudioResponse(
                            false, null, e.getMessage()
                        );
                        return ResponseEntity.badRequest().body(errore);
                    }
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new PianoStudioResponse(false, null, "Esame non trovato")
                ));
    }
}