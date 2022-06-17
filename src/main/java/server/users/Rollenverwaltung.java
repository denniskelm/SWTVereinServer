package server.users;

/*
@author
Raphael Kleebaum
Jonny Schlutter
Ole Björn Adelmann
*/

import server.VereinssoftwareServer;
import server.geraetemodul.Mahnung;
import server.db.RollenDB;
import shared.communication.*;

import java.rmi.NoSuchObjectException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Rollenverwaltung implements IRollenverwaltung {

     private static ArrayList<Gast> gaeste;
     private static ArrayList<Mitglied> mitglieder;
     private static ArrayList<Mitarbeiter> mitarbeiter;
     private static ArrayList<Vorsitz> vorsitze;
     private static ArrayList<Mahnung> mahnungen;
     private int IdCounter;
     private final RollenDB rDB;


     public Rollenverwaltung(){
         try {
             rDB = new RollenDB();

             gaeste = rDB.getGaeste();
             mitglieder = rDB.getMitglieder();
             mitarbeiter = rDB.getMitarbeiter();
             vorsitze = rDB.getVorsitze();
             mahnungen = rDB.getMahnungenListe();
             IdCounter = rDB.getIdCounter();
         } catch (SQLException e) {
             System.err.println("Verbindung zu Datenbank konnte nicht hergestellt werden!");
             throw new RuntimeException(e);
         }

         //mitgliedHinzufuegen("Ehrenmann", "Stefan", "stefan.ehrenmann@t-online.de", "12345678", "Huglfingstr. 27", "M4657", "110", false, LocalDateTime.now());
         //mitgliedHinzufuegen("Tran", "Huy", "huy@email.de", "1234", "Musterstr. 1", "ABC", "123", true, LocalDateTime.now().minusDays(3));
         //mitgliedHinzufuegen("Kelm", "Dennis", "dennis@email.de", "2345", "Teststr. 4", "DEF", "234", false, LocalDateTime.now().minusDays(20));
     }

     public Object[] gastHinzufuegen(String nachname, String vorname, String email, String password, String anschrift, String mitgliedsnr, String telefonnummer, boolean spender) {
         if (gaeste.size() >= 50000) throw new ArrayIndexOutOfBoundsException();

         //naechste ID generieren
         IdCounter++;
         String personenID = Long.toString(IdCounter);

         Gast gast = new Gast(personenID, nachname, vorname, email, password, anschrift, mitgliedsnr, telefonnummer, spender);
         gaeste.add(gast);
         rDB.gastHinzufuegen(gast);
         System.out.println("Gast mit ID " +  personenID + " hinzugefuegt. Anzahl Gaeste: " +  gaeste.size());

         Object[] result = new Object[2];
         result[0] = personenID;
         result[1] = Rolle.GAST;

         return result;
     }
     
     public void mitgliedHinzufuegen(String nachname, String vorname, String email, String password, String anschrift,
                                     String mitgliedsnr, String telefonnummer, boolean spender/*, Profilseite profilseite */,
                                     LocalDateTime mitglied_seit) {
         if (mitglieder.size() >= 50000) throw new ArrayIndexOutOfBoundsException();

         //naechste ID generieren
         IdCounter++;
         String personenID = Long.toString(IdCounter);

         Mitglied m = new Mitglied(personenID, nachname, vorname, email, password, anschrift, mitgliedsnr, telefonnummer, spender, mitglied_seit);
         mitglieder.add(m);

         rDB.MitgliedHinzufuegen(m);
     }

     // Gibt das passende Mitglied anhand der MitgliedsID zurück
    public Mitglied fetch(String mitgliederID) throws NoSuchObjectException {
        for (Mitglied m : vorsitze) {
            if (m.getPersonenID().equals(mitgliederID)) return m;
        }

        for (Mitglied m : mitarbeiter) {
            if (m.getPersonenID().equals(mitgliederID)) return m;
        }

        for (Mitglied m : mitglieder) {
            if (m.getPersonenID().equals(mitgliederID)) return m;
        }

        throw new NoSuchObjectException("Person mit ID: " + mitgliederID + " nicht vorhanden.");
    }

    // Gibt den passenden Gast anhand der MitgliedsID zurück
    public Gast fetchGaeste(String personenID) throws NoSuchObjectException{
        for (Gast g : gaeste) {
            if (g.getPersonenID().equals(personenID)) return g;
        }
        throw new NoSuchObjectException("Person mit ID: " + personenID + " nicht vorhanden.");
    }

    public  ArrayList<Mitglied> getMitglieder() {
        return mitglieder;
    }


    public ArrayList<Mitarbeiter> getMitarbeiter() {
        return mitarbeiter;
    }

    public ArrayList<Vorsitz> getVorsitze() {
        return vorsitze;
    }

    public long getIdCounter() {
        return IdCounter;
    }

    public Object[] gastListeAnzeigen() { return gaeste.toArray(); }

    public Object[] mitgliedListeAnzeigen() { return mitglieder.toArray(); }

    public Object[] mitarbeiterListeAnzeigen() { return mitarbeiter.toArray(); }

    public Object[] vorsitzListeAnzeigen() { return vorsitze.toArray(); }

    public void rolleAendern(String mitgliedsID, Rolle rolle) throws Exception {
        Mitglied mitgliedInAlterRolle;
        Gast GastInAlterRolle;
        LocalDateTime mitglied_seit;

        rDB.rolleAendern(mitgliedsID, rolle);

        // Es wird das passende Mitglied zur Id gesucht
        try {
            mitgliedInAlterRolle = fetch(mitgliedsID);
        } catch (NoSuchObjectException e) {
            try {
                // Da die PersonenID unter den Mitgliedern nicht gefunden wurde, wird bei den Gaesten weiter gesucht
                GastInAlterRolle = fetchGaeste(mitgliedsID);
                mitglied_seit = LocalDateTime.now();

                if(GastInAlterRolle.getClass() == rolle.getKlasse()) {
                    throw new Exception("Der Nutzer hat diese Rolle bereits.");
                }
                rolleAendernSwitch(GastInAlterRolle, rolle, mitglied_seit);
                return;

            } catch (NoSuchObjectException f) {
                // Wirft Exception falls die Person nicht gefunden wurde
                throw new RuntimeException(f);
            }
        }

        if (mitgliedInAlterRolle.getClass() == rolle.getKlasse())
            throw new Exception("Der Nutzer hat diese Rolle bereits.");

        mitglied_seit = mitgliedInAlterRolle.getMitgliedSeit();

        rolleAendernSwitch(mitgliedInAlterRolle, rolle, mitglied_seit);
    }

    // Methode löscht die Person in der alten Rolle und erstellt eine Instanz der neuen Rolle der Person
    private void rolleAendernSwitch(Gast gast, Rolle rolle, LocalDateTime mitglied_seit) {
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
                        mitglied_seit);

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
                        mitglied_seit);

                vorsitze.add((Vorsitz) g);
                break;
        }

        nutzerAusAlterListeEntfernen(gast);
    }

    public void nutzereintragAendern(String mitgliedsID, Personendaten attr, String wert) throws NoSuchObjectException {
        try {
            (fetch(mitgliedsID)).datenVerwalten(attr, wert);
        } catch (NoSuchObjectException e) {
                (fetchGaeste(mitgliedsID)).datenVerwalten(attr, wert);
        }

        rDB.nutzerEintragAendern(mitgliedsID, attr, wert);

    }

    public Object[] mahnungsverwaltungAnzeigen() { return mahnungen.toArray(); }

    private void nutzerAusAlterListeEntfernen(Gast mitglied) {
         //überprüft ob der Nutzer der Rolle Gast angehört und löscht dann diesen
        if (mitglied instanceof Gast) {
            for (int i = 0; i < gaeste.size(); i++) {
                if (gaeste.get(i).getPersonenID().equals(mitglied.getPersonenID())) {
                    gaeste.remove(i);
                    break;
                }
            }
            //überprüft ob der Nutzer der Rolle Mitglied angehört und löscht dann diesen
        } else if (mitglied instanceof Mitglied) {
            for (int i = 0; i < mitglieder.size(); i++) {
                if (mitglieder.get(i).getPersonenID().equals(mitglied.getPersonenID())) {
                    mitglieder.remove(i);
                    break;
                }
            }
            //überprüft ob der Nutzer der Rolle Mitarbeiter angehört und löscht dann diesen
        } else if (mitglied instanceof Mitarbeiter) {
            for (int i = 0; i < mitarbeiter.size(); i++) {
                if (mitarbeiter.get(i).getPersonenID().equals(mitglied.getPersonenID())) {
                    mitarbeiter.remove(i);
                    break;
                }
            }
            //überprüft ob der Nutzer der Rolle Vorsitz angehört und löscht dann diesen
        } else if (mitglied instanceof Vorsitz) {
            for (int i = 0; i < vorsitze.size(); i++) {
                if (vorsitze.get(i).getPersonenID().equals(mitglied.getPersonenID())) {
                    vorsitze.remove(i);
                    break;
                }
            }
        }
    }

    //TODO Sperrung
    public Object[] login(String email, String password) throws Exception {
        Object[] result = new Object[2];

        System.out.println("Es meldet sich an: " + email + password);

        for (Mitglied m : vorsitze) {
            if (m.getEmail().equals(email)) {
                if (m.getPassword() == password.hashCode()) {
                    result[0] = m.getPersonenID();
                    result[1] = Rolle.VORSITZ;

                    System.out.println("Antwort der Anmeldung: " + result[0] + " | " + result[1]);

                    return result;
                }
                else
                    throw new Exception("E-Mail oder Passwort falsch!");
            }
        }

        for (Mitglied m : mitarbeiter) {
            if (m.getEmail().equals(email)) {
                if (m.getPassword() == password.hashCode()) {
                    result = new Object[2];
                    result[0] = m.getPersonenID();
                    result[1] = Rolle.MITARBEITER;

                    System.out.println("Antwort der Anmeldung: " + result[0] + " | " + result[1]);

                    return result;
                }
                else
                    throw new Exception("E-Mail oder Passwort falsch!");
            }
        }

        for (Mitglied m : mitglieder) {
            if (m.getEmail().equals(email)) {
                if (m.getPassword() == password.hashCode()) {
                    result = new Object[2];
                    result[0] = m.getPersonenID();
                    result[1] = Rolle.MITGLIED;

                    System.out.println("Antwort der Anmeldung: " + result[0] + " | " + result[1]);

                    return result;
                }
                else
                    throw new Exception("E-Mail oder Passwort falsch!");
            }
        }

        for (Gast m : gaeste) {
            if (m.getEmail().equals(email)) {
                if (m.getPassword() == password.hashCode()) {
                    result = new Object[2];
                    result[0] = m.getPersonenID();
                    result[1] = Rolle.GAST;

                    System.out.println("Antwort der Anmeldung: " + result[0] + " | " + result[1]);

                    return result;
                }
                else
                    throw new Exception("E-Mail oder Passwort falsch!");
            }
        }

        throw new Exception("E-Mail oder Passwort falsch!");

    }

    public void nutzerEntfernen(String personenID) {

        rDB.nutzerEntfernen(personenID);

         for (int i = 0; i < vorsitze.size(); i++) {
             if (vorsitze.get(i).getPersonenID().equals(personenID)) {
                 vorsitze.remove(i);
                 return;
             }
         }

        for (int i = 0; i < mitarbeiter.size(); i++) {
            if (mitarbeiter.get(i).getPersonenID().equals(personenID)) {
                mitarbeiter.remove(i);
                return;
            }
        }

        for (int i = 0; i < mitglieder.size(); i++) {
            if (mitglieder.get(i).getPersonenID().equals(personenID)) {
                mitglieder.remove(i);
                return;
            }
        }

        for (int i = 0; i < gaeste.size(); i++) {
            if (gaeste.get(i).getPersonenID().equals(personenID)) {
                gaeste.remove(i);
                return;
            }
        }

    }

    public ArrayList<Gast> getGaeste() {
        return gaeste;
    }

    public String getMitgliedsNamen(String MitgliedsID) {
         Mitglied mitglied;

         try{
             mitglied = fetch(MitgliedsID);
         }
         catch(NoSuchObjectException e){
             throw new RuntimeException(e);
         }

         return mitglied.getVorname() + " " + mitglied.getNachname();
    }

    //@author Dennis Kelm
    public String getMitgliedsMail(String MitgliedsID) {
        Mitglied mitglied;

        try{
            mitglied = fetch(MitgliedsID);
        }
        catch(NoSuchObjectException e){
            throw new RuntimeException(e);
        }

        return mitglied.getEmail();
    }

    public int getStundenzahl(String mitgliedsID){

        try {
            return fetch(mitgliedsID).getStundenkonto();
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }
    }

    //setzt die gesamte Rollenverwaltung zurück
    public void reset() {
        gaeste = new ArrayList<>();
        mitglieder = new ArrayList<>();
        mitarbeiter = new ArrayList<>();
        vorsitze = new ArrayList<>();
        rDB.reset();
        IdCounter = 0;
        System.out.println("Rollenverwaltung zurueckgesetzt.");
    }


    public Mahnung fetchMahnung(String mahnungsID) throws NoSuchObjectException{
         for(Mahnung m : mahnungen){
             if(m.getMahnungsID().equals(mahnungsID))
                 return m;
         }
         throw new NoSuchObjectException("Mahnung mit solcher ID nicht gefunden.");
    }


    public void mahnungErstellen(String mitgliedsID, String grund, LocalDateTime verfallsdatum) throws NoSuchObjectException {
        String mahnungsID ="m" + mahnungen.size()+1;
        Mahnung m = new Mahnung(mahnungsID, mitgliedsID, grund, verfallsdatum);
        mahnungen.add(m);
        rDB.mahnungErstellen(m);
        Mitglied nutzer = fetch(mitgliedsID);

        if(anzahlMahnungenVonNutzer(mitgliedsID) >= 3){
            nutzer.setIst_gesperrt(true);
            rDB.nutzerEintragAendern(mitgliedsID, Personendaten.IST_GESPERRT, "Y");

            Mitglied mit=VereinssoftwareServer.rollenverwaltung.fetch(mitgliedsID);
            mit.getAnfragenliste().reset();
        }

    }

    public void mahnungLoeschen(String mahnungsID){
        try {
            Mahnung m = fetchMahnung(mahnungsID);
            mahnungen.remove(m);
            rDB.mahnungLoeschen(mahnungsID);
            Mitglied nutzer = fetch(m.getMitgliedsID());

            if(anzahlMahnungenVonNutzer(m.getMitgliedsID()) < 3){
                nutzer.setIst_gesperrt(false);
                rDB.nutzerEintragAendern(m.getMitgliedsID(), Personendaten.IST_GESPERRT, "N");
            }

        } catch (NoSuchObjectException e) {
            e.printStackTrace();
        }

    }

    public int anzahlMahnungenVonNutzer(String mitgliedsID) {
        int anzahlMahnungen = 0;

        for (Mahnung m : mahnungen) {
            if (m.getMitgliedsID().equals(mitgliedsID))
                anzahlMahnungen++;
        }
        return anzahlMahnungen;
    }


    public Object[][] mahnungenVomNutzer(String mitgliedsID) {
         Object[][] nutzerMahnungen = new Mahnung[anzahlMahnungenVonNutzer(mitgliedsID)][4];
         int k = 0;

         for(int i = 0; i < anzahlMahnungenVonNutzer(mitgliedsID); i++){
             while(k < mahnungen.size()){
                 if(mahnungen.get(k).getMitgliedsID().equals(mitgliedsID)){
                     nutzerMahnungen[i] = mahnungAnzeigen(mahnungen.get(k).getMahnungsID());
                     break;
                 } else
                     k++;
             }
         }
         return nutzerMahnungen;
    }

    public Object[] mahnungAnzeigen(String mahnungsID){
        try {
            Mahnung mahnung = fetchMahnung(mahnungsID);

            Object[] array = new Object[4];

            array[0] = mahnung.getMahnungsID();
            array[1] = mahnung.getMitgliedsID();
            array[2] = mahnung.getGrund();
            array[3] = mahnung.getVerfallsdatum();

            return array;

        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

    }

}
