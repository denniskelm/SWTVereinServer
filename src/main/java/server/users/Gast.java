package server.users;

public class Gast {
    private String personenID;
    private String nachname;
    private String vorname;
    private String email;
    private int password;
    private String anschrift;
    private String mitgliedsnr;
    private int telefonnummer;
    private boolean spender;

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

}

