package server;

import shared.communication.IMahnungsverwaltung;

import java.rmi.NoSuchObjectException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Mahnungsverwaltung implements IMahnungsverwaltung {

    public ArrayList<Mahnung> mahnungen;

    public Mahnungsverwaltung(){
        mahnungen = new ArrayList<>();
    }

    // Die Methode gibt die passende Mahnung zu der MahnungsId zurück
    public Mahnung fetch(String mahnungsID) throws NoSuchObjectException {
        for (Mahnung m : mahnungen) {
            if (m.getMahnungsID().equals(mahnungsID)) return m;
        }

        throw new NoSuchObjectException("");
    }

    public void mahnungErstellen(String mitgliedsID, String grund, LocalDateTime verfallsdatum){
        String mahnungsID ="m" + mahnungen.size()+1;
        Mahnung m = new Mahnung(mahnungsID, mitgliedsID, grund, verfallsdatum);
        mahnungen.add(m);
    }

    public void mahnungLoeschen(String mahnungsID){
        try {
            Mahnung m = fetch(mahnungsID);
            mahnungen.remove(m);
            String anfrage = "delete from mahnung where MahnungsID = " + m.getMahnungsID();
        } catch (NoSuchObjectException e) {
            e.printStackTrace();
        }
    }


}
