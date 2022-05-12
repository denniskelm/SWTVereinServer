package server;

import server.users.Mitglied;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Mahnungsverwaltung {

    public ArrayList<Mahnung> mahnungen;

    public Mahnungsverwaltung(){
        mahnungen = new ArrayList<>();
    }

    public void mahnungErstellen(String mitgliedsID, String grund, LocalDateTime verfallsdatum){
        String mahnungsID ="m" + mahnungen.size()+1;
        Mahnung m = new Mahnung(mahnungsID, mitgliedsID, grund, verfallsdatum);
        mahnungen.add(m);
    }

    public void mahnungLoeschen(Mahnung m){
        mahnungen.remove(m);
        String anfrage = "delete from mahnung where MahnungsID = " + m.getMahnungsID();
    }
}
