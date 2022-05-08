package server;
/*
@author
Raphael Kleebaum
Jonny Schlutter
Gabriel Kleebaum
Mhd Esmail Kanaan
Gia Huy Hans Tran
Ole Björn Adelmann
Bastian Reichert
Dennis Kelm
*/

import server.Geraet;

import java.util.ArrayList;

public class Geraeteverwaltung {
    private static ArrayList<Geraet> geraete;
    private static long IdCounter = 0;

    public static void geraeteVerwaltungLaden() {
        geraete = new ArrayList<>();
    }

    public static void geraetHinzufuegen(String name, String spender, int leihfrist, String kategorie, String beschreibung, String abholort) {
        if (geraete.size() < 50000) return; //TODO Exception

        //naechste ID holen
        String geraeteID = Long.toString(IdCounter++);

        Geraet g = new Geraet(geraeteID, name, spender, leihfrist, kategorie, beschreibung, abholort);
        geraete.add(g);
    }

    public static void geraetReservieren(Geraet geraet, String personenID) {
        if (!geraete.contains(geraet)) return; // TODO Exception
        // TODO Ueberpruefung ob mitglied berechtigt ist (mehr als 3 Geräte ausgeliehen, Ausleihsperre, ...)

        for (Geraet g : geraete) {
            if (g.getGeraeteID() == geraet.getGeraeteID()) g.reservierungHinzufuegen(personenID);
        }
    }

    public static void geraetAusgeben(Geraet geraet) {
        if (!geraete.contains(geraet)) return; // TODO Exception

        for (Geraet g : geraete) {
            if (g.getGeraeteID() == geraet.getGeraeteID()) g.ausgeben();
        }
    }

    public static void geraetAnnehmen(Geraet geraet) {
        if (!geraete.contains(geraet)) return; // TODO Exception

        for (Geraet g : geraete) {
            if (g.getGeraeteID() == geraet.getGeraeteID()) g.annehmen();
        }
    }

    public static void geraetEntfernen(Geraet geraet) {
        if (!geraete.contains(geraet)) return; // TODO Exception

        geraete.remove(geraet);
    }

    public static void geraeteDatenVerwalten(Geraet g, Object attr, Object wert) {
        if (!geraete.contains(g)) return; // TODO Exception

    }

    public void historieZuruecksetzen(Geraet geraet) {
        for (Geraet g : geraete) {
            if (g.getGeraeteID().equals(geraet.getGeraeteID())) {
                g.setHistorie(new ArrayList<>());
                break;
            }
        }
    }


    public static void geraeteAnzeigen() {
    }


}
