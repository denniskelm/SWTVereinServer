package server.users;

/*
@author
Raphael Kleebaum
Jonny Schlutter
Ole Björn Adelmann
*/

/* Was macht diese Klasse ?
   Die Rollenverwaltungsklasse zeigt  alle Nutzer der Software an.
   Außerdem gibt es Methoden, um neue Nutzer zu erstellen, Nutzer eine andere Rolle zu geben oder auch um Nutzer eine
   Mahnung zu vergeben, dies kann allerdings nur von Mitarbeitern oder Vostandsvorsitzenden getan werden.
 */

import server.VereinssoftwareServer;
import server.geraetemodul.Mahnung;
import server.db.RollenDB;
import shared.communication.*;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
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

             listenBereinigen();

         } catch (SQLException e) {
             System.err.println("Verbindung zu Datenbank konnte nicht hergestellt werden!");
             throw new RuntimeException(e);
         }

     }
     // Fügt einen Gast hinzu
     public Object[] gastHinzufuegen(String nachname, String vorname, String email, String password, String anschrift, String mitgliedsnr, String telefonnummer, boolean spender) {
         if (gaeste.size() >= 50000) throw new ArrayIndexOutOfBoundsException();

         //naechste ID generieren
         String personenID;
         IdCounter++;
         if (IdCounter < 10) personenID = "p0000" + (IdCounter);
         else if (IdCounter < 100) personenID = "p000" + (IdCounter);
         else if (IdCounter < 1000) personenID = "p00" + (IdCounter);
         else if (IdCounter < 10000) personenID = "p0" + (IdCounter);
         else personenID = "p" + (IdCounter);

         //personenID = String.valueOf(IdCounter);

         Gast gast = new Gast(personenID, nachname, vorname, email, password, anschrift, mitgliedsnr, telefonnummer, spender);
         gaeste.add(gast);
         rDB.gastHinzufuegen(gast);
         System.out.println("Gast mit ID " +  personenID + " hinzugefuegt. Anzahl Gaeste: " +  gaeste.size());

         Object[] result = new Object[2];
         result[0] = personenID;
         result[1] = Rolle.GAST;

         return result;
     }
     // Fügt ein Mitglied hinzu
     public void mitgliedHinzufuegen(String nachname, String vorname, String email, String password, String anschrift,
                                     String mitgliedsnr, String telefonnummer, boolean spender/*, Profilseite profilseite */,
                                     LocalDateTime mitglied_seit) {
         if (mitglieder.size() >= 50000) throw new ArrayIndexOutOfBoundsException();

         //naechste ID generieren
         IdCounter++;
         String personenID;

         if (IdCounter < 10) personenID = "p0000" + (IdCounter);
         else if (IdCounter < 100) personenID = "p000" + (IdCounter);
         else if (IdCounter < 1000) personenID = "p00" + (IdCounter);
         else if (IdCounter < 10000) personenID = "p0" + (IdCounter);
         else personenID = "p" + (IdCounter);

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

    public Rolle fetchRolle(String mitgliedsID){
        try {
            Gast gast = fetchGaeste(mitgliedsID);
            return Rolle.GAST;
        } catch (NoSuchObjectException e) {
            try {
                Mitglied mitglied = fetch(mitgliedsID);

                if(mitglied instanceof Mitglied)
                    return Rolle.MITGLIED;

                else if(mitglied instanceof Mitarbeiter)
                    return Rolle.MITARBEITER;

                else
                    return Rolle.VORSITZ;
            } catch (NoSuchObjectException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    public long getIdCounter() {
        return IdCounter;
    }

    public Object[] gastDaten(String mitgliedsID) {
        try {
            Gast gast = fetchGaeste(mitgliedsID);
            Object[] gastDaten = new Object[8];

            gastDaten[0] = gast.getPersonenID();
            gastDaten[1] = gast.getVorname();
            gastDaten[2] = gast.getNachname();
            gastDaten[3] = gast.getEmail();
            gastDaten[4] = gast.getAnschrift();
            gastDaten[5] = gast.getMitgliedsNr();
            gastDaten[6] = gast.getTelefonNr();
            gastDaten[7] = gast.getSpenderStatus();

            return gastDaten;

        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }
    }

    public Object[][] gaesteDaten() {
        Object[][] gaesteDaten = new Object[gaeste.size()][8];
        for(int i = 0; i < gaeste.size(); i++){
            gaesteDaten[i] = gastDaten(gaeste.get(i).getPersonenID());
        }
        return gaesteDaten;
    }

    public Object[] mitgliedDaten(String mitgliedsID){
        try {
            Mitglied mitglied = fetch(mitgliedsID);
            Object[] mitgliedDaten = new Object[11];

            mitgliedDaten[0] = mitglied.getPersonenID();
            mitgliedDaten[1] = mitglied.getVorname();
            mitgliedDaten[2] = mitglied.getNachname();
            mitgliedDaten[3] = mitglied.getEmail();
            mitgliedDaten[4] = mitglied.getAnschrift();
            mitgliedDaten[5] = mitglied.getMitgliedsNr();
            mitgliedDaten[6] = mitglied.getTelefonNr();
            mitgliedDaten[7] = mitglied.getSpenderStatus();
            mitgliedDaten[8] = mitglied.getStundenkonto();
            mitgliedDaten[9] = mitglied.isGesperrt();
            mitgliedDaten[10] = mitglied.getMitgliedSeit();

            return mitgliedDaten;

        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean istSpender(String nutzerId) {
         return rDB.istSpender(nutzerId);
    }


    public Object[][] mitgliederDaten() {
         Object[][] mitgliederDaten = new Object[mitglieder.size()][11];
         for(int i = 0; i < mitglieder.size(); i++){
             mitgliederDaten[i] = mitgliedDaten(mitglieder.get(i).getPersonenID());
         }
         return mitgliederDaten;
    }


    public Object[][] mitarbeiterDaten() {
        Object[][] mitarbeiterDaten = new Object[mitarbeiter.size()][11];

        for(int i = 0; i < mitarbeiter.size(); i++){
            mitarbeiterDaten[i] = mitgliedDaten(mitarbeiter.get(i).getPersonenID());
        }
        return mitarbeiterDaten;
     }

    public Object[][] vorsitzDaten() {
        Object[][] vorsitzeDaten = new Object[vorsitze.size()][11];
        for(int i = 0; i < vorsitze.size(); i++){
            vorsitzeDaten[i] = mitgliedDaten(vorsitze.get(i).getPersonenID());
        }
        return vorsitzeDaten;
    }

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
        nutzerAusAlterListeEntfernen(gast.getPersonenID(), fetchRolle(gast.getPersonenID()));
    }

    public void nutzereintragAendern(String mitgliedsID, Personendaten attr, String wert) throws NoSuchObjectException {
        try {
            (fetch(mitgliedsID)).datenVerwalten(attr, wert);
        } catch (NoSuchObjectException e) {
                (fetchGaeste(mitgliedsID)).datenVerwalten(attr, wert);
        }

        rDB.nutzerEintragAendern(mitgliedsID, attr, wert);

    }

    public void nutzerAusAlterListeEntfernen(String mitgliedsID, Rolle rolle) {
         //überprüft ob der Nutzer der Rolle Gast angehört und löscht dann diesen
        if (rolle == Rolle.GAST) {
            for (int i = 0; i < gaeste.size(); i++) {
                if (gaeste.get(i).getPersonenID().equals(mitgliedsID)) {
                    gaeste.remove(i);
                    break;
                }
            }
            //überprüft ob der Nutzer der Rolle Mitglied angehört und löscht dann diesen
        } else if (rolle == Rolle.MITGLIED) {
            for (int i = 0; i < mitglieder.size(); i++) {
                if (mitglieder.get(i).getPersonenID().equals(mitgliedsID)) {
                    mitglieder.remove(i);
                    break;
                }
            }
            //überprüft ob der Nutzer der Rolle Mitarbeiter angehört und löscht dann diesen
        } else if (rolle == Rolle.MITARBEITER) {
            for (int i = 0; i < mitarbeiter.size(); i++) {
                if (mitarbeiter.get(i).getPersonenID().equals(mitgliedsID)) {
                    mitarbeiter.remove(i);
                    break;
                }
            }
            //überprüft ob der Nutzer der Rolle Vorsitz angehört und löscht dann diesen
        } else if (rolle == Rolle.VORSITZ) {
            for (int i = 0; i < vorsitze.size(); i++) {
                if (vorsitze.get(i).getPersonenID().equals(mitgliedsID)) {
                    vorsitze.remove(i);
                    break;
                }
            }
        }
    }

    public Object[] login(String email, String password) throws Exception {
        Object[] result = new Object[2];

        System.out.println("Es meldet sich an: " + email);

        for (Mitglied m : vorsitze) {
            if (m.getEmail().equals(email)) {
                if (m.getPassword() == password.hashCode()) {
                    result[0] = m.getPersonenID();
                    result[1] = Rolle.VORSITZ;

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

                    if(m.isGesperrt())throw new Exception("Du bist gesperrt!!!");
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

                    if(m.isGesperrt())throw new Exception("Du bist gesperrt!!!");
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

                    //if(m.isGesperrt())throw new Exception("Du bist gesperrt!!!");
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
        String mahnungsID = mahnungsIdErstellen();
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

    public String mahnungsIdErstellen() {
         if(mahnungen.size() + 1 < 50000)
             return getString(mahnungen.size() + 1);
         else {
             for(int i = 0; i < mahnungen.size(); i++){
                 if(mahnungen.get(i) == null)
                     getString(i);
             }
         }
         return "wrongID";
    }

    private String getString(int number) {
        if(number < 10)
            return "m0000" + number;
        else if(number < 100)
            return "m000" + number;
        else if(number < 1000)
            return "m00" + number;
        else if(number < 10000)
            return "m0" + number;
        else if(number < 50000)
            return "m" + number;
        return "wrongID";
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

    // Entfernt Gäste aus gaeste, wenn Sie Mitglieder sind etc.
    private void listenBereinigen() {

        for (Vorsitz v : vorsitze) {

            for (int i = 0; i < mitarbeiter.size(); i++) {
                if (mitarbeiter.get(i).getPersonenID().equals(v.getPersonenID())) {
                    mitarbeiter.remove(i);
                    break;
                }
            }

            for (int i = 0; i < mitglieder.size(); i++) {
                if (mitglieder.get(i).getPersonenID().equals(v.getPersonenID())) {
                    mitglieder.remove(i);
                    break;
                }
            }

            for (int i = 0; i < gaeste.size(); i++) {
                if (gaeste.get(i).getPersonenID().equals(v.getPersonenID())) {
                    gaeste.remove(i);
                    break;
                }
            }

        }

        for (Mitarbeiter m : mitarbeiter) {

            for (int i = 0; i < mitglieder.size(); i++) {
                if (mitglieder.get(i).getPersonenID().equals(m.getPersonenID())) {
                    mitglieder.remove(i);
                    break;
                }
            }

            for (int i = 0; i < gaeste.size(); i++) {
                if (gaeste.get(i).getPersonenID().equals(m.getPersonenID())) {
                    gaeste.remove(i);
                    break;
                }
            }

        }

        for (Mitglied m : mitglieder) {

            for (int i = 0; i < gaeste.size(); i++) {
                if (gaeste.get(i).getPersonenID().equals(m.getPersonenID())) {
                    gaeste.remove(i);
                    break;
                }
            }

        }

    }

    public boolean existiertEMail(String email) {

         for (Gast g : gaeste) {
             if (g.getEmail().equals(email))
                 return true;
         }

         for (Mitglied m : mitglieder) {
             if (m.getEmail().equals(email))
                 return true;
         }

         for (Mitarbeiter m : mitarbeiter) {
             if (m.getEmail().equals(email))
                 return true;
         }

         for (Vorsitz v : vorsitze) {
             if (v.getEmail().equals(email))
                 return true;
         }

         return false;
    }

}
