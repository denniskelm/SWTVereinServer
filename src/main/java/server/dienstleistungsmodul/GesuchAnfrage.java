package server.dienstleistungsmodul;
/*
@author
Bastian Reichert
*/
import server.dienstleistungsmodul.Dienstleistungsgesuch;
import shared.communication.IMitglied;

public class GesuchAnfrage {
    public IMitglied nutzer ;
    public Dienstleistungsgesuch gesuch;
    public int stunden;

    public GesuchAnfrage (IMitglied nutzer, Dienstleistungsgesuch gesuch, int stunden) {
        this.nutzer = nutzer;
        this.stunden = -stunden;
        this.gesuch = gesuch;
        }
    }


