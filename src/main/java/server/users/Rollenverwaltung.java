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

    public void nutzereintragAendern(String mitgliedsID, Mitgliederdaten attr, Object wert) throws Exception{
        for (Mitglied m : mitglieder) {
            if (m.getPersonenID().equals(mitgliedsID)) {
                switch (attr) {
                    case PERSONENID -> m.setPersonenID(wert.toString());
                    case NACHNAME -> m.setNachname(wert.toString());
                    case VORNAME -> m.setVorname(wert.toString());
                    case E_MAIL -> m.setEmail(wert.toString());
                    case PASSWORD -> m.setPassword((wert.toString()).hashCode());
                    case ANSCHRIFT -> m.setAnschrift(wert.toString());
                    case MITGLIEDSNR -> m.setMitgliedsnr(wert.toString());
                    case TELEFONNUMMER -> m.setTelefonnummer(Integer.parseInt(wert.toString()));
                    case SPENDER -> m.setSpender(Boolean.parseBoolean(wert.toString()));
                    case STUNDENKONTO -> m.setStundenkonto(Integer.parseInt(wert.toString()));
                    case MAHNUNGEN -> throw new Exception();
                    case IST_GESPERRT -> m.setIst_gesperrt(Boolean.parseBoolean(wert.toString()));
                    case MITGLIED_SEIT -> m.setMitglied_seit(LocalDateTime.parse(wert.toString()));
                }
                break;
            }
        }
    }

    public Object[] mahnungsverwaltungAnzeigen() { return mahnungen.toArray()}
}
