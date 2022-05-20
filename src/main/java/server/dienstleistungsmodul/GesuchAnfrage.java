package server.dienstleistungsmodul;
/*
@author
Bastian Reichert
*/
import server.dienstleistungsmodul.Dienstleistungsgesuch;

public class GesuchAnfrage {
    public server.users.Mitglied nutzer ;
    public Dienstleistungsgesuch gesuch;
    public int stunden;

    public GesuchAnfrage (server.users.Mitglied nutzer, Dienstleistungsgesuch gesuch, int stunden) {
        this.nutzer = nutzer;
        this.stunden = -stunden;
        this.gesuch = gesuch;
        }
    }


