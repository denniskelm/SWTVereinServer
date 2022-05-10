package server;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import server.users.Mitglied;

import java.awt.Image;
import java.time.LocalDateTime;
import java.lang.Object;
import java.util.ArrayList;

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

public class Dienstleistungsverwaltung {

    private static ArrayList<Dienstleistungsangebot> angebote;
    private static ArrayList<Dienstleistungsgesuch> gesuche;

    public static void gesuchErstellen(String titel, String beschreibung, String kategorie, Mitglied ersteller) throws Exception {
        int anzahl = gesuche.size();
        String gesuch_ID;

        if (anzahl < 9)
            gesuch_ID = "dg0000" + anzahl + 1;
        else if (anzahl < 99)
            gesuch_ID = "dg000" + anzahl + 1;
        else if (anzahl < 999)
            gesuch_ID = "dg00" + anzahl + 1;
        else if (anzahl < 9999)
            gesuch_ID = "dg0" + anzahl + 1;
        else if (anzahl < 50000)
            gesuch_ID = "dg" + anzahl + 1;
        else
            throw new Exception();

        Dienstleistungsgesuch g = new Dienstleistungsgesuch(gesuch_ID, titel, beschreibung, kategorie, ersteller);
        gesuche.add(g);
    }

    public static void angebotErstellen(String titel, String beschreibung, String kategorie, LocalDateTime ab, LocalDateTime bis , String personen_ID) throws Exception {
        int anzahl = angebote.size();
        String angebot_ID;

        if (anzahl < 9)
            angebot_ID = "da0000" + anzahl + 1;
        else if (anzahl < 99)
            angebot_ID = "da000" + anzahl + 1;
        else if (anzahl < 999)
            angebot_ID = "da00" + anzahl + 1;
        else if (anzahl < 9999)
            angebot_ID = "da0" + anzahl + 1;
        else if (anzahl < 50000)
            angebot_ID = "da" + anzahl + 1;
        else
            throw new Exception();

        Dienstleistungsangebot g = new Dienstleistungsangebot(angebot_ID, titel, beschreibung, kategorie, ab, bis, personen_ID);
        angebote.add(g);
    }

    public static void gesuchLoeschen(String gesuch_ID) {
        for (Dienstleistungsgesuch g : gesuche){
            if(g.getGesuch_ID() == gesuch_ID)
                gesuche.remove(g);
        }

    }

    public static void angebotLoeschen(String angebots_ID) {
        for (Dienstleistungsangebot a : angebote){
            if(a.getAngebots_ID() == angebots_ID)
                angebote.remove(a);
        }

    }

    public static void gesuchAendern(Dienstleistungsgesuch gesuch, Object attr, Object wert) {

    }

    public static void angebotAendern(Dienstleistungsangebot angebot, Object attr, Object wert) {

    }

    public static void gesuchAnnehmen(String gesuchs_ID, String nutzer_ID, int stunden) throws Exception{
        Dienstleistungsgesuch gesuch;
        int anzahl = gesuche.size();
        int counter = 0;

        for(Dienstleistungsgesuch g : gesuche) {
            if (g.getGesuch_ID() == gesuchs_ID && counter <= anzahl) {
                gesuch = g;
                g.getSuchender().veraendereStundenkonto(-stunden);
            }
            else
                throw new Exception();
        }


        }

    public static void angebotAnfragen(Dienstleistungsangebot angebot, String ersteller, String fragender) {

    }

}
