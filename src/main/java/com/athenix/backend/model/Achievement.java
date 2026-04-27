package com.athenix.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "achievements")
public class Achievement {

    @Id
    private String id;

    private Long utenteId;
    private boolean sbloccato = false;
    private String dataSblocco;

    public Achievement() {}

    public Achievement(String id, Long utenteId) {
        this.id = id;
        this.utenteId = utenteId;
        this.sbloccato = false;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Long getUtenteId() { return utenteId; }
    public void setUtenteId(Long utenteId) { this.utenteId = utenteId; }

    public boolean isSbloccato() { return sbloccato; }
    public void setSbloccato(boolean sbloccato) { this.sbloccato = sbloccato; }

    public String getDataSblocco() { return dataSblocco; }
    public void setDataSblocco(String dataSblocco) { this.dataSblocco = dataSblocco; }
}