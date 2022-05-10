package server;

import server.users.Mitglied;

import java.time.LocalDateTime;

public class Dienstleistungsangebot {

    private String angebots_ID;
    private String titel;
    private String beschreibung;
    private String kategorie;
    private LocalDateTime ab;
    private LocalDateTime bis;
    private Mitglied dienstleister;

    public Dienstleistungsangebot(String angebots_ID, String titel, String beschreibung, String kategorie, LocalDateTime ab, LocalDateTime bis , Mitglied dienstleister){
        this.angebots_ID = angebots_ID;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.kategorie = kategorie;
        this.ab = ab;
        this.bis = bis;
        this.dienstleister = dienstleister;
        String anfrage = "insert into  Dienstleitunsgangebot(angebots_ID, personenID Titel, Beschreibung, Kategorie) values (" + angebots_ID + ", " + dienstleister.getPersonenID() + ", " + titel + ", "+ beschreibung + ", " + kategorie + ", " + dienstleister + " );";

    }
}
