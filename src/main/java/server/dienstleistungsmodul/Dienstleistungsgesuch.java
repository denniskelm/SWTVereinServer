package server.dienstleistungsmodul;

import server.users.Mitglied;

public class Dienstleistungsgesuch {

    private String gesuch_ID;
    private String titel;
    private String beschreibung;
    private String kategorie;

    private String imageUrl;
    private String suchenderID; //MitgliedID z.b

    public Dienstleistungsgesuch(String gesuch_ID, String titel, String beschreibung, String kategorie, String imageUrl, String suchenderID){
        this.gesuch_ID = gesuch_ID;
        this.titel = titel;
        this.beschreibung = beschreibung;
        this.kategorie = kategorie;
        this.suchenderID = suchenderID;
        this.imageUrl =imageUrl;

        //TODO: mehr Parameter gegeben als erwartet
        //String anfrage = "insert into  Dienstleitunsgesuch(angebots_ID, PersonenID, Titel, Beschreibung, Kategorie) values (" + gesuch_ID + ", " + suchenderID + ", " + titel + ", "+ beschreibung + ", " + kategorie + ", " + suchender + ");";
        //(Temporärer) Fix
        String anfrage = String.format("insert into Dienstleistungsgesuch(angebots_ID, PersonenID, Titel, Beschreibung, Kategorie) values \"%s\", \"%s\", \"%s\", \"%s\", \"%s\");", gesuch_ID, suchenderID, titel, beschreibung, kategorie);
    }


    //Anmerkung : es kann sein dass die Getter Public sind, aber es macht fuer Esmail kein Sinn dass Setter auch public sind
    public String getGesuch_ID() {
        return gesuch_ID;
    }


    public String getSuchender() {
        return suchenderID;
    }

    public String getImageUrl() { return imageUrl; }

    public void setGesuch_ID(String gesuch_ID) { this.gesuch_ID = gesuch_ID; }


    public void setTitel(String titel) {
        this.titel = titel;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }

    public void setImageUrl(String url) { this.imageUrl = url; }

    /* public void setSuchender(Mitglied suchender) {
        this.suchender = suchender;
    } */

    public void setSuchender_ID(String suchenderID) {
        this.suchenderID = suchenderID;
    }

    public String getTitel(){return this.titel;}
    public String getBeschreibung(){return this.beschreibung;}
    public String getKategorie(){return this.kategorie;}
}
