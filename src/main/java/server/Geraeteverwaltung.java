package server;
/*
@author
Raphael Kleebaum
Jonny Schlutter
//TODO Gabriel Kleebaum
//TODO Mhd Esmail Kanaan
//TODO Gia Huy Hans Tran
//TODO Ole Björn Adelmann
//TODO Bastian Reichert
//TODO Dennis Kelm
*/

import shared.communication.IGeraeteverwaltung;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Geraeteverwaltung implements IGeraeteverwaltung {
    private static ArrayList<Geraet> geraete;
    private static long IdCounter = 0;

    public Geraeteverwaltung() {
        geraete = new ArrayList<>();
    }

    public void geraetHinzufuegen(String name, String spender, int leihfrist, String kategorie, String beschreibung, String abholort) {
        if (geraete.size() < 50000) return; //TODO Exception

        //naechste ID holen
        String geraeteID = Long.toString(IdCounter++);

        Geraet g = new Geraet(geraeteID, name, spender, leihfrist, kategorie, beschreibung, abholort);
        geraete.add(g);
    }

    public void geraetReservieren(String geraeteID, String personenID) {
        // TODO Ueberpruefung ob mitglied berechtigt ist (mehr als 3 Geräte ausgeliehen, Ausleihsperre, ...)

        for (Geraet g : geraete) {
            if (g.getGeraeteID().equals(geraet.getGeraeteID())) g.reservierungHinzufuegen(personenID);
        }
    }

    public void geraetAusgeben(String geraeteID) {
        this.fetch(geraeteID).ausgeben();
    }

    public void geraetAnnehmen(String geraeteID) {
        this.fetch(geraeteID).annehmen();
    }

    public void geraetEntfernen(Geraet geraet) {
        if (!geraete.contains(geraet)) return; // TODO Exception

        geraete.remove(geraet);
    }

    public void geraeteDatenVerwalten(String geraeteID, Object attr, Object wert) {
        Geraet g = fetch(geraeteID);
    }

    public void historieZuruecksetzen(String geraeteID) {
        Geraet geraet = fetch(geraeteID);
        for (Geraet g : geraete) {
            if (g.getGeraeteID().equals(geraet.getGeraeteID())) {
                g.setHistorie(new ArrayList<>());
                break;
            }
        }
    }


    public void geraeteAnzeigen() {
    }


}
