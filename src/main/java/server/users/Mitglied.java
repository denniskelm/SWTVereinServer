package server.users;

import java.time.LocalDateTime;

public class Mitglied extends Gast {
    private int stundenkonto;
    //private Mahnungsverwaltung mahnungen;
    private boolean ist_gesperrt;
    //private Profilseite profilseite;
    private LocalDateTime mitglied_seit;

    public Mitglied(String personenID, String nachname, String vorname, String email, String password, String anschrift, String mitgliedsnr, int telefonnummer, boolean spender/*, Mahnungsverwaltung mahnungen, Profilseite profilseite */, LocalDateTime mitglied_seit){
        super(personenID, nachname, vorname, email, password, anschrift, mitgliedsnr, telefonnummer, spender);
        this.stundenkonto = 0;
    //    this.mahnungen = mahnungen;
        this.ist_gesperrt = false;
    //    this.profilseite = profilseite;
        this.mitglied_seit = mitglied_seit;
    }


}
