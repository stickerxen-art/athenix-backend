package com.athenix.backend.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.athenix.backend.model.Esame;
import com.athenix.backend.repository.EsameRepository;

@Service
public class EsameService {

    private final EsameRepository esameRepository;

    public EsameService(EsameRepository esameRepository) {
        this.esameRepository = esameRepository;
    }

    public Esame salvaEsame(Esame esame) {
        return esameRepository.save(esame);
    }

    // Restituisce solo gli esami dell'utente loggato
    public List<Esame> getTuttiGliEsami(Long utenteId) {
        return esameRepository.findByUtenteId(utenteId);
    }

    public Optional<Esame> getEsameById(Long id) {
        return esameRepository.findById(id);
    }

    public Optional<Esame> aggiornaEsame(Long id, Esame datiNuovi) {
        return esameRepository.findById(id).map(esameEsistente -> {
            esameEsistente.setNome(datiNuovi.getNome());
            esameEsistente.setData(datiNuovi.getData());
            esameEsistente.setCfu(datiNuovi.getCfu());
            return esameRepository.save(esameEsistente);
        });
    }

    public boolean eliminaEsame(Long id) {
        if (esameRepository.existsById(id)) {
            esameRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Cerca per nome solo tra gli esami dell'utente
    public List<Esame> cercaPerNome(String nome, Long utenteId) {
        return esameRepository.findByNomeContainingIgnoreCaseAndUtenteId(nome, utenteId);
    }

    public double calcolaOreGiornaliere(int cfu, LocalDate dataEsame) {
        double oreTotali = cfu * 25.0;
        long giorniDisponibili = ChronoUnit.DAYS.between(LocalDate.now(), dataEsame);
        if (giorniDisponibili <= 0) {
            throw new IllegalArgumentException("La data dell'esame deve essere nel futuro");
        }
        double risultato = oreTotali / giorniDisponibili;
        return Math.round(risultato * 100.0) / 100.0;
    }
}