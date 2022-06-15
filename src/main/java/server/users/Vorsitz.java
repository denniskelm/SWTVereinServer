package server.users;

import shared.communication.IVorsitz;

import java.time.LocalDateTime;

public class Vorsitz extends Mitarbeiter implements IVorsitz {
    public Vorsitz(String personenID, String nachname, String vorname, String email, String password, String anschrift, String mitgliedsnr, String telefonnummer, boolean spender, LocalDateTime mitglied_seit){
        super(personenID, nachname, vorname, email, password, anschrift, mitgliedsnr, telefonnummer, spender, mitglied_seit);

    }

    // Weiterer Konstruktor, falls man die Rolle von einem Gast ändert, da dann das Passwort schon gehasht ist
    public Vorsitz(String personenID, String nachname, String vorname, String email, int password, String anschrift, String mitgliedsnr, String telefonnummer, boolean spender, LocalDateTime mitglied_seit){
        super(personenID, nachname, vorname, email, password, anschrift, mitgliedsnr, telefonnummer, spender, mitglied_seit);

    }

}