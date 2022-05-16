package server.users;

import server.dienstleistungsmodul.Anfragenliste;

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

    public Anfragenliste getAnfragenliste(){  //TODO muss wahrscheinlich aus der DB geholt werden
        return this.anfragenliste;
    }

    @Override
    public void datenVerwalten(Personendaten attr, String wert) {
        switch (attr) {
            case PERSONENID -> this.personenID = wert;
            case NACHNAME -> this.nachname = wert;
            case VORNAME -> this.vorname = wert;
            case E_MAIL -> this.email = wert;
            case PASSWORD -> this.password = Integer.parseInt(wert);
            case ANSCHRIFT -> this.anschrift = wert;
            case MITGLIEDSNR -> this.mitgliedsnr = wert;
            case TELEFONNUMMER -> this.telefonnummer = Integer.parseInt(wert);
            case SPENDER -> this.spender = Boolean.parseBoolean(wert);
            case STUNDENKONTO -> this.stundenkonto = Integer.parseInt(wert);
            case IST_GESPERRT -> this.ist_gesperrt = Boolean.parseBoolean(wert);

            //Attribut kann für diese Rolle nicht geändert werden
            default -> {return;}
        }
    }

}
