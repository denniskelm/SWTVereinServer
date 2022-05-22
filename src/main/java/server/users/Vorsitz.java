package server.users;

import server.Mahnungsverwaltung;
import shared.communication.IVorsitz;

import java.time.LocalDateTime;

public class Vorsitz extends Mitarbeiter implements IVorsitz {
    public Vorsitz(String personenID, String nachname, String vorname, String email, String password, String anschrift, String mitgliedsnr, int telefonnummer, boolean spender, LocalDateTime mitglied_seit, Mahnungsverwaltung mahnungen){
        super(personenID, nachname, vorname, email, password, anschrift, mitgliedsnr, telefonnummer, spender, mitglied_seit, mahnungen);

    }

    // Weiterer Konstruktor, falls man die Rolle von einem Gast ändert, da dann das Passwort schon gehasht ist
    public Vorsitz(String personenID, String nachname, String vorname, String email, int password, String anschrift, String mitgliedsnr, int telefonnummer, boolean spender, LocalDateTime mitglied_seit, Mahnungsverwaltung mahnungen){
        super(personenID, nachname, vorname, email, password, anschrift, mitgliedsnr, telefonnummer, spender, mitglied_seit, mahnungen);

    }

}