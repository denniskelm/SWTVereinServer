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

import server.Geraet;
import shared.communication.IRollenverwaltung;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class Rollenverwaltung implements IRollenverwaltung {

     private static ArrayList<Gast> gaeste;
     private static ArrayList<Mitglied> mitglieder;
     private static ArrayList<Mitarbeiter> mitarbeiter;
     private static ArrayList<Vorsitz> vorsitze;

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

    public void gastListeAnzeigen() {

    }

    public void mitgliedListeAnzeigen() {

    }

    public void mitarbeiterListeAnzeigen() {

    }

    public void vorsitzListeAnzeigen() {

    }

    public void rolleAendern(Mitglied mitglied, Rolle rolle) {

    }

    public void nutzereintragAendern(String mitgliedsID, Object attr, Object wert) {

    }

    public void mahnungsverwaltungAnzeigen() {

    }
}
