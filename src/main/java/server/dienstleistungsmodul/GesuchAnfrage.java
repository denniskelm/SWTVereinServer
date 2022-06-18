package server.dienstleistungsmodul;
/**
 * @author Bastian Reichert
 */

import server.users.Mitglied;

public class GesuchAnfrage {
    public String anfrageID;

    public Mitglied nutzer ;
    public Dienstleistungsgesuch gesuch;
    public int stunden;

    public GesuchAnfrage (String anfrageId, Mitglied nutzer, Dienstleistungsgesuch gesuch, int stunden) {
        this.anfrageID = anfrageId;
        this.nutzer = nutzer;
        this.stunden = -stunden;
        this.gesuch = gesuch;
        }
    }


