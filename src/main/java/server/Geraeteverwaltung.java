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

import java.util.ArrayList;

public class Geraeteverwaltung {
    private static ArrayList<Geraet> geraete;

    public static void geraeteVerwaltungLaden() {
        geraete = new ArrayList<>();
    }

    public static void geraetHinzufuegen(String name, String spender, int leihfrist, String kategorie, String beschreibung, String abholort) {
        if (geraete.size() < 50000) {
            Geraet g = new Geraet("", name, spender, leihfrist, kategorie, beschreibung, abholort);
            geraete.add(g);
        }
    }

    public static void geraetReservieren(Geraet geraet) {
    }

    public static void geraetAusgeben(Geraet geraet) {
    }

    public static void geraetAnnehmen(Geraet geraet) {
    }

    public static void geraetEntfernen(Geraet geraet) {
    }

    public static void geraeteDatenVerwalten(Geraet g, Object attr, Object wert) {
    }

    public static void historieZuruecksetzen(Geraet geraet) {
    }

    public static void geraeteAnzeigen() {
    }


}
