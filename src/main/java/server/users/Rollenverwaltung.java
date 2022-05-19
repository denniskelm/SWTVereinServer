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

     public void gastHinzufuegen(String nachname, String vorname, String email, String password, String anschrift, String mitgliedsnr, int telefonnummer, boolean spender) {
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

    public void rolleAendern(String mitgliedsID, Rolle rolle) throws Exception {
        Mitglied mitgliedInAlterRolle;
        Gast g;
        LocalDateTime mitglied_seit;

        try {
            mitgliedInAlterRolle = fetch(mitgliedsID);
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

        if (mitgliedInAlterRolle.getClass() ==  rolle.getKlasse())
                throw new Exception("Der Nutzer hat diese Rolle schon.");

        if (mitgliedInAlterRolle instanceof Gast)
            mitglied_seit = LocalDateTime.now();
        else
            mitglied_seit = mitgliedInAlterRolle.mitglied_seit;

         switch (rolle) {
             case GAST:

                     g = new Gast(mitgliedInAlterRolle.personenID,
                             mitgliedInAlterRolle.nachname,
                             mitgliedInAlterRolle.vorname,
                             mitgliedInAlterRolle.email,
                             mitgliedInAlterRolle.password,
                             mitgliedInAlterRolle.anschrift,
                             mitgliedInAlterRolle.mitgliedsnr,
                             mitgliedInAlterRolle.telefonnummer,
                             mitgliedInAlterRolle.spender);

                 gaeste.add(g);
                 break;
             case MITGLIED:
                 g = new Mitglied(mitgliedInAlterRolle.personenID,
                            mitgliedInAlterRolle.nachname,
                            mitgliedInAlterRolle.vorname,
                            mitgliedInAlterRolle.email,
                            mitgliedInAlterRolle.password,
                            mitgliedInAlterRolle.anschrift,
                            mitgliedInAlterRolle.mitgliedsnr,
                            mitgliedInAlterRolle.telefonnummer,
                            mitgliedInAlterRolle.spender,
                            mitglied_seit);

                mitglieder.add((Mitglied) g);
                break;

             case MITARBEITER:
                     g = new Mitarbeiter(mitgliedInAlterRolle.personenID,
                             mitgliedInAlterRolle.nachname,
                             mitgliedInAlterRolle.vorname,
                             mitgliedInAlterRolle.email,
                             mitgliedInAlterRolle.password,
                             mitgliedInAlterRolle.anschrift,
                             mitgliedInAlterRolle.mitgliedsnr,
                             mitgliedInAlterRolle.telefonnummer,
                             mitgliedInAlterRolle.spender,
                             mitglied_seit,
                             new Mahnungsverwaltung());

                 mitarbeiter.add((Mitarbeiter) g);
                 break;

             case VORSITZ:
                     g = new Vorsitz(mitgliedInAlterRolle.personenID,
                             mitgliedInAlterRolle.nachname,
                             mitgliedInAlterRolle.vorname,
                             mitgliedInAlterRolle.email,
                             mitgliedInAlterRolle.password,
                             mitgliedInAlterRolle.anschrift,
                             mitgliedInAlterRolle.mitgliedsnr,
                             mitgliedInAlterRolle.telefonnummer,
                             mitgliedInAlterRolle.spender,
                             mitglied_seit,
                             new Mahnungsverwaltung());

                 vorsitze.add((Vorsitz) g);
                 break;
         }

        nutzerAusAlterListeEntfernen(mitgliedInAlterRolle);

    }

    public void nutzereintragAendern(String mitgliedsID, Personendaten attr, String wert) throws NoSuchObjectException {
        try {
            (fetch(mitgliedsID)).datenVerwalten(attr, wert);
        } catch (NoSuchObjectException e) {
            throw new NoSuchObjectException("");
        }
    }

    public Object[] mahnungsverwaltungAnzeigen() { return mahnungen.toArray(); }

    private void nutzerAusAlterListeEntfernen(Mitglied mitglied) {
        if (mitglied instanceof Gast) {
            for (int i = 0; i < gaeste.size(); i++) {
                if (gaeste.get(i).personenID.equals(mitglied.getPersonenID())) {
                    gaeste.remove(i);
                    break;
                }
            }
        } else if (mitglied instanceof Mitglied) {
            for (int i = 0; i < mitglieder.size(); i++) {
                if (mitglieder.get(i).personenID.equals(mitglied.getPersonenID())) {
                    mitglieder.remove(i);
                    break;
                }
            }
        } else if (mitglied instanceof Mitarbeiter) {
            for (int i = 0; i < mitarbeiter.size(); i++) {
                if (mitarbeiter.get(i).personenID.equals(mitglied.getPersonenID())) {
                    mitarbeiter.remove(i);
                    break;
                }
            }
        } else if (mitglied instanceof Vorsitz) {
            for (int i = 0; i < vorsitze.size(); i++) {
                if (vorsitze.get(i).personenID.equals(mitglied.getPersonenID())) {
                    vorsitze.remove(i);
                    break;
                }
            }
        }
    }

    public boolean login(String email, int password) throws Exception {

        for (Mitglied m : mitglieder) {
            if (m.email.equals(email)) {
                if (m.password == password)
                    return true;
                else
                    throw new Exception("E-Mail oder Passwort falsch!");
            }
        }

        for (Mitglied m : mitarbeiter) {
            if (m.email.equals(email)) {
                if (m.password == password)
                    return true;
                else
                    throw new Exception("E-Mail oder Passwort falsch!");
            }
        }

        for (Mitglied m : vorsitze) {
            if (m.email.equals(email)) {
                if (m.password == password)
                    return true;
                else
                    throw new Exception("E-Mail oder Passwort falsch!");
            }
        }

        throw new Exception("E-Mail oder Passwort falsch!");

    }

}
