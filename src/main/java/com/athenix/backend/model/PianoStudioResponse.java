package com.athenix.backend.model;

public class PianoStudioResponse {

    private boolean success;
    private Data data;
    private String message;

    public static class Data {
        private double oreTotali;
        private long giorniDisponibili;
        private double oreGiornaliere;
        private String oreGiornaliereFormattate;

        public Data(double oreTotali, long giorniDisponibili, double oreGiornaliere) {
            this.oreTotali = oreTotali;
            this.giorniDisponibili = giorniDisponibili;
            this.oreGiornaliere = oreGiornaliere;
            this.oreGiornaliereFormattate = formatta(oreGiornaliere);
        }

        // Converte 2.78 in "2h 47min"
        private String formatta(double ore) {
            int oreIntere = (int) ore;
            int minuti = (int) Math.round((ore - oreIntere) * 60);
            if (oreIntere == 0) return minuti + "min";
            if (minuti == 0) return oreIntere + "h";
            return oreIntere + "h " + minuti + "min";
        }

        public double getOreTotali() { return oreTotali; }
        public long getGiorniDisponibili() { return giorniDisponibili; }
        public double getOreGiornaliere() { return oreGiornaliere; }
        public String getOreGiornaliereFormattate() { return oreGiornaliereFormattate; }
    }

    public PianoStudioResponse(boolean success, Data data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public boolean isSuccess() { return success; }
    public Data getData() { return data; }
    public String getMessage() { return message; }
}