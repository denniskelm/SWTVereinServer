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

import server.Dienstleistungsgesuch;
import server.Geraet;
import server.Mahnungsverwaltung;
import shared.communication.IRollenverwaltung;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Rollenverwaltung implements IRollenverwaltung {

     private static ArrayList<Gast> gaeste;
     private static ArrayList<Mitglied> mitglieder;
     private static ArrayList<Mitarbeiter> mitarbeiter;
     private static ArrayList<Vorsitz> vorsitze;
     private static ArrayList<Mahnungsverwaltung> mahnungen;

     public Rollenverwaltung(){
         gaeste = new ArrayList();
         mitglieder = new ArrayList();
         mitarbeiter = new ArrayList();
         vorsitze = new ArrayList();
     }

    public Mitglied fetch(String mitgliederID) throws NoSuchObjectException { // warum heißt das nicht getGeraet
        for (Mitglied m : mitglieder) {
            if (m.getPersonenID().equals(mitgliederID)) return m;
        }

        for (Mitglied m : mitarbeiter) {
            if (m.getPersonenID().equals(mitgliederID)) return m;
        }

        for (Mitglied m : vorsitze) {
            if (m.getPersonenID().equals(mitgliederID)) return m;
        }

        throw new NoSuchObjectException("");
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
        System.out.println("test");
        try {
            (fetch(mitgliedsID)).datenVerwalten(attr, wert);
        } catch (NoSuchObjectException e) {
            throw new NoSuchObjectException("");
        }


    }

    public Object[] mahnungsverwaltungAnzeigen() { return mahnungen.toArray(); }
}
