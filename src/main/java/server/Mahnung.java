package server;

import server.users.Mitglied;

import java.time.LocalDateTime;

public class Mahnung {

    private String mahnungsID;
    private String mitgliedsID;
    private String grund;
    private LocalDateTime verfallsdatum;

    public Mahnung(String mahnungsID, String mitgliedsID, String grund, LocalDateTime verfallsdatum){
        this.mitgliedsID = mitgliedsID;
        this.grund = grund;
        this.verfallsdatum = verfallsdatum;
        this.mahnungsID = mahnungsID;
        String anfrage = "insert into  Mahnung(MahnungsID, personenID, grund, Verfallsdatum, ) values (" + mahnungsID +", " + mitgliedsID + ", " + grund + ", "+ verfallsdatum + ");";

    }

    public String getMahnungsID() {
        return mahnungsID;
    }

    public String getMitgliedsID() {
        return mitgliedsID;
    }
}
