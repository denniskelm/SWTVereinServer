package server;

import server.users.Mitglied;

public class Dienstleistungsgesuch {

    private String gesuch_ID;
    private String titel;
    private String beschreibung;
    private String kategorie;
    private String suchenderID;

    public Dienstleistungsgesuch(String gesuch_ID, String titel, String beschreibung, String kategorie, String suchenderID){
        this.gesuch_ID = gesuch_ID;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.kategorie = kategorie;
        this.suchenderID = suchenderID;

        //TODO: mehr Parameter gegeben als erwartet
        //String anfrage = "insert into  Dienstleitunsgesuch(angebots_ID, PersonenID, Titel, Beschreibung, Kategorie) values (" + gesuch_ID + ", " + suchenderID + ", " + titel + ", "+ beschreibung + ", " + kategorie + ", " + suchender + ");";
        //(Temporärer) Fix
        String anfrage = String.format("insert into Dienstleistungsgesuch(angebots_ID, PersonenID, Titel, Beschreibung, Kategorie) values \"%s\", \"%s\", \"%s\", \"%s\", \"%s\");", gesuch_ID, suchenderID, titel, beschreibung, kategorie);
    }

    public String getGesuch_ID() {
        return gesuch_ID;
    }

    /*public Mitglied getSuchender() {
        return suchender;
    } */

    public String getSuchender() {
        return suchenderID;
    }


    public void setTitel(String titel) {
        this.titel = titel;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    /* public void setSuchender(Mitglied suchender) {
        this.suchender = suchender;
    } */

    public void setSuchender(String suchenderID) {
        this.suchenderID = suchenderID;
    }
}
