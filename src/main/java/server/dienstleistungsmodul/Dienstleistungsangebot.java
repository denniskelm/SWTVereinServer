package server.dienstleistungsmodul;

import server.users.Mitglied;

import java.time.LocalDateTime;

public class Dienstleistungsangebot {

    private String angebots_ID;
    private String titel;
    private String beschreibung;
    private String kategorie;
    private LocalDateTime ab;
    private LocalDateTime bis;
    private String imageUrl;
    private String personenID;

    public Dienstleistungsangebot(String angebots_ID, String titel, String beschreibung, String kategorie, LocalDateTime ab, LocalDateTime bis ,String imageUrl, String personen_ID){
        this.angebots_ID = angebots_ID;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.kategorie = kategorie;
        this.ab = ab;
        this.bis = bis;
        this.imageUrl = imageUrl;
        this.personenID = personen_ID;
        String anfrage = "insert into  Dienstleitungsangebot(angebots_ID, personenID Titel, Beschreibung, Kategorie) values (" + angebots_ID + ", " + personen_ID + ", " + titel + ", "+ beschreibung + ", " + kategorie ;
        //Sql Anfrage , QueryString

    }

    public String getAngebots_ID() {
        return angebots_ID;
    }

    public void setAngebots_ID(String angebots_ID) {
        this.angebots_ID = angebots_ID;
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

    public void setAb(LocalDateTime ab) {
        this.ab = ab;
    }

    public void setBis(LocalDateTime bis) {
        this.bis = bis;
    }

    public void setPersonenID(String personenID) {
        this.personenID = personenID;
    }

    public String getTitel(){return this.titel;}
    public String getBeschreibung(){return this.beschreibung;}
    public String getKategorie(){return this.kategorie;}
    public LocalDateTime getTime1(){return this.ab;}
    public LocalDateTime getTime2(){return this.bis;}
}
