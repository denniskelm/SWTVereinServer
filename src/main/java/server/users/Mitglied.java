package server.users;

import server.Mahnungsverwaltung;

import java.time.LocalDateTime;

public class Mitglied extends Gast {
    private int stundenkonto;
    private Anfragenliste anfragenliste; //da Profilseite nicht existiert, habe ich die Anfregenliste direkt zum Mitglied gepackt(kann ja noch verändert werden)

    private boolean ist_gesperrt;
    private LocalDateTime mitglied_seit;
    // private int ausgelieheneGeraete TODO

    public Mitglied(String personenID, String nachname, String vorname, String email, String password, String anschrift, String mitgliedsnr, int telefonnummer, boolean spender/*, Mahnungsverwaltung mahnungen, Profilseite profilseite */, LocalDateTime mitglied_seit){

        super(personenID, nachname, vorname, email, password, anschrift, mitgliedsnr, telefonnummer, spender);
        this.stundenkonto = 0;
        this.ist_gesperrt = false;
        this.mitglied_seit = mitglied_seit;
        this.anfragenliste = new Anfragenliste(personenID);
        this.anfragenliste.nutzer = this;
    }

    public void veraendereStundenkonto(int change) {
        this.stundenkonto += change;
    }

    public boolean isGesperrt() {
        return ist_gesperrt;
    }

    public void setStundenkonto(int stundenkonto) {
        this.stundenkonto = stundenkonto;
    }

    public void setIst_gesperrt(boolean ist_gesperrt) {
        this.ist_gesperrt = ist_gesperrt;
    }

    public void setMitglied_seit(LocalDateTime mitglied_seit) {
        this.mitglied_seit = mitglied_seit;
    }

    public Anfragenliste getAnfragenliste(){  //TODO muss wahrscheinlich aus der DB geholt werden
        return this.anfragenliste;
    }

    @Override
    public void datenVerwalten(Personendaten attr, Object wert) {
        switch (attr) {
            case ID -> this.personenID = wert.toString();
            case NACHNAME -> this.nachname = wert.toString();
            case VORNAME -> this.vorname = wert.toString();
            case EMAIL -> this.email = wert.toString();
            case PASSWORT -> this.password = Integer.parseInt(wert.toString());
            case ANSCHRIFT -> this.anschrift = wert.toString();
            case MITGLIEDSNUMMER -> this.mitgliedsnr = wert.toString();
            case TELEFONNUMMER -> this.telefonnummer = Integer.parseInt(wert.toString());
            case SPENDERSTATUS -> this.spender = Boolean.parseBoolean(wert.toString());
            case STUNDENZAHL -> this.stundenkonto = Integer.parseInt(wert.toString());
            case SPERRSTATUS -> this.ist_gesperrt = Boolean.parseBoolean(wert.toString());

            //Attribut kann für diese Rolle nicht geändert werden
            default -> {return;}
        }
    }

}
