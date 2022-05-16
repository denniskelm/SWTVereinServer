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

import shared.communication.IGeraeteverwaltung;

import java.rmi.NoSuchObjectException;
import java.util.ArrayList;

public class Geraeteverwaltung implements IGeraeteverwaltung {
    private static ArrayList<Geraet> geraete;

    private static long IdCounter = 0;

    public Geraeteverwaltung() {
        geraete = new ArrayList<>();
    }

    public String geraetHinzufuegen(String name, String spender, int leihfrist, String kategorie, String beschreibung, String abholort) {
        if (geraete.size() >= 50000) throw new ArrayIndexOutOfBoundsException(); //TODO Exception

        //naechste ID generieren
        IdCounter++;
        String geraeteID = Long.toString(IdCounter);

        Geraet g = new Geraet(geraeteID, name, spender, leihfrist, kategorie, beschreibung, abholort);
        geraete.add(g);
        System.out.println("Geraet mit ID " +  g.getGeraeteID() + " hinzugefuegt. Anzahl Geraete: " +  geraete.size());
        return geraeteID;
    }

    public void geraetEntfernen(String geraeteID) throws NoSuchObjectException {
        geraete.remove(fetch(geraeteID));
        System.out.println("Geraet mit ID " +  geraeteID + " entfernt. Anzahl Geraete: " +  geraete.size());
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

    public void geraetReservieren(String geraeteID, String personenID) throws Exception {
        int reservierungen = 0;
        //Mitglied m = new Rollenverwaltung().fetch(personenID); // TODO später die richtige Rollenverwaltung nehmen

        for (Geraet g : geraete) {
            for (Ausleiher a : g.getReservierungsliste()) {
                if (a.getMitlgiedsID().equals(personenID)) {
                    reservierungen++; // TODO als Attribut des Mitglieds implementieren. Es ist dumm immer alle Geraete zu ueberprufen

                    if (reservierungen == 3)
                        break; // damit man bestenfalls nicht durch alle Geräte iterieren muss

                }
            }
        }

        if (reservierungen == 3)
            throw new ArrayIndexOutOfBoundsException();

//        if (m.isGesperrt())
//            throw new NoPermissionException();

        fetch(geraeteID).reservierungHinzufuegen(personenID);
        System.out.println("Geraet mit ID " +  geraeteID + " reserviert.");
    }

    //TODO
    public void reservierungStornieren(String geraeteID, String personenID) {
        return;
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

}
