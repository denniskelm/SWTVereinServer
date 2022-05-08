package server.geraetemodul;

import java.time.LocalDateTime;
import java.util.Date;

public class Ausleiher {
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

    public String getMitlgiedsID() {
        return mitgliedsID;
    }

}
