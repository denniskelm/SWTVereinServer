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

    public Gast(String personenID, String nachname, String vorname, String email, String password, String anschrift,
                String mitgliedsnr, int telefonnummer, boolean spender ){
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

            //Attribut kann für diese Rolle nicht geändert werden
            default -> {return;}
        }
    }

}

