package com.athenix.backend.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.athenix.backend.service.AchievementService;

@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

    private final AchievementService achievementService;

    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    // GET /api/achievements?utenteId=1 — lista tutti gli achievement con stato
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAchievements(@RequestParam Long utenteId) {
        return ResponseEntity.ok(achievementService.getAchievements(utenteId));
    }

    // POST /api/achievements/controlla — controlla e sblocca achievement
    @PostMapping("/controlla")
    public ResponseEntity<Map<String, Object>> controlla(@RequestBody Map<String, Object> body) {
        Long utenteId = Long.valueOf(body.get("utenteId").toString());

        // Costruisce il contesto con tutti i dati necessari
        Map<String, Object> contesto = new HashMap<>();
        if (body.containsKey("pomoCount"))      contesto.put("pomoCount",      Integer.parseInt(body.get("pomoCount").toString()));
        if (body.containsKey("pomoTotali"))     contesto.put("pomoTotali",     Integer.parseInt(body.get("pomoTotali").toString()));
        if (body.containsKey("pomoConsecutivi")) contesto.put("pomoConsecutivi", Integer.parseInt(body.get("pomoConsecutivi").toString()));
        if (body.containsKey("numEsami"))       contesto.put("numEsami",       Integer.parseInt(body.get("numEsami").toString()));
        if (body.containsKey("ora"))            contesto.put("ora",            Integer.parseInt(body.get("ora").toString()));
        if (body.containsKey("giorniTotali"))   contesto.put("giorniTotali",   Integer.parseInt(body.get("giorniTotali").toString()));

        List<String> nuovi = achievementService.controllaEAggiorna(utenteId, contesto);

        // Recupera i dettagli degli achievement sbloccati
        List<Map<String, Object>> dettagli = new ArrayList<>();
        for (String id : nuovi) {
            String[] info = AchievementService.TUTTI_GLI_ACHIEVEMENT.get(id);
            if (info != null) {
                Map<String, Object> ach = new HashMap<>();
                ach.put("id", id);
                ach.put("emoji", info[0]);
                ach.put("nome", info[1]);
                ach.put("descrizione", info[2]);
                dettagli.add(ach);
            }
        }

        Map<String, Object> risposta = new HashMap<>();
        risposta.put("success", true);
        risposta.put("nuoviSbloccati", dettagli);
        return ResponseEntity.ok(risposta);
    }
}