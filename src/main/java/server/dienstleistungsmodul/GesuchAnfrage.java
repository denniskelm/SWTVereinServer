package server.dienstleistungsmodul;
/*
@author
Bastian Reichert
*/
import server.dienstleistungsmodul.Dienstleistungsgesuch;
import server.users.Mitglied;

public class GesuchAnfrage {
    public Mitglied nutzer ;
    public Dienstleistungsgesuch gesuch;
    public int stunden;

    public GesuchAnfrage (Mitglied nutzer, Dienstleistungsgesuch gesuch, int stunden) {
        this.nutzer = nutzer;
        this.stunden = -stunden;
        this.gesuch = gesuch;
        }
    }


