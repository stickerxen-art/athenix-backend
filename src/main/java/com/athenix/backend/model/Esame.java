package com.athenix.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "esami")
public class Esame {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Il nome non può essere vuoto")
    private String nome;

    @NotNull(message = "La data è obbligatoria")
    private LocalDate data;

    @Min(value = 1, message = "I CFU devono essere almeno 1")
    private int cfu;

    // Ogni esame appartiene a un utente specifico
    private Long utenteId;

    public Esame() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public int getCfu() { return cfu; }
    public void setCfu(int cfu) { this.cfu = cfu; }

    public Long getUtenteId() { return utenteId; }
    public void setUtenteId(Long utenteId) { this.utenteId = utenteId; }
}