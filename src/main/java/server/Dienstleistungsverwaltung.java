package server;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import server.users.Mitglied;
import shared.communication.IDienstleistungsverwaltung;

import java.awt.Image;
import java.time.LocalDateTime;
import java.lang.Object;
import java.util.ArrayList;

/*
@author
//TODO Raphael Kleebaum
Jonny Schlutter
Gabriel Kleebaum
//TODO Mhd Esmail Kanaan
//TODO Gia Huy Hans Tran
Ole Björn Adelmann
//TODO Bastian Reichert
//TODO Dennis Kelm
*/

public class Dienstleistungsverwaltung implements IDienstleistungsverwaltung {

    private ArrayList<Dienstleistungsangebot> angebote;
    private ArrayList<Dienstleistungsgesuch> gesuche;

    public Dienstleistungsverwaltung() {
        angebote = new ArrayList<>();
        gesuche = new ArrayList<>();
    }

    public void gesuchErstellen(String titel, String beschreibung, String kategorie, String imageUrl, String ersteller) throws Exception {
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

    public void angebotErstellen(String titel, String beschreibung, String kategorie, LocalDateTime ab, LocalDateTime bis , String personen_ID) throws Exception {
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

    public void gesuchLoeschen(String gesuch_ID) {
        for (Dienstleistungsgesuch g : gesuche){
            if(g.getGesuch_ID() == gesuch_ID)
                gesuche.remove(g);
        }

    }

    public void angebotLoeschen(String angebots_ID) {
        for (Dienstleistungsangebot a : angebote){
            if(a.getAngebots_ID() == angebots_ID)
                angebote.remove(a);
        }

    }

    public void gesuchAendern(String gesuchsID, Object attr, Object wert) {

    }

    public void angebotAendern(String angebotsID, Object attr, Object wert) {

    }

    public void gesuchAnnehmen(String gesuchs_ID, String ersteller_ID, String nutzer_ID, int stunden) throws Exception{
        Dienstleistungsgesuch gesuch;
        int anzahl = gesuche.size();
        int counter = 0;

        for(Dienstleistungsgesuch g : gesuche) {
            if (g.getGesuch_ID() == gesuchs_ID && counter <= anzahl) {
                gesuch = g;
                //TODO g.getSuchender().veraendereStundenkonto(-stunden);
            }
            else
                throw new Exception();
        }


        }

    public void angebotAnfragen(String angebotsID, String ersteller, String fragender) {

    }

}
