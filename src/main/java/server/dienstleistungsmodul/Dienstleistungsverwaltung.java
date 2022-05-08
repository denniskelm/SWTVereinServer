package server.dienstleistungsmodul;

import server.dienstleistungsmodul.Dienstleistungsangebot;
import server.dienstleistungsmodul.Dienstleistungsgesuch;
import server.users.Mitglied;

import java.time.LocalDateTime;
import java.lang.Object;

/*
@author
Raphael Kleebaum
Jonny Schlutter
Gabriel Kleebaum
Mhd Esmail Kanaan
Gia Huy Hans Tran
Ole Björn Adelmann
Bastian Reichert
Dennis Kelm
*/

public class Dienstleistungsverwaltung {

    public static void gesuchErstellen(String d_ID, String titel, String beschreibung, String kategorie, Mitglied ersteller) {
        new Dienstleistungsgesuch(d_ID, titel, beschreibung, kategorie, ersteller);
    }

    public static void angebotErstellen(String d_id, String titel, String beschreibung, String kategorie, LocalDateTime ab, LocalDateTime bis, Mitglied ersteller) {
        new Dienstleistungsangebot(d_id, titel, beschreibung, kategorie, ab, bis, ersteller);
    }

    public static void gesuchLoeschen(Dienstleistungsgesuch gesuch) {

    }

    public static void angebotLoeschen(Dienstleistungsangebot angebot) {

    }

    public static void gesuchAendern(Dienstleistungsgesuch gesuch, Object attr, Object wert) {

    }

    public static void angebotAendern(Dienstleistungsangebot angebot, Object attr, Object wert) {

    }

    public static void gesuchAnnehmen(Dienstleistungsgesuch gesuch, String ersteller, String nutzer, int stunden) {

    }

    public static void angebotAnfragen(Dienstleistungsangebot angebot, String ersteller, String fragender) {

    }

}
