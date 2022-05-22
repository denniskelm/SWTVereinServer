package server.dienstleistungsmodul;

import shared.communication.IMitglied;

/*
@author
Bastian Reichert
*/
public class AngebotAnfrage {
    public IMitglied nutzer;
    public Dienstleistungsangebot angebot;
    public int stunden;

    public AngebotAnfrage (IMitglied nutzer, Dienstleistungsangebot angebot, int stunden) {
        this.nutzer = nutzer;
        this.stunden = stunden;
        this.angebot = angebot;
    }
}


