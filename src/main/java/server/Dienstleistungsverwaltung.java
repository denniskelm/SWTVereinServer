package server;

import org.jetbrains.annotations.NotNull;
import shared.communication.IDienstleistungsverwaltung;

import java.time.LocalDateTime;
import java.util.ArrayList;

/*
@author
//TODO Raphael Kleebaum
Jonny Schlutter
Gabriel Kleebaum
Mhd Esmail Kanaan
//TODO Gia Huy Hans Tran
Ole Björn Adelmann
Bastian Reichert
//TODO Dennis Kelm
*/

public class Dienstleistungsverwaltung implements IDienstleistungsverwaltung {

    private ArrayList<Dienstleistungsangebot> angebote;
    private ArrayList<Dienstleistungsgesuch> gesuche;

    public ArrayList<Dienstleistungsangebot> getAngeboteArrayList() {
        return angebote;
    }

    public ArrayList<Dienstleistungsgesuch> getGesucheArrayList() {
        return gesuche;
    }

    public Dienstleistungsverwaltung() {
        angebote = new ArrayList<>();
        gesuche = new ArrayList<>();
    }



    public String gesuchErstellen(String titel, String beschreibung, String kategorie, String imageUrl, String ersteller) throws Exception {
        int anzahl = gesuche.size();
        String gesuch_ID;

        if (anzahl < 9)  // todo : nach dem testen ist die Rueckgabe nicht wie gewunscht
            gesuch_ID = "dg0000" + (anzahl + 1); // klammern hinzugefügt damit test richtig
        else if (anzahl < 99)
            gesuch_ID = "dg000" + (anzahl + 1);
        else if (anzahl < 999)
            gesuch_ID = "dg00" + (anzahl + 1);
        else if (anzahl < 9999)
            gesuch_ID = "dg0" + (anzahl + 1);
        else if (anzahl < 50000)
            gesuch_ID = "dg" + (anzahl + 1);
        else
            throw new Exception();

        Dienstleistungsgesuch g = new Dienstleistungsgesuch(gesuch_ID, titel, beschreibung, kategorie, ersteller);
        gesuche.add(g);
        return gesuch_ID;
    }

    public String angebotErstellen(String titel, String beschreibung, String kategorie, LocalDateTime ab, LocalDateTime bis , String personen_ID) throws Exception {
        int anzahl = angebote.size();
        String angebot_ID;

        if (anzahl < 9)
            angebot_ID = "da0000" + (anzahl + 1); //klammern hinzugefügt damit test richtig
        else if (anzahl < 99)
            angebot_ID = "da000" + (anzahl + 1);
        else if (anzahl < 999)
            angebot_ID = "da00" + (anzahl + 1);
        else if (anzahl < 9999)
            angebot_ID = "da0" + (anzahl + 1);
        else if (anzahl < 50000)
            angebot_ID = "da" + (anzahl + 1);
        else
            throw new Exception();

        Dienstleistungsangebot g = new Dienstleistungsangebot(angebot_ID, titel, beschreibung, kategorie, ab, bis, personen_ID);
        angebote.add(g);
        return angebot_ID;
    }

    public void gesuchLoeschen(String gesuch_ID) {
        for (Dienstleistungsgesuch g : gesuche){
            if(g.getGesuch_ID().equals(gesuch_ID)) {
                gesuche.remove(g);
                break;
            }
        }

    }

    public void angebotLoeschen(String angebots_ID) {
        for (Dienstleistungsangebot a : angebote){
            if(a.getAngebots_ID().equals(angebots_ID)) {
                angebote.remove(a);
                break;
            }
        }

    }

    public void gesuchAendern(String gesuchsID, Dienstleistungsgesuchdaten attr, Object wert) {
        for (Dienstleistungsgesuch a : gesuche) {
            if (a.getGesuch_ID().equals(gesuchsID)) {
                switch (attr) {
                    case GESUCH_ID -> a.setGesuch_ID(wert.toString());
                    case TITEL -> a.setTitel(wert.toString());
                    case BESCHREIBUNG -> a.setBeschreibung(wert.toString());
                    case KATEGORIE -> a.setKategorie(wert.toString());
                    case SUCHENDER_ID -> a.setSuchender_ID((wert.toString()));
                }
                break;
            }
        }
    }
    public void angebotAendern(String angebotsID, Dienstleistungsangebotdaten attr, Object wert) {
        for (Dienstleistungsangebot a : angebote) {
            if (a.getAngebots_ID().equals(angebotsID)) {
                switch(attr){
                    case ANGEBOTS_ID -> a.setAngebots_ID(wert.toString());
                    case TITEL -> a.setTitel(wert.toString());
                    case BESCHREIBUNG -> a.setBeschreibung(wert.toString());
                    case KATEGORIE -> a.setKategorie(wert.toString());
                    case AB -> a.setAb(LocalDateTime.parse(wert.toString()));
                    case BIS -> a.setBis(LocalDateTime.parse(wert.toString()));
                    case PERSONEN_ID -> a.setPersonenID(wert.toString());
                }
                break;
            }
        }
    }

    public void gesuchAnnehmen(String gesuchs_ID, String ersteller_ID, String nutzer_ID, int stunden) throws Exception {
        Dienstleistungsgesuch gesuch;
        int anzahl = gesuche.size();
        int counter = 0;

        for (Dienstleistungsgesuch g : gesuche) {
            if (g.getGesuch_ID() == gesuchs_ID && counter <= anzahl) {
                gesuch = g;
                //TODO g.getSuchender().veraendereStundenkonto(-stunden);
            } else
                throw new Exception();
        }
    }

    public void gesuchAnnehmenI(Dienstleistungsgesuch g, server.users.Mitglied ersteller, server.users.Mitglied nutzer, int stunden) throws Exception{
        Anfragenliste l= ersteller.getAnfragenliste();
        l.addgAnfrage(nutzer, g,stunden);
        }

    public void angebotAnfragenI(Dienstleistungsangebot a, server.users.Mitglied ersteller, server.users.Mitglied nutzer, int stunden) throws Exception{
        Anfragenliste l= ersteller.getAnfragenliste();
        l.addaAnfrage(nutzer, a,stunden);
    }

    public void angebotAnfragen(String angebotsID, String ersteller, String fragender) {

    }

}
