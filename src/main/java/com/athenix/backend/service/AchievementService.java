package com.athenix.backend.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.athenix.backend.model.Achievement;
import com.athenix.backend.model.Utente;
import com.athenix.backend.repository.AchievementRepository;
import com.athenix.backend.repository.UtenteRepository;

@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UtenteRepository utenteRepository;

    public AchievementService(AchievementRepository achievementRepository,
                               UtenteRepository utenteRepository) {
        this.achievementRepository = achievementRepository;
        this.utenteRepository = utenteRepository;
    }

    // Definizione di tutti gli achievement disponibili
    public static final Map<String, String[]> TUTTI_GLI_ACHIEVEMENT = new LinkedHashMap<>();
    static {
        // Pomodoro
        TUTTI_GLI_ACHIEVEMENT.put("primo_pomodoro",     new String[]{"🍅", "Primo Pomodoro",      "Completa la tua prima sessione Pomodoro"});
        TUTTI_GLI_ACHIEVEMENT.put("in_fiamme",          new String[]{"🔥", "In Fiamme",            "Completa 3 pomodori in un solo giorno"});
        TUTTI_GLI_ACHIEVEMENT.put("maratoneta",         new String[]{"💪", "Maratoneta",            "Completa 8 pomodori in un solo giorno"});
        TUTTI_GLI_ACHIEVEMENT.put("re_del_pomodoro",    new String[]{"👑", "Re del Pomodoro",       "Completa 50 pomodori in totale"});
        TUTTI_GLI_ACHIEVEMENT.put("sessione_epica",     new String[]{"⚔️", "Sessione Epica",        "Completa 4 pomodori consecutivi"});
        // Streak
        TUTTI_GLI_ACHIEVEMENT.put("primo_passo",        new String[]{"👣", "Primo Passo",           "Prima sessione di studio completata"});
        TUTTI_GLI_ACHIEVEMENT.put("una_settimana",      new String[]{"📅", "Una Settimana",          "Studia per 7 giorni di fila"});
        TUTTI_GLI_ACHIEVEMENT.put("inarrestabile",      new String[]{"🚀", "Inarrestabile",          "Studia per 30 giorni di fila"});
        TUTTI_GLI_ACHIEVEMENT.put("leggenda",           new String[]{"🏆", "Leggenda",               "Studia per 100 giorni di fila"});
        TUTTI_GLI_ACHIEVEMENT.put("studente_del_mese",  new String[]{"🗓️", "Studente del Mese",     "Studia almeno 1 volta al giorno per 20 giorni"});
        // Livelli
        TUTTI_GLI_ACHIEVEMENT.put("benvenuto",          new String[]{"⭐", "Benvenuto",              "Raggiungi il livello 2"});
        TUTTI_GLI_ACHIEVEMENT.put("studente_serio",     new String[]{"🎓", "Studente Serio",         "Raggiungi il livello 5"});
        TUTTI_GLI_ACHIEVEMENT.put("esperto",            new String[]{"🧠", "Esperto",                "Raggiungi il livello 10"});
        TUTTI_GLI_ACHIEVEMENT.put("accademico",         new String[]{"🎖️", "Accademico",            "Raggiungi il livello 20"});
        // Esami
        TUTTI_GLI_ACHIEVEMENT.put("organizzato",        new String[]{"📚", "Organizzato",            "Aggiungi il tuo primo esame"});
        TUTTI_GLI_ACHIEVEMENT.put("piano_completo",     new String[]{"📖", "Piano Completo",         "Aggiungi 5 esami"});
        // XP
        TUTTI_GLI_ACHIEVEMENT.put("prima_scintilla",    new String[]{"⚡", "Prima Scintilla",        "Guadagna 100 XP"});
        TUTTI_GLI_ACHIEVEMENT.put("in_forma",           new String[]{"💎", "In Forma",               "Guadagna 1.000 XP"});
        TUTTI_GLI_ACHIEVEMENT.put("powerhouse",         new String[]{"🌟", "Powerhouse",             "Guadagna 5.000 XP"});
        // Orari speciali
        TUTTI_GLI_ACHIEVEMENT.put("mattiniero",         new String[]{"🌅", "Mattiniero",             "Studia prima delle 6:00 di mattina"});
        TUTTI_GLI_ACHIEVEMENT.put("notturno",           new String[]{"🌙", "Notturno",               "Studia dopo la mezzanotte"});
    }

    // Restituisce tutti gli achievement di un utente con stato sbloccato/bloccato
    public List<Map<String, Object>> getAchievements(Long utenteId) {
        List<Achievement> sbloccati = achievementRepository.findByUtenteId(utenteId);
        Map<String, Achievement> mappa = new HashMap<>();
        for (Achievement a : sbloccati) mappa.put(a.getId(), a);

        List<Map<String, Object>> risultato = new ArrayList<>();
        for (Map.Entry<String, String[]> entry : TUTTI_GLI_ACHIEVEMENT.entrySet()) {
            String id = entry.getKey();
            String[] info = entry.getValue();
            Map<String, Object> ach = new HashMap<>();
            ach.put("id", id);
            ach.put("emoji", info[0]);
            ach.put("nome", info[1]);
            ach.put("descrizione", info[2]);
            Achievement trovato = mappa.get(id);
            ach.put("sbloccato", trovato != null && trovato.isSbloccato());
            ach.put("dataSblocco", trovato != null ? trovato.getDataSblocco() : null);
            risultato.add(ach);
        }
        return risultato;
    }

    // Sblocca un achievement se non è già sbloccato
    public boolean sblocca(Long utenteId, String achievementId) {
        Optional<Achievement> esistente = achievementRepository.findByIdAndUtenteId(achievementId, utenteId);
        if (esistente.isPresent() && esistente.get().isSbloccato()) return false;

        Achievement ach = esistente.orElse(new Achievement(achievementId, utenteId));
        ach.setSbloccato(true);
        ach.setDataSblocco(LocalDate.now().toString());
        achievementRepository.save(ach);
        return true;
    }

    // Controlla e sblocca achievement in base ai dati aggiornati dell'utente
    public List<String> controllaEAggiorna(Long utenteId, Map<String, Object> contesto) {
        Utente utente = utenteRepository.findById(utenteId).orElse(null);
        if (utente == null) return new ArrayList<>();

        List<String> nuoviSbloccati = new ArrayList<>();

        // XP
        if (utente.getXp() >= 100)   if (sblocca(utenteId, "prima_scintilla")) nuoviSbloccati.add("prima_scintilla");
        if (utente.getXp() >= 1000)  if (sblocca(utenteId, "in_forma"))        nuoviSbloccati.add("in_forma");
        if (utente.getXp() >= 5000)  if (sblocca(utenteId, "powerhouse"))      nuoviSbloccati.add("powerhouse");

        // Livelli
        if (utente.getLivello() >= 2)  if (sblocca(utenteId, "benvenuto"))       nuoviSbloccati.add("benvenuto");
        if (utente.getLivello() >= 5)  if (sblocca(utenteId, "studente_serio"))  nuoviSbloccati.add("studente_serio");
        if (utente.getLivello() >= 10) if (sblocca(utenteId, "esperto"))         nuoviSbloccati.add("esperto");
        if (utente.getLivello() >= 20) if (sblocca(utenteId, "accademico"))      nuoviSbloccati.add("accademico");

        // Streak
        if (utente.getStreak() >= 1)   if (sblocca(utenteId, "primo_passo"))     nuoviSbloccati.add("primo_passo");
        if (utente.getStreak() >= 7)   if (sblocca(utenteId, "una_settimana"))   nuoviSbloccati.add("una_settimana");
        if (utente.getStreak() >= 30)  if (sblocca(utenteId, "inarrestabile"))   nuoviSbloccati.add("inarrestabile");
        if (utente.getStreak() >= 100) if (sblocca(utenteId, "leggenda"))        nuoviSbloccati.add("leggenda");

        // Pomodori dal contesto
        int pomoCount = contesto.containsKey("pomoCount") ? (int) contesto.get("pomoCount") : 0;
        int pomoTotali = contesto.containsKey("pomoTotali") ? (int) contesto.get("pomoTotali") : 0;
        int pomoConsecutivi = contesto.containsKey("pomoConsecutivi") ? (int) contesto.get("pomoConsecutivi") : 0;

        if (pomoCount >= 1)  if (sblocca(utenteId, "primo_pomodoro")) nuoviSbloccati.add("primo_pomodoro");
        if (pomoCount >= 3)  if (sblocca(utenteId, "in_fiamme"))      nuoviSbloccati.add("in_fiamme");
        if (pomoCount >= 8)  if (sblocca(utenteId, "maratoneta"))     nuoviSbloccati.add("maratoneta");
        if (pomoTotali >= 50) if (sblocca(utenteId, "re_del_pomodoro")) nuoviSbloccati.add("re_del_pomodoro");
        if (pomoConsecutivi >= 4) if (sblocca(utenteId, "sessione_epica")) nuoviSbloccati.add("sessione_epica");

        // Esami dal contesto
        int numEsami = contesto.containsKey("numEsami") ? (int) contesto.get("numEsami") : 0;
        if (numEsami >= 1) if (sblocca(utenteId, "organizzato"))    nuoviSbloccati.add("organizzato");
        if (numEsami >= 5) if (sblocca(utenteId, "piano_completo")) nuoviSbloccati.add("piano_completo");

        // Orari speciali dal contesto
        int ora = contesto.containsKey("ora") ? (int) contesto.get("ora") : -1;
        if (ora >= 0 && ora < 6)  if (sblocca(utenteId, "mattiniero")) nuoviSbloccati.add("mattiniero");
        if (ora >= 0 && ora == 0) if (sblocca(utenteId, "notturno"))   nuoviSbloccati.add("notturno");

        // Studente del mese — giorni totali studiati
        int giorniTotali = contesto.containsKey("giorniTotali") ? (int) contesto.get("giorniTotali") : 0;
        if (giorniTotali >= 20) if (sblocca(utenteId, "studente_del_mese")) nuoviSbloccati.add("studente_del_mese");

        return nuoviSbloccati;
    }
}