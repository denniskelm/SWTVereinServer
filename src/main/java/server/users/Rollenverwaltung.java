package server.users;

/*
@author
Raphael Kleebaum
Jonny Schlutter
Gabriel Kleebaum
Mhd Esmail Kanaan
Gia Huy Hans Tran
Ole Björn Adelmann
Bastian Reichert
Dennis Kelm
*/

import server.Mahnungsverwaltung;
import shared.communication.IRollenverwaltung;

import java.rmi.NoSuchObjectException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Rollenverwaltung implements IRollenverwaltung {

     private static ArrayList<Gast> gaeste;
     private static ArrayList<Mitglied> mitglieder;
     private static ArrayList<Mitarbeiter> mitarbeiter;
     private static ArrayList<Vorsitz> vorsitze;
     private static ArrayList<Mahnungsverwaltung> mahnungen;
     private long IdCounter;

     public Rollenverwaltung(){
         gaeste = new ArrayList<Gast>();
         mitglieder = new ArrayList<Mitglied>();
         mitarbeiter = new ArrayList<Mitarbeiter>();
         vorsitze = new ArrayList<Vorsitz>();
         IdCounter = 0;
     }

     public void gastHinzufuegen(String nachname, String vorname, String email, String password, String anschrift, String mitgliedsnr, int telefonnummer, boolean spender ) {
         if (gaeste.size() >= 50000) throw new ArrayIndexOutOfBoundsException();

         //naechste ID generieren
         IdCounter++;
         String personenID = Long.toString(IdCounter);

         Gast gast = new Gast(personenID,nachname,vorname,email,password,anschrift,mitgliedsnr,telefonnummer,spender);
         gaeste.add(gast);
         System.out.println("Gast mit ID " +  personenID + " hinzugefuegt. Anzahl Gaeste: " +  gaeste.size());
     }

     // TODO wie zum Teufel soll man das richtig machen?
     public void mitgliedHinzufuegen(String nachname, String vorname, String email, String password, String anschrift,
                                     String mitgliedsnr, int telefonnummer, boolean spender/*, Mahnungsverwaltung mahnungen, Profilseite profilseite */,
                                     LocalDateTime mitglied_seit) {
         if (mitglieder.size() >= 50000) throw new ArrayIndexOutOfBoundsException();

         //naechste ID generieren
         IdCounter++;
         String personenID = Long.toString(IdCounter);

         Mitglied m = new Mitglied(personenID,nachname,vorname,email,password,anschrift,mitgliedsnr,telefonnummer,spender, mitglied_seit);
         mitglieder.add(m);
         System.out.println("Mitglied mit ID " +  personenID + " hinzugefuegt. Anzahl Mitglieder: " +  mitglieder.size());

     }

    public Mitglied fetch(String mitgliederID) throws NoSuchObjectException {
        for (Mitglied m : mitglieder) {
            if (m.getPersonenID().equals(mitgliederID)) return m;
        }

        for (Mitglied m : mitarbeiter) {
            if (m.getPersonenID().equals(mitgliederID)) return m;
        }

        for (Mitglied m : vorsitze) {
            if (m.getPersonenID().equals(mitgliederID)) return m;
        }

        throw new NoSuchObjectException("Person mit ID: " + mitgliederID + " nicht vorhanden.");
    }

    public static ArrayList<Mitglied> getMitglieder() {
        return mitglieder;
    }

    public Object[] gastListeAnzeigen() { return gaeste.toArray(); }

    public Object[] mitgliedListeAnzeigen() { return mitglieder.toArray(); }

    public Object[] mitarbeiterListeAnzeigen() { return mitarbeiter.toArray(); }

    public Object[] vorsitzListeAnzeigen() { return vorsitze.toArray(); }

    public void rolleAendern(String mitgliedsID, Rolle rolle) {
        // TODO wie genau soll das funktionieren ?
    }

    public void nutzereintragAendern(String mitgliedsID, Personendaten attr, String wert) throws NoSuchObjectException {
        try {
            (fetch(mitgliedsID)).datenVerwalten(attr, wert);
        } catch (NoSuchObjectException e) {
            throw new NoSuchObjectException("");
        }
    }

    public Object[] mahnungsverwaltungAnzeigen() { return mahnungen.toArray(); }
}
