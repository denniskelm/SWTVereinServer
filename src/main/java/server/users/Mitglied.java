package server.users;

import server.Mahnungsverwaltung;
import server.dienstleistungsmodul.Anfragenliste;
import shared.communication.IMitglied;

import java.time.LocalDateTime;

public class Mitglied extends Gast implements IMitglied {
    private int stundenkonto;
    private Anfragenliste anfragenliste; //da Profilseite nicht existiert, habe ich die Anfregenliste direkt zum Mitglied gepackt(kann ja noch verändert werden)

    private boolean ist_gesperrt;
    private LocalDateTime mitglied_seit;
    private int reservierungen;
    private Mahnungsverwaltung mahnungen;

    public Mitglied(String personenID, String nachname, String vorname, String email, String password, String anschrift,
                    String mitgliedsnr, String telefonnummer, boolean spender, Mahnungsverwaltung mahnungen/*, Profilseite profilseite */,
                    LocalDateTime mitglied_seit) {

        super(personenID, nachname, vorname, email, password, anschrift, mitgliedsnr, telefonnummer, spender);
        this.stundenkonto = 0;
        this.ist_gesperrt = false;
        this.mitglied_seit = mitglied_seit;
        this.anfragenliste = (new server.dienstleistungsmodul.Anfragenliste()).ofUser_ID(personenID);
        this.anfragenliste.nutzer = this;
        this.reservierungen = 0;
        this.mahnungen = mahnungen;
    }

    // Weiterer Konstruktor, falls man die Rolle von einem Gast ändert, da dann das Passwort schon gehasht ist
    public Mitglied(String personenID, String nachname, String vorname, String email, int password, String anschrift,
                    String mitgliedsnr, String telefonnummer, boolean spender, Mahnungsverwaltung mahnungen/*, Profilseite profilseite */,
                    LocalDateTime mitglied_seit) {

        super(personenID, nachname, vorname, email, password, anschrift, mitgliedsnr, telefonnummer, spender);
        this.stundenkonto = 0;
        this.ist_gesperrt = false;
        this.mitglied_seit = mitglied_seit;
        this.anfragenliste = new server.dienstleistungsmodul.Anfragenliste().ofUser_ID(personenID);
        this.anfragenliste.nutzer = this;
        this.reservierungen = 0;
        this.mahnungen = mahnungen;
    }

    public Mitglied(String personenID, String nachname, String vorname, String email, int password, String anschrift,
                    String mitgliedsnr, String telefonnummer, boolean spender, Mahnungsverwaltung mahnungen/*, Profilseite profilseite */,
                    LocalDateTime mitglied_seit, boolean ist_gesperrt) {

        super(personenID, nachname, vorname, email, password, anschrift, mitgliedsnr, telefonnummer, spender);
        this.stundenkonto = 0;
        this.ist_gesperrt = ist_gesperrt;
        this.mitglied_seit = mitglied_seit;
        this.anfragenliste = new server.dienstleistungsmodul.Anfragenliste().ofUser_ID(personenID);
        this.anfragenliste.nutzer = this;
        this.reservierungen = 0;
        this.mahnungen = mahnungen;
    }

    public void reservierungenErhöhen() {
        reservierungen++;
    }

    public void reservierungenVerringern() {
        reservierungen--;
    }

    public int getReservierungen() {
        return reservierungen;
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
    public LocalDateTime getMitgliedSeit() { return mitglied_seit; }

    // aendert ein angegebenes Attribut auf den angegbenen Wert
    public void datenVerwalten(Personendaten attr, String wert) {
        switch (attr) {
            case PERSONENID -> this.personenID = wert;
            case NACHNAME -> this.nachname = wert;
            case VORNAME -> this.vorname = wert;
            case E_MAIL -> this.email = wert;
            case PASSWORD -> this.password = Integer.parseInt(wert);
            case ANSCHRIFT -> this.anschrift = wert;
            case MITGLIEDSNR -> this.mitgliedsnr = wert;
            case TELEFONNUMMER -> this.telefonnummer = wert;
            case SPENDER -> this.spender = Boolean.parseBoolean(wert);
            case STUNDENKONTO -> this.stundenkonto = Integer.parseInt(wert);
            case IST_GESPERRT -> this.ist_gesperrt = Boolean.parseBoolean(wert);
            case RESERVIERUNGEN -> this.reservierungen = Integer.parseInt(wert);

            //Attribut kann für diese Rolle nicht geändert werden
            default -> {return;}
        }
    }

}