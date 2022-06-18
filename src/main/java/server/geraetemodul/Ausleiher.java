package server.geraetemodul;

import shared.communication.IAusleiher;

import java.time.LocalDateTime;

/**
 * @author Raphael Kleebaum
 * @author Jonny Schlutter
 */

public class Ausleiher implements IAusleiher {
    private LocalDateTime reservierdatum;
    private LocalDateTime fristBeginn;
    private boolean abgegeben;
    private String mitgliedsID;

    public Ausleiher(String mitgliedsID) {
        this.reservierdatum = LocalDateTime.now();
        this.mitgliedsID = mitgliedsID;
        abgegeben = false;
    }

    public LocalDateTime getFristBeginn() {
        return fristBeginn;
    }

    public void setFristBeginn(LocalDateTime fristBeginn) {
        this.fristBeginn = fristBeginn;
    }

    public String getMitgliedsID() {
        return mitgliedsID;
    }

    public LocalDateTime getReservierdatum() {
        return reservierdatum;
    }

    public void setReservierdatum(LocalDateTime reservierdatum) {
        this.reservierdatum = reservierdatum;
    }

    public boolean isAbgegeben() {
        return abgegeben;
    }

    public void setAbgegeben(boolean abgegeben) {
        this.abgegeben = abgegeben;
    }
}
