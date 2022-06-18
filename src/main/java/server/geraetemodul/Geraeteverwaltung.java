package server.geraetemodul;
/*
@author
Raphael Kleebaum
Jonny Schlutter
//TODO Gabriel Kleebaum
 Mhd Esmail Kanaan
//TODO Gia Huy Hans Tran
//TODO Ole Björn Adelmann
//TODO Bastian Reichert
//TODO Dennis Kelm
*/

import server.VereinssoftwareServer;
import server.db.GeraeteDB;
import server.users.Mitglied;
import server.users.Rollenverwaltung;
import shared.communication.IGeraeteverwaltung;

import javax.naming.NoPermissionException;
import java.rmi.NoSuchObjectException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class Geraeteverwaltung implements IGeraeteverwaltung {
    private ArrayList<Geraet> geraete;
    private int IdCounter;
    private GeraeteDB gDB;


    public Geraeteverwaltung() {
        try {
            gDB = new GeraeteDB();
        } catch (SQLException e) {
            System.err.println("Verbindung zu Datenbank konnte nicht hergestellt werden!");
            throw new RuntimeException(e);
        }

        geraete = gDB.getGeraeteList();
        IdCounter = gDB.getIdCounter();
    }

    public String geraetHinzufuegen(String name, String spender, int leihfrist, String kategorie, String beschreibung, String abholort, String bild) {
        if (geraete.size() >= 50000) throw new ArrayIndexOutOfBoundsException();

        //naechste ID generieren
        String geraeteID;
        IdCounter++;
        if (IdCounter < 10) geraeteID = "g0000" + (IdCounter);
        else if (IdCounter < 100) geraeteID = "g000" + (IdCounter);
        else if (IdCounter < 1000) geraeteID = "g00" + (IdCounter);
        else if (IdCounter < 10000) geraeteID = "g0" + (IdCounter);
        else geraeteID = "g" + (IdCounter);

        Geraet g = new Geraet(geraeteID, name, spender, leihfrist, kategorie, beschreibung, abholort, bild);
        geraete.add(g);
        gDB.geraetHinzufuegen(g);
        System.out.println("Geraet mit ID " +  g.getGeraeteID() + " hinzugefuegt. Anzahl Geraete: " +  geraete.size());
        return geraeteID;
    }

    public void geraetEntfernen(String geraeteID) throws NoSuchObjectException {
        geraete.remove(fetch(geraeteID));
        gDB.geraetEntfernen(geraeteID);
        System.out.println("Geraet mit ID " +  geraeteID + " entfernt. Anzahl Geraete: " +  geraete.size());
    }

    public ArrayList<Geraet> getGeraete() {
        return geraete;
    }

    public int getIdCounter() {
        return IdCounter;
    }

    public Geraet fetch(String geraeteID) throws NoSuchObjectException {
        for (Geraet g : geraete) {
            if (g.getGeraeteID().equals(geraeteID)) return g;
        }

        throw new NoSuchObjectException("Geraet mit ID: " + geraeteID + " nicht vorhanden.");
    }

    public void reset() {
        geraete = new ArrayList<>();
        gDB.reset();
        IdCounter = 0;
        System.out.println("Geraeteverwaltung zurueckgesetzt.");
    }

    //Mitglied reserviert ein Geraet
    public void geraetReservieren(String geraeteID, String personenID) throws Exception {
        Rollenverwaltung rv = VereinssoftwareServer.rollenverwaltung;
        Mitglied m = rv.fetch(personenID);
        Geraet g = fetch(geraeteID);

        if (m.isGesperrt()) throw new NoPermissionException("Mitglied ist gesperrt.");
        if (m.getReservierungen() >= 3) throw new ArrayIndexOutOfBoundsException("Mitglied hat bereits 3 oder mehr Reservierungen.");
        for (Ausleiher a :  g.getReservierungsliste()) {
            if (a.getMitgliedsID().equals(personenID)) {
                throw new Exception("Mitglied hat das Geraet bereits reserviert.");
            }
        }

        g.reservierungHinzufuegen(personenID); //Reservierung hinzufuegen

        Ausleiher ausleiher = g.getReservierungsliste().get(g.getReservierungsliste().size() - 1);
        gDB.geraetReservieren(geraeteID, ausleiher);

        m.reservierungenErhöhen(); //Anzahl der Reservierung des Mitglieds erhöhen

        System.out.println("Geraet mit ID " +  geraeteID + " reserviert.");
    }

    //Mitglied storniert eine Reservierung für ein Geraet
    public void reservierungStornieren(String geraeteID, String personenID) throws Exception {
        server.users.Rollenverwaltung rv = VereinssoftwareServer.rollenverwaltung;
        Mitglied m = rv.fetch(personenID);
        Geraet g = fetch(geraeteID);
        if (Objects.equals(g.getReservierungsliste().get(0).getMitgliedsID(), personenID) && g.getLeihstatus() == Status.AUSGELIEHEN)
            throw new Exception("Geraet ist momentan von dir ausgeliehen.");

        g.reservierungEntfernen(personenID);
        gDB.reservierungStornieren(geraeteID, personenID);
        m.reservierungenVerringern(); //Anzahl Reservierung des Mitglieds verringern
        System.out.println("Geraet mit ID " +  geraeteID + " storniert.");
    }

    //Mitarbeiter gibt das Geraet an ein Mitglied aus
    public void geraetAusgeben(String geraeteID) throws Exception {
        Geraet g = fetch(geraeteID);
        if (g.getReservierungsliste().isEmpty())
            throw new Exception("keine Reservierung vorhanden.");
        if (g.getLeihstatus() != Status.BEANSPRUCHT)
            throw new Exception("ausgeliehenes/freies Geraet kann nicht ausgegeben werden.");

        g.ausgeben();
        gDB.geraetAusgeben(geraeteID);
    }

    //Mitarbeiter nimmt das Geraet von einem Mitglied wieder an
    public void geraetAnnehmen(String geraeteID) throws Exception {
        Geraet g = fetch(geraeteID);

        if (g.getReservierungsliste().isEmpty())
            throw new Exception("keine Reservierung vorhanden.");
        if (g.getLeihstatus() != Status.AUSGELIEHEN)
            throw new Exception("beanspruchtes/freies Geraet kann nicht angenommen werden.");

        //Anzahl Reservierung des Mitglieds verringern
        server.users.Rollenverwaltung rv = VereinssoftwareServer.rollenverwaltung;
        Mitglied m = rv.fetch(g.getReservierungsliste().get(0).getMitgliedsID());
        m.reservierungenVerringern();

        g.annehmen();
        gDB.geraetAnnehmen(geraeteID);
    }

    //Mitarbeiter aendert Informationen zu einem Geraet
    public void geraeteDatenVerwalten(String geraeteID, Geraetedaten attr, Object wert) throws NoSuchObjectException {
        Geraet g = fetch(geraeteID);

        switch (attr) { //wie setter methoden
            case NAME -> g.setName(wert.toString());
            case SPENDERNAME -> g.setSpenderName(wert.toString());
            case LEIHFRIST -> g.setLeihfrist(Integer.parseInt(wert.toString()));
            case KATEGORIE -> g.setKategorie(wert.toString());
            case BESCHREIBUNG -> g.setBeschreibung(wert.toString());
            case ABHOLORT -> g.setAbholort(wert.toString());
        }

        gDB.geraeteDatenVerwalten(geraeteID, attr, wert);
    }

    public void historieZuruecksetzen(String geraeteID) throws NoSuchObjectException {
        Geraet g = fetch(geraeteID);
        g.setHistorie(new ArrayList<Ausleiher>());
        gDB.historieZuruecksetzen(geraeteID);
    }


    public ArrayList<Geraet> geraeteAnzeigen() { //geraet Fenester anzeigen
        return geraete;
    }

    //Zum Testen der Geraeteverwaltung
    public String geraeteDatenAusgeben(String geraeteID) throws NoSuchObjectException {
        Geraet geraet = fetch(geraeteID);
        StringBuilder str = new StringBuilder();

        str.append("ID: ");
        str.append(geraet.getGeraeteID());
        str.append(", ");

        str.append("Name: ");
        str.append(geraet.getGeraetName());
        str.append(", ");

        str.append("Spender: ");
        str.append(geraet.getSpenderName());
        str.append(", ");

        str.append("Leihfrist: ");
        str.append(geraet.getLeihfrist());
        str.append(", ");

        str.append("Kategorie: ");
        str.append(geraet.getKategorie());
        str.append(", ");

        str.append("Beschreibung: ");
        str.append(geraet.getGeraetBeschreibung());
        str.append(", ");

        str.append("Abholort: ");
        str.append(geraet.getGeraetAbholort());

        return str.toString();
    }

    public ArrayList<Geraet> getGeraeteArrayList() {
        return geraete;
    }

    public Object[][] omniGeraeteDaten() throws NoSuchObjectException {
        Object[][] gliste = new Object[50000][11];

        for(int i = 0; i < geraete.size(); i++) {
            gliste[i] = getGeraeteInformationen(geraete.get(i).getGeraeteID());
        }

        return gliste;
    }

    public Object[] getGeraeteInformationen(String geraeteID) throws NoSuchObjectException {

        Geraet g = fetch(geraeteID);
        Object[] info = new Object[11];
        info[0] = g.getGeraeteID();
        info[1] = g.getGeraetName();
        info[2] = g.getGeraetBeschreibung();
        info[3] = g.getKategorie();
        info[4] = g.getSpenderName();
        info[5] = g.getLeihfrist();
        info[6] = g.getLeihstatus().toString();
        info[7] = g.getGeraetAbholort();
        info[8] = g.getHistorieListeAlsArray();
        info[9] = g.getReservierungsListeAlsArray();
        info[10] = g.getBild();


        return info;
    }

}
