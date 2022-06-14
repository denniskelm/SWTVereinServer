package server.dienstleistungsmodul;

import server.users.Mitglied;

/*
@author
Bastian Reichert
*/
public class AngebotAnfrage {
    public String anfrageID;

    public Mitglied nutzer;
    public Dienstleistungsangebot angebot;
    public int stunden;

    public AngebotAnfrage (String anfrageID, Mitglied nutzer, Dienstleistungsangebot angebot, int stunden) {
        this.anfrageID = anfrageID;
        this.nutzer = nutzer;
        this.stunden = stunden;
        this.angebot = angebot;
    }
}


