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

    public Geraeteverwaltung() {
        geraete = new ArrayList<>();
    }

    public void geraetHinzufuegen(String name, String spender, int leihfrist, String kategorie, String beschreibung, String abholort) {
        if (geraete.size() < 50000) {
            Geraet g = new Geraet("", name, spender, leihfrist, kategorie, beschreibung, abholort);
            geraete.add(g);
        }
    }

    public void geraetReservieren(Geraet geraet, String ausleiherID) {
        geraet.reservierungHinzufuegen(ausleiherID);
    }

    public void geraetAusgeben(Geraet geraet) {
        geraet.ausgeben();
    }

    public void geraetAnnehmen(Geraet geraet) {
        geraet.annehmen();
    }

    public void geraetEntfernen(Geraet geraet) {
        geraete.remove(geraet);
    }

    public void gereateDatenVerwalten(Geraet g, Object attr, Object wert) {
    }

    public void historieZuruecksetzen(Geraet geraet) {
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
