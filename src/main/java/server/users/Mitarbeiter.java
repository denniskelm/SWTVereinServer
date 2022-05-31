package server.users;

import server.Mahnungsverwaltung;
import shared.communication.IMitarbeiter;

import java.time.LocalDateTime;

public class Mitarbeiter extends Mitglied implements IMitarbeiter {

    public Mitarbeiter(String personenID, String nachname, String vorname, String email, String password, String anschrift, String mitgliedsnr, String telefonnummer, boolean spender, LocalDateTime mitglied_seit, Mahnungsverwaltung mahnungen){
        super(personenID, nachname, vorname, email, password, anschrift, mitgliedsnr, telefonnummer, spender, mitglied_seit);
    }

    // Weiterer Konstruktor, falls man die Rolle von einem Gast ändert, da dann das Passwort schon gehasht ist
    public Mitarbeiter(String personenID, String nachname, String vorname, String email, int password, String anschrift, String mitgliedsnr, String telefonnummer, boolean spender, LocalDateTime mitglied_seit, Mahnungsverwaltung mahnungen){
        super(personenID, nachname, vorname, email, password, anschrift, mitgliedsnr, telefonnummer, spender, mitglied_seit);
    }

}