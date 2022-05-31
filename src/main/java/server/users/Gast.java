package server.users;

import shared.communication.IGast;

import java.time.LocalDateTime;

public class Gast implements IGast {
    String personenID;
    String nachname;
    String vorname;
    String email;
    int password;
    String anschrift;
    String mitgliedsnr;
    String telefonnummer;
    boolean spender;

    public Gast(String personenID, String nachname, String vorname, String email, String password, String anschrift,
                String mitgliedsnr, String telefonnummer, boolean spender ){
        this.personenID = personenID;
        this.nachname = nachname;
        this.vorname = vorname;
        this.email = email;
        this.password = password.hashCode();
        this.anschrift = anschrift;
        this.mitgliedsnr = mitgliedsnr;
        this.telefonnummer = telefonnummer;
        this.spender = spender;
    }

    // Weiterer Konstruktor, falls man die Rolle von einem Gast ändert, da dann das Passwort schon gehasht ist
    public Gast(String personenID, String nachname, String vorname, String email, int password, String anschrift,
                String mitgliedsnr, String telefonnummer, boolean spender ){
        this.personenID = personenID;
        this.nachname = nachname;
        this.vorname = vorname;
        this.email = email;
        this.password = password;
        this.anschrift = anschrift;
        this.mitgliedsnr = mitgliedsnr;
        this.telefonnummer = telefonnummer;
        this.spender = spender;
    }

    public String getPersonenID() {
        return personenID;
    }

    public String getNachname() { return nachname; }

    public String getAnschrift() { return anschrift; }

    public String getVorname() { return vorname; }
    public String getEmail() { return email; }
    public int getPassword() { return password; }
    public String getMitgliedsNr() { return mitgliedsnr; }
    public String getTelefonNr() { return telefonnummer; }
    public boolean getSpenderStatus() { return spender; }

    public void datenVerwalten(Personendaten attr, String wert) {
        switch (attr) {
            case PERSONENID -> this.personenID = wert.toString();
            case NACHNAME -> this.nachname = wert.toString();
            case VORNAME -> this.vorname = wert.toString();
            case E_MAIL -> this.email = wert.toString();
            case PASSWORD -> this.password = Integer.parseInt(wert.toString());
            case ANSCHRIFT -> this.anschrift = wert.toString();
            case MITGLIEDSNR -> this.mitgliedsnr = wert.toString();
            case TELEFONNUMMER -> this.telefonnummer = wert.toString();

            //Attribut kann für diese Rolle nicht geändert werden
            default -> {return;}
        }
    }

}