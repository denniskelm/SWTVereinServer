package server.dienstleistungsmodul;

import server.dienstleistungsmodul.Dienstleistungsgesuch;

public class GesuchAnfrage {
    public server.users.Mitglied nutzer ;
    private Dienstleistungsgesuch gesuch;
    public int stunden;

    public GesuchAnfrage (server.users.Mitglied nutzer, Dienstleistungsgesuch gesuch, int stunden) {
        this.nutzer = nutzer;
        this.stunden = -stunden;
        this.gesuch = gesuch;
        }
    }


