package server;

import server.users.Mitglied;

public class Dienstleistungsgesuch {

    private String gesuch_ID;
    private String titel;
    private String beschreibung;
    private String kategorie;
    private Mitglied suchender;

    public Dienstleistungsgesuch(String gesuch_ID, String titel, String beschreibung, String kategorie, Mitglied suchender){
        this.gesuch_ID = gesuch_ID;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.kategorie = kategorie;
        this.suchender = suchender;
        String anfrage = "insert into  Dienstleitunsgesuch(angebots_ID, PersonenID, Titel, Beschreibung, Kategorie) values (" + gesuch_ID + ", " + suchender.getPersonenID() + ", " + titel + ", "+ beschreibung + ", " + kategorie + ", " + suchender + ");";
    }


}
