package server;

public class AngebotAnfrage {
    public server.users.Mitglied nutzer;
    private Dienstleistungsangebot angebot;
    public int stunden;

    public AngebotAnfrage (server.users.Mitglied nutzer, Dienstleistungsangebot angebot, int stunden) {
        this.nutzer = nutzer;
        this.stunden = stunden;
        this.angebot = angebot;
    }
}

