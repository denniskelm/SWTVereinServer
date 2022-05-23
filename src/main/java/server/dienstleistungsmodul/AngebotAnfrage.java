package server.dienstleistungsmodul;

import server.users.Mitglied;

/*
@author
Bastian Reichert
*/
public class AngebotAnfrage {
    public Mitglied nutzer;
    public Dienstleistungsangebot angebot;
    public int stunden;

    public AngebotAnfrage (Mitglied nutzer, Dienstleistungsangebot angebot, int stunden) {
        this.nutzer = nutzer;
        this.stunden = stunden;
        this.angebot = angebot;
    }
}


