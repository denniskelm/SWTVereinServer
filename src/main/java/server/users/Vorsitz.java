package server.users;

import server.Mahnungsverwaltung;

import java.time.LocalDateTime;

public class Vorsitz extends Mitarbeiter {
    public Vorsitz(String personenID, String nachname, String vorname, String email, String password, String anschrift, String mitgliedsnr, int telefonnummer, boolean spender, LocalDateTime mitglied_seit, Mahnungsverwaltung mahnungen){
        super(personenID, nachname, vorname, email, password, anschrift, mitgliedsnr, telefonnummer, spender, mitglied_seit, mahnungen);

    }
}
