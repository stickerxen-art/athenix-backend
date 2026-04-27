package com.athenix.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "utenti")
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Il nome non può essere vuoto")
    private String nome;

    @NotBlank(message = "L'email non può essere vuota")
    @Email(message = "Email non valida")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "La password non può essere vuota")
    private String password;

    private int xp = 0;
    private int livello = 1;
    private int streak = 0;
    private String ultimoStudio;
    private int giorniTotali = 0;

    public Utente() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }

    public int getLivello() { return livello; }
    public void setLivello(int livello) { this.livello = livello; }

    public int getStreak() { return streak; }
    public void setStreak(int streak) { this.streak = streak; }

    public String getUltimoStudio() { return ultimoStudio; }
    public void setUltimoStudio(String ultimoStudio) { this.ultimoStudio = ultimoStudio; }

    public int getGiorniTotali() { return giorniTotali; }
    public void setGiorniTotali(int giorniTotali) { this.giorniTotali = giorniTotali; }
}