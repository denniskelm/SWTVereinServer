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
import server.users.Mitglied;
import server.users.Personendaten;
import shared.communication.IGeraeteverwaltung;

import javax.naming.NoPermissionException;
import java.rmi.NoSuchObjectException;
import java.util.ArrayList;
import java.util.Objects;

public class Geraeteverwaltung implements IGeraeteverwaltung {
    private static ArrayList<Geraet> geraete;

    private static int IdCounter = 0;

    public Geraeteverwaltung() {
        geraete = new ArrayList<>();

        geraetHinzufuegen("Rasenmäher", "Gabriel Kleebaum", 5, "Geräte", "Nichts weiter zu sagen.", "Raum 2");
    }

    public String geraetHinzufuegen(String name, String spender, int leihfrist, String kategorie, String beschreibung, String abholort) {
        if (geraete.size() >= 50000) throw new ArrayIndexOutOfBoundsException();

        //naechste ID generieren
        String geraeteID;
        IdCounter++;
        if (IdCounter < 10) geraeteID = "g0000" + (IdCounter);
        else if (IdCounter < 100) geraeteID = "g000" + (IdCounter);
        else if (IdCounter < 1000) geraeteID = "g00" + (IdCounter);
        else if (IdCounter < 10000) geraeteID = "g0" + (IdCounter);
        else geraeteID = "g" + (IdCounter);

        Geraet g = new Geraet(geraeteID, name, spender, leihfrist, kategorie, beschreibung, abholort);
        geraete.add(g);
        System.out.println("Geraet mit ID " +  g.getGeraeteID() + " hinzugefuegt. Anzahl Geraete: " +  geraete.size());
        return geraeteID;
    }

    public void geraetEntfernen(String geraeteID) throws NoSuchObjectException {
        geraete.remove(fetch(geraeteID));
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
        IdCounter = 0;
        System.out.println("Geraeteverwaltung zurueckgesetzt.");
    }

    //Mitglied reserviert ein Geraet
    public void geraetReservieren(String geraeteID, String personenID) throws Exception {
        server.users.Rollenverwaltung rv = VereinssoftwareServer.rollenverwaltung;
        Mitglied m = rv.fetch(personenID);
        Geraet g = fetch(geraeteID);

        if (m.isGesperrt()) throw new NoPermissionException("Mitglied ist gesperrt.");
        if (m.getReservierungen() >= 3) throw new ArrayIndexOutOfBoundsException("Mitglied hat bereits 3 oder mehr Reservierungen.");
        for (Ausleiher a :  g.getReservierungsliste()) {
            if (Objects.equals(a.getMitlgiedsID(), personenID)) {
                throw new Exception("Mitglied hat das Geraet bereits reserviert.");
            }
        }

        g.reservierungHinzufuegen(personenID); //Reservierung hinzufuegen
        m.reservierungenErhöhen(); //Anzahl der Reservierung des Mitglieds erhöhen
        System.out.println("Geraet mit ID " +  geraeteID + " reserviert.");
    }

    //Mitglied storniert eine Reservierung für ein Geraet
    public void reservierungStornieren(String geraeteID, String personenID) throws Exception {
        server.users.Rollenverwaltung rv = VereinssoftwareServer.rollenverwaltung;
        Mitglied m = rv.fetch(personenID);
        Geraet g = fetch(geraeteID);
        if (Objects.equals(g.getReservierungsliste().get(0).getMitlgiedsID(), personenID) && g.getLeihstatus() == Status.AUSGELIEHEN)
            throw new Exception("Geraet ist momentan von dir ausgeliehen.");

        g.reservierungEntfernen(personenID);
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
        Mitglied m = rv.fetch(g.getReservierungsliste().get(0).getMitlgiedsID());
        m.reservierungenVerringern();

        g.annehmen();
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
    }

    public void historieZuruecksetzen(String geraeteID) throws NoSuchObjectException {
        Geraet g = fetch(geraeteID);
        g.setHistorie(new ArrayList<>());
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

    public static ArrayList<Geraet> getGeraeteArrayList() {
        return geraete;
    }

    public Object[][] omniGeraeteDaten() throws NoSuchObjectException {
        Object[][] aliste = new Object[50000][8];

        for(int i = 0; i < geraete.size(); i++) {
            aliste[i] = getAngeboteInformationen(geraete.get(i).getGeraeteID());
        }

        return aliste;
    }

    public Object[] getAngeboteInformationen(String geraeteID) throws NoSuchObjectException {

        Geraet a = fetch(geraeteID);
        Object[] info = new Object[8];
        info[0] = a.getGeraeteID();
        info[1] = a.getGeraetName();
        info[2] = a.getGeraetBeschreibung();
        info[3] = a.getKategorie();
        info[4] = a.getSpenderName();
        info[5] = a.getLeihfrist();
        info[6] = a.getLeihstatus().getName();
        info[7] = a.getGeraetAbholort();


        return info;
    }

}
