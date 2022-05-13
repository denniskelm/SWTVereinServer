package server;
/*
@author
Raphael Kleebaum
Jonny Schlutter
//TODO Gabriel Kleebaum
//TODO Mhd Esmail Kanaan
//TODO Gia Huy Hans Tran
//TODO Ole Björn Adelmann
//TODO Bastian Reichert
//TODO Dennis Kelm
*/

import server.users.Mitglied;
import server.users.Rollenverwaltung;
import shared.communication.IGeraeteverwaltung;

import javax.naming.NoPermissionException;
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

        //naechste ID holen
        String geraeteID = Long.toString(IdCounter++);

        Geraet g = new Geraet(geraeteID, name, spender, leihfrist, kategorie, beschreibung, abholort);
        geraete.add(g);
        return geraeteID;
    }

    public Geraet fetch(String geraeteID) throws NoSuchObjectException { // warum heißt das nicht getGeraet
        for (Geraet g : geraete) {
            if (g.getGeraeteID().equals(geraeteID)) return g;
        }

        throw new NoSuchObjectException("Geraet mit ID:" + geraeteID + " nicht vorhanden.");
    }

    public void geraetReservieren(String geraeteID, String personenID) throws Exception {
        int reservierungen = 0;
        Mitglied m = new Rollenverwaltung().fetch(personenID); // TODO später die richtige Rollenverwaltung nehmen

        for (Geraet g : geraete) {
            for (Ausleiher a : g.getReservierungsliste()) {
                if (a.getMitlgiedsID().equals(personenID)) {
                    reservierungen++;

                    if (reservierungen == 3)
                        throw new ArrayIndexOutOfBoundsException(); // damit man bestenfalls nicht durch alle Geräte iterieren muss
                }
            }
        }

        if (m.isGesperrt())
            throw new NoPermissionException();

        fetch(geraeteID).reservierungHinzufuegen(personenID);
    }

    public void geraetAusgeben(String geraeteID) throws NoSuchObjectException {  //Mitarbeiter gibt Gerät fur mitglied
        this.fetch(geraeteID).ausgeben();
    }

    public void geraetAnnehmen(String geraeteID) throws NoSuchObjectException {
        this.fetch(geraeteID).annehmen();
    } //rueckgabe des geraets

    public void geraetEntfernen(String geraeteID) throws NoSuchObjectException {
        Geraet geraet = fetch(geraeteID);

        geraete.remove(geraet);
    }

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
        Geraet geraet = fetch(geraeteID);
        for (Geraet g : geraete) {
            if (g.getGeraeteID().equals(geraet.getGeraeteID())) {
                g.setHistorie(new ArrayList<>());
                break;
            }
        }
    }


    public Object[] geraeteAnzeigen() { //geraet Fenester anzeigen
        return geraete.toArray();
    }

    //Zum Testen der Geraeteverwaltung
    public String geraeteDatenAusgeben(String geraeteID) throws NoSuchObjectException {
        Geraet geraet = fetch(geraeteID);
        StringBuilder str = new StringBuilder(); //Stringvuilder klasse ist in Java integriert und kann die Woerter bsser hintereinander haengen

        str.append("ID: " + geraet.getGeraeteID());
        //TODO: Alle anderen Daten anhaengen

        return str.toString();
    }

}
