package server.users;

public class Gast {
    String personenID;
    String nachname;
    String vorname;
    String email;
    int password;
    String anschrift;
    String mitgliedsnr;
    int telefonnummer;
    boolean spender;

    public Gast(String personenID, String nachname, String vorname, String email, String password, String anschrift, String mitgliedsnr, int telefonnummer, boolean spender ){
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

    public String getPersonenID() {
        return personenID;
    }

    public void setPersonenID(String personenID) {
        this.personenID = personenID;
    }

    public void setNachname(String nachname) {
        this.nachname = nachname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public void setAnschrift(String anschrift) {
        this.anschrift = anschrift;
    }

    public void setMitgliedsnr(String mitgliedsnr) {
        this.mitgliedsnr = mitgliedsnr;
    }

    public void setTelefonnummer(int telefonnummer) {
        this.telefonnummer = telefonnummer;
    }

    public void setSpender(boolean spender) {
        this.spender = spender;
    }

    public void datenVerwalten(Personendaten attr, Object wert) {
        switch (attr) {
            case PERSONENID -> this.personenID = wert.toString();
            case NACHNAME -> this.nachname = wert.toString();
            case VORNAME -> this.vorname = wert.toString();
            case E_MAIL -> this.email = wert.toString();
            case PASSWORD -> this.password = Integer.parseInt(wert.toString());
            case ANSCHRIFT -> this.anschrift = wert.toString();
            case MITGLIEDSNR -> this.mitgliedsnr = wert.toString();
            case TELEFONNUMMER -> this.telefonnummer = Integer.parseInt(wert.toString());

            //Attribut kann für diese Rolle nicht geändert werden
            default -> {return;}
        }
    }

}

