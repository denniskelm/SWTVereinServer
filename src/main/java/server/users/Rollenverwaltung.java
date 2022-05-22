package server.users;

/*
@author
Raphael Kleebaum
Jonny Schlutter
Ole Björn Adelmann
*/

import server.Mahnungsverwaltung;
import shared.communication.*;

import java.rmi.NoSuchObjectException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Rollenverwaltung implements IRollenverwaltung {

     private static ArrayList<IGast> gaeste;
     private static ArrayList<IMitglied> mitglieder;
     private static ArrayList<IMitarbeiter> mitarbeiter;
     private static ArrayList<IVorsitz> vorsitze;
     private static ArrayList<Mahnungsverwaltung> mahnungen;
     private int IdCounter;

     public Rollenverwaltung(){
         gaeste = new ArrayList<IGast>();
         mitglieder = new ArrayList<IMitglied>();
         mitarbeiter = new ArrayList<IMitarbeiter>();
         vorsitze = new ArrayList<IVorsitz>();
         IdCounter = 0;

         mitgliedHinzufuegen("Ehrenmann", "Stefan", "stefan.ehrenmann@t-online.de", "12345678", "Huglfingstr. 27", "M4657", 110, false, LocalDateTime.now());
         mitgliedHinzufuegen("Tran", "Huy", "huy@email.de", "1234", "Musterstr. 1", "ABC", 123, true, LocalDateTime.now().minusDays(3));
         mitgliedHinzufuegen("Kelm", "Dennis", "dennis@email.de", "2345", "Teststr. 4", "DEF", 234, false, LocalDateTime.now().minusDays(20));
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
     
     public void mitgliedHinzufuegen(String nachname, String vorname, String email, String password, String anschrift,
                                     String mitgliedsnr, int telefonnummer, boolean spender/*, Mahnungsverwaltung mahnungen, Profilseite profilseite */,
                                     LocalDateTime mitglied_seit) {
         if (mitglieder.size() >= 50000) throw new ArrayIndexOutOfBoundsException();

         //naechste ID generieren
         IdCounter++;
         String personenID = Long.toString(IdCounter);

         Mitglied m = new Mitglied(personenID, nachname, vorname, email, password, anschrift, mitgliedsnr, telefonnummer, spender, mitglied_seit);
         mitglieder.add(m);

     }

    public IMitglied fetch(String mitgliederID) throws NoSuchObjectException {
        for (IMitglied m : vorsitze) {
            if (m.getPersonenID().equals(mitgliederID)) return m;
        }

        for (IMitglied m : mitarbeiter) {
            if (m.getPersonenID().equals(mitgliederID)) return m;
        }

        for (IMitglied m : mitglieder) {
            if (m.getPersonenID().equals(mitgliederID)) return m;
        }


        throw new NoSuchObjectException("Person mit ID: " + mitgliederID + " nicht vorhanden.");
    }

    public IGast fetchGaeste(String personenID) throws NoSuchObjectException{
        for (IGast g : gaeste) {
            if (g.getPersonenID().equals(personenID)) return g;
        }
        throw new NoSuchObjectException("Person mit ID: " + personenID + " nicht vorhanden.");
    }

    public  ArrayList<IMitglied> getMitglieder() {
        return mitglieder;
    }


    public ArrayList<IMitarbeiter> getMitarbeiter() {
        return mitarbeiter;
    }

    public ArrayList<IVorsitz> getVorsitze() {
        return vorsitze;
    }

    public ArrayList<Mahnungsverwaltung> getMahnungen() {
        return mahnungen;
    }

    public long getIdCounter() {
        return IdCounter;
    }

    public Object[] gastListeAnzeigen() { return gaeste.toArray(); }

    public Object[] mitgliedListeAnzeigen() { return mitglieder.toArray(); }

    public Object[] mitarbeiterListeAnzeigen() { return mitarbeiter.toArray(); }

    public Object[] vorsitzListeAnzeigen() { return vorsitze.toArray(); }

    public void rolleAendern(String mitgliedsID, Rolle rolle) throws Exception {
        IMitglied mitgliedInAlterRolle;
        IGast GastInAlterRolle;
        IGast g;
        LocalDateTime mitglied_seit;

        try {
            mitgliedInAlterRolle = fetch(mitgliedsID);
        } catch (NoSuchObjectException e) {
            try {
                GastInAlterRolle = fetchGaeste(mitgliedsID);
                mitglied_seit = LocalDateTime.now();

                if(GastInAlterRolle.getClass() == rolle.getKlasse()) {
                    throw new Exception("Der Nutzer hat diese Rolle bereits.");
                }
                rolleAendernSwitch(GastInAlterRolle, rolle, mitglied_seit);
                return;

            } catch (NoSuchObjectException f) {
                throw new RuntimeException(f);
            }
        }

        if (mitgliedInAlterRolle.getClass() == rolle.getKlasse())
            throw new Exception("Der Nutzer hat diese Rolle bereits.");

        mitglied_seit = mitgliedInAlterRolle.getMitgliedSeit();

        rolleAendernSwitch(mitgliedInAlterRolle, rolle, mitglied_seit);
    }

    private void rolleAendernSwitch(IGast gast, Rolle rolle, LocalDateTime mitglied_seit) {
        Gast g;
        switch (rolle) {
            case GAST:

                g = new Gast(gast.getPersonenID(),
                        gast.getNachname(),
                        gast.getVorname(),
                        gast.getEmail(),
                        gast.getPassword(),
                        gast.getAnschrift(),
                        gast.getMitgliedsNr(),
                        gast.getTelefonNr(),
                        gast.getSpenderStatus());

                gaeste.add(g);
                break;
            case MITGLIED:
                g = new Mitglied(gast.getPersonenID(),
                        gast.getNachname(),
                        gast.getVorname(),
                        gast.getEmail(),
                        gast.getPassword(),
                        gast.getAnschrift(),
                        gast.getMitgliedsNr(),
                        gast.getTelefonNr(),
                        gast.getSpenderStatus(),
                        mitglied_seit);

                mitglieder.add((Mitglied) g);
                break;

            case MITARBEITER:
                g = new Mitarbeiter(gast.getPersonenID(),
                        gast.getNachname(),
                        gast.getVorname(),
                        gast.getEmail(),
                        gast.getPassword(),
                        gast.getAnschrift(),
                        gast.getMitgliedsNr(),
                        gast.getTelefonNr(),
                        gast.getSpenderStatus(),
                        mitglied_seit,
                        new Mahnungsverwaltung());

                mitarbeiter.add((Mitarbeiter) g);
                break;

            case VORSITZ:
                g = new Vorsitz(gast.getPersonenID(),
                        gast.getNachname(),
                        gast.getVorname(),
                        gast.getEmail(),
                        gast.getPassword(),
                        gast.getAnschrift(),
                        gast.getMitgliedsNr(),
                        gast.getTelefonNr(),
                        gast.getSpenderStatus(),
                        mitglied_seit,
                        new Mahnungsverwaltung());

                vorsitze.add((Vorsitz) g);
                break;
        }

        nutzerAusAlterListeEntfernen(gast);
    }

    public void nutzereintragAendern(String mitgliedsID, Personendaten attr, String wert) throws NoSuchObjectException {
        try {
            (fetch(mitgliedsID)).datenVerwalten(attr, wert);
        } catch (NoSuchObjectException e) {
            try {
                (fetchGaeste(mitgliedsID)).datenVerwalten(attr, wert);
            } catch (NoSuchObjectException f) {
                throw  f;
            }
        }
    }

    public Object[] mahnungsverwaltungAnzeigen() { return mahnungen.toArray(); }

    private void nutzerAusAlterListeEntfernen(IGast mitglied) {
        if (mitglied instanceof Gast) {
            for (int i = 0; i < gaeste.size(); i++) {
                if (gaeste.get(i).getPersonenID().equals(mitglied.getPersonenID())) {
                    gaeste.remove(i);
                    break;
                }
            }
        } else if (mitglied instanceof Mitglied) {
            for (int i = 0; i < mitglieder.size(); i++) {
                if (mitglieder.get(i).getPersonenID().equals(mitglied.getPersonenID())) {
                    mitglieder.remove(i);
                    break;
                }
            }
        } else if (mitglied instanceof Mitarbeiter) {
            for (int i = 0; i < mitarbeiter.size(); i++) {
                if (mitarbeiter.get(i).getPersonenID().equals(mitglied.getPersonenID())) {
                    mitarbeiter.remove(i);
                    break;
                }
            }
        } else if (mitglied instanceof Vorsitz) {
            for (int i = 0; i < vorsitze.size(); i++) {
                if (vorsitze.get(i).getPersonenID().equals(mitglied.getPersonenID())) {
                    vorsitze.remove(i);
                    break;
                }
            }
        }
    }

    public boolean login(String email, String password) throws Exception {

        for (IGast m : gaeste) {
            if (m.getEmail().equals(email)) {
                if (m.getPassword() == password.hashCode())
                    return true;
                else
                    throw new Exception("E-Mail oder Passwort falsch!");
            }
        }

        for (IMitglied m : mitglieder) {
            if (m.getEmail().equals(email)) {
                if (m.getPassword() == password.hashCode())
                    return true;
                else
                    throw new Exception("E-Mail oder Passwort falsch!");
            }
        }

        for (IMitglied m : mitarbeiter) {
            if (m.getEmail().equals(email)) {
                if (m.getPassword() == password.hashCode())
                    return true;
                else
                    throw new Exception("E-Mail oder Passwort falsch!");
            }
        }

        for (IMitglied m : vorsitze) {
            if (m.getEmail().equals(email)) {
                if (m.getPassword() == password.hashCode())
                    return true;
                else
                    throw new Exception("E-Mail oder Passwort falsch!");
            }
        }

        throw new Exception("E-Mail oder Passwort falsch!");

    }

    public ArrayList<IGast> getGaeste() {
        return null;
    }

    public String getMitgliedsNamen(String MitgliedsID) throws Exception{

         IMitglied mitglied;

         try{
             mitglied = fetch(MitgliedsID);
         }
         catch(NoSuchObjectException e){
             throw new RuntimeException(e);
         }

         return mitglied.getVorname() + " " + mitglied.getNachname();
    }

}
