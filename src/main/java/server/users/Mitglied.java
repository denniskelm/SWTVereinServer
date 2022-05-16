package server.users;

import server.Mahnungsverwaltung;

import java.time.LocalDateTime;

public class Mitglied extends Gast {
    private int stundenkonto;
    private boolean ist_gesperrt;
    private LocalDateTime mitglied_seit;
    // private int ausgelieheneGeraete TODO

    public Mitglied(String personenID, String nachname, String vorname, String email, String password, String anschrift, String mitgliedsnr, int telefonnummer, boolean spender/*, Mahnungsverwaltung mahnungen, Profilseite profilseite */, LocalDateTime mitglied_seit){

        super(personenID, nachname, vorname, email, password, anschrift, mitgliedsnr, telefonnummer, spender);
        this.stundenkonto = 0;
        this.ist_gesperrt = false;
        this.mitglied_seit = mitglied_seit;
    }

    public void veraendereStundenkonto(int change) {
        this.stundenkonto += change;
    }

    public boolean isGesperrt() {
        return ist_gesperrt;
    }

    @Override
    public void datenVerwalten(Personendaten attr, String wert) {
        switch (attr) {
            case PERSONENID -> this.personenID = wert.toString();
            case NACHNAME -> this.nachname = wert.toString();
            case VORNAME -> this.vorname = wert.toString();
            case E_MAIL -> this.email = wert.toString();
            case PASSWORD -> this.password = Integer.parseInt(wert.toString());
            case ANSCHRIFT -> this.anschrift = wert.toString();
            case MITGLIEDSNR -> this.mitgliedsnr = wert.toString();
            case TELEFONNUMMER -> this.telefonnummer = Integer.parseInt(wert.toString());
            case SPENDER -> this.spender = Boolean.parseBoolean(wert.toString());
            case STUNDENKONTO -> this.stundenkonto = Integer.parseInt(wert.toString());
            case IST_GESPERRT -> this.ist_gesperrt = Boolean.parseBoolean(wert.toString());
            case MITGLIED_SEIT -> this.mitglied_seit = LocalDateTime.parse(wert.toString());

            //Attribut kann für diese Rolle nicht geändert werden
            default -> {return;}
        }
    }

}
