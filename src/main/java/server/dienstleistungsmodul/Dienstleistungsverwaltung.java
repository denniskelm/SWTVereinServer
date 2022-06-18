package server.dienstleistungsmodul;

import server.VereinssoftwareServer;
import server.db.DienstleistungsDB;

import server.db.RollenDB;
import server.dienstleistungsmodul.*;
import server.users.Mitglied;
import shared.communication.IDienstleistungsverwaltung;

import javax.naming.NoPermissionException;
import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Raphael Kleebaum
 * @author Jonny Schlutter
 * @author Gabriel Kleebaum
 * @author Mhd Esmail Kanaan
 * @author Ole Adelmann
 * @author Bastian Reichert
 * @author Dennis Kelm
*/

public class Dienstleistungsverwaltung implements IDienstleistungsverwaltung {

//    private LocalDateTime now = LocalDateTime.now();
    private ArrayList<Dienstleistungsangebot> angebote;
    private ArrayList<Dienstleistungsgesuch> gesuche;
    private final DienstleistungsDB dlDB;
    public ArrayList<String> aidliste;//Angebote
    public ArrayList<String> gidliste;//Gesuche

    public ArrayList<Dienstleistungsangebot> getAngeboteArrayList() {
        return angebote;
    }

    public ArrayList<Dienstleistungsgesuch> getGesucheArrayList() {
        return gesuche;
    }

    public ArrayList<String> getAidliste() {
        return aidliste;
    }

    public DienstleistungsDB getDlDB() {
        return dlDB;
    }

    public ArrayList<String> getGidliste() {
        return gidliste;
    }

    public void setGesuche(ArrayList<Dienstleistungsgesuch> gesuche) {
        this.gesuche = gesuche;
    }

    public void setAngebote(ArrayList<Dienstleistungsangebot> angebote) {
        this.angebote = angebote;
    }

    public void setAidliste(ArrayList<String> aidliste) {
        this.aidliste = aidliste;
    }

    public void setGidliste(ArrayList<String> gidliste) {
        this.gidliste = gidliste;
    }

    public void reset() {
        angebote = new ArrayList<>();
        gesuche = new ArrayList<>();
        aidliste = new ArrayList<>();
        gidliste = new ArrayList<>();


        dlDB.reset();

        createIdListen();
        System.out.println("Dienstleistungsverwaltung zurueckgesetzt.");

    }

    // Für die Gui von Nöten
    public Object[] getAngeboteInformationen(String angebotID) throws NoSuchObjectException {

        Dienstleistungsangebot a = fetchAngebot(angebotID);
        Object[] info = new Object[8];
        info[0] = a.getTitel();
        info[1] = a.getBeschreibung();
        info[2] = a.getKategorie();
        info[3] = a.getTime1();
        info[4] = a.getTime2();
        info[5] = a.getPersonenID();
        info[6] = angebotID;
        info[7] = a.getImageUrl();
        return info;
    }

    // Für die Gui von Nöten
    public Object[] getGesucheInformationen(String gesucheID) throws NoSuchObjectException {
        Dienstleistungsgesuch g= fetchGesuch(gesucheID);
        Object[] info = new Object[6];
        info[0] = g.getTitel();
        info[1] = g.getBeschreibung();
        info[2] = g.getKategorie();
        info[3] = g.getGesuch_ID();
        info[4] = g.getSuchender();
        info[5] = g.getImageUrl();
        return info;
    }


    public int anzahlAngebote(){
        return angebote.size();
    }

    public int anzahlGesuche(){
        return gesuche.size();
    }

    //Konstruktor der Klasse
    public Dienstleistungsverwaltung() {
        try {
            dlDB = new DienstleistungsDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



        angebote = dlDB.getAngeboteArrayList();
        gesuche = dlDB.getGesucheArrayList();
        aidliste = new ArrayList<>(); //Anfrage ID
        gidliste = new ArrayList<>(); //Gesuche ID

        //Liste mit verfuegbaren IDs für Gesuche fuellen
        createIdListen();

        try {
            //gesuchErstellen("Rasen maehen", "Ich brauche jemanden, der meine Wiese maeht", "Gartenarbeiten", "https://www.gartentipps.com/wp-content/uploads/2013/08/spindelmaeher-1.jpg", "1");
            //gesuchErstellen("Pinsel", "Ich möchte meinen Zaun streichen und brauche dafür einen Pinsel.", "Werkzeuge", "https://cdn.hornbach.de/data/shop/D04/001/780/491/350/564/DV_8_8447763_02_4c_DE_20180410170240.jpg", "2");

            //angebotErstellen("Gartenschere", "Braucht jemand eine Schere?", "Werkzeuge", LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(3), "https://bilder.gartenpaul.de/item/images/456/full/456-IMG-0840.JPG", "3");
            //angebotErstellen("Vogelhaus", "Gebe mein Vogelhäuschen ab.", "Sonstiges", LocalDateTime.now().minusDays(4), LocalDateTime.now().plusDays(1), "https://master.opitec.com/out/pictures/master/product/1/101290-01-z.jpg", "1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public void createIdListen(){  //TODO überpüfen, ob diese schon in der von der db geholten angebot/gesuch-menge sind
        int anzahl=0;
        while (anzahl<5000) {
            if (anzahl < 9)
                gidliste.add("dg0000" + (anzahl + 1)); // klammern hinzugefügt damit test richtig
            else if (anzahl < 99)
                gidliste.add("dg000" + (anzahl + 1));
            else if (anzahl < 999)
                gidliste.add("dg00" + (anzahl + 1));
            else if (anzahl < 9999)
                gidliste.add("dg0" + (anzahl + 1));
            else if (anzahl < 50000)
                gidliste.add("dg" + (anzahl + 1));
            anzahl++;
        }

        //Liste mit verfuegbaren IDs für Angebote fuellen
        anzahl=0;
        while (anzahl<5000) {
            if (anzahl < 9)
                aidliste.add("da0000" + (anzahl + 1)); // klammern hinzugefügt damit test richtig
            else if (anzahl < 99)
                aidliste.add("da000" + (anzahl + 1));
            else if (anzahl < 999)
                aidliste.add("da00" + (anzahl + 1));
            else if (anzahl < 9999)
                aidliste.add("da0" + (anzahl + 1));
            else if (anzahl < 50000)
                aidliste.add("da" + (anzahl + 1));
            anzahl++;
        }
        for (Dienstleistungsangebot da : angebote) {
            for (String element : aidliste){
                if (da.getAngebots_ID().equals(element)) {
                    aidliste.remove(element);
                    break;
                }
            }
        }
        for (Dienstleistungsgesuch dg : gesuche) {
            for (String element : gidliste) {
                if (dg.getGesuch_ID().equals(element)) {
                    gidliste.remove(element);
                    break;
                }
            }
        }
    }

    public ArrayList<Dienstleistungsangebot> getAngebote() {
        return angebote;
    }

    public ArrayList<Dienstleistungsgesuch> getGesuche() {
        return gesuche;
    }

    public String gesuchErstellen(String titel, String beschreibung, String kategorie, String imageUrl, String ersteller) throws Exception {
        if (gesuche.size()>=50000)throw new ArrayIndexOutOfBoundsException("Es gibt bereits 50000 Gesuche.");
        String gesuch_ID;
        gesuch_ID=this.gidliste.get(0);
        this.gidliste.remove(0);

        System.out.println(ersteller);
        Dienstleistungsgesuch g = new Dienstleistungsgesuch(gesuch_ID, titel, beschreibung, kategorie, imageUrl, ersteller);
        gesuche.add(g);

        dlDB.gesuchErstellen(g);

        return gesuch_ID;
    }

    public String angebotErstellen(String titel, String beschreibung, String kategorie, LocalDateTime ab, LocalDateTime bis ,String imageUrl, String personen_ID) throws Exception {
        if (angebote.size()>=50000)throw new ArrayIndexOutOfBoundsException("Es gibt bereits 50000 Angebote.");
        String angebot_ID;
        angebot_ID=this.aidliste.get(0);
        this.aidliste.remove(0);

        Dienstleistungsangebot a = new Dienstleistungsangebot(angebot_ID, titel, beschreibung, kategorie, ab, bis,imageUrl, personen_ID);
        angebote.add(a);

        dlDB.angebotErstellen(a);


        System.out.println(titel + " " + beschreibung + kategorie + ab + bis + personen_ID);

        return angebot_ID;
    }

    public boolean gesuchLoeschen(String gesuch_ID) {

        try{
            Dienstleistungsgesuch g=fetchGesuch(gesuch_ID);
            VereinssoftwareServer.rollenverwaltung.fetch(g.getSuchender()).getAnfragenliste().removeAlleGAnfragen(g);
            gesuche.remove(g);
        }
        catch (NoSuchObjectException e){
            return false;
        }
        gidliste.add(gesuch_ID);
        dlDB.gesuchLoeschen(gesuch_ID);
        return true;
    }

    public boolean angebotLoeschen(String angebots_ID) {
        try{
            Dienstleistungsangebot a=fetchAngebot(angebots_ID);
            VereinssoftwareServer.rollenverwaltung.fetch(a.getPersonenID()).getAnfragenliste().removeAlleAAnfragen(a);
            angebote.remove(fetchAngebot(angebots_ID));
        }
        catch (NoSuchObjectException e){
            return false;
        }
        aidliste.add(angebots_ID);
        dlDB.angebotLoeschen(angebots_ID);
        return true;
    }

    public void gesuchAendern(String gesuchsID, Dienstleistungsgesuchdaten attr, Object wert) {
        try {
            Dienstleistungsgesuch a = fetchGesuch(gesuchsID);
                    switch (attr) {
                        case GESUCH_ID -> a.setGesuch_ID(wert.toString());
                        case TITEL -> a.setTitel(wert.toString());
                        case BESCHREIBUNG -> a.setBeschreibung(wert.toString());
                        case KATEGORIE -> a.setKategorie(wert.toString());
                        case SUCHENDER_ID -> a.setSuchender_ID((wert.toString()));
                        case URL -> a.setImageUrl(wert.toString());
                    }
        }
        catch(NoSuchObjectException e){
            throw new RuntimeException();
        }


        dlDB.gesuchAendern(gesuchsID, attr, wert);
    }
    public void angebotAendern(String angebotsID, Dienstleistungsangebotdaten attr, Object wert) {
        try {
            Dienstleistungsangebot a = fetchAngebot(angebotsID);
            switch (attr) {
                case ANGEBOTS_ID -> a.setAngebots_ID(wert.toString());
                case TITEL -> a.setTitel(wert.toString());
                case BESCHREIBUNG -> a.setBeschreibung(wert.toString());
                case KATEGORIE -> a.setKategorie(wert.toString());
                case AB -> a.setAb((LocalDateTime) wert);
                case BIS -> a.setBis((LocalDateTime) wert);
                case PERSONEN_ID -> a.setPersonenID(wert.toString());
                case URL -> a.setImageUrl(wert.toString());
            }
        }
        catch(NoSuchObjectException e){
            throw new RuntimeException();
        }

        dlDB.angebotAendern(angebotsID, attr, wert);
    }


    public void gesuchAnnehmen(String gesuchID, String erstellerID, String nutzerID, int stunden) throws Exception{

        server.users.Rollenverwaltung r= VereinssoftwareServer.rollenverwaltung;
        //Dienstleistungsgesuch gesuch = fetchGesuch(gesuchID);

        Mitglied ersteller=r.fetch(erstellerID);

        if (ersteller.isGesperrt()) throw new NoPermissionException("Mitglied ist gesperrt.");
        //Mitglied nutzer=r.fetch(nutzerID);
        Anfragenliste l= ersteller.getAnfragenliste();
        l.addgAnfrage(nutzerID, gesuchID ,stunden);

        dlDB.gesuchAnnhemen(gesuchID, erstellerID, nutzerID ,stunden);
    }

    // Gibt das zugehörige Angebot zu der gegebenen AngebotsID
    public Dienstleistungsangebot fetchAngebot(String angebotID) throws NoSuchObjectException {
        System.out.println("fetchAngebot startet hier mit ID " + angebotID);

        for (Dienstleistungsangebot da : angebote) {
            System.out.println("Angebot: " + da.getAngebots_ID() + " angeschaut");
            if (da.getAngebots_ID().equals(angebotID))
                return da;
        }

        throw new NoSuchObjectException("Angebot mit ID: " + angebotID + " nicht vorhanden.");
    }

    // Gibt das zugehörige Gesuch zu der gegebenen GesuchsID
    public Dienstleistungsgesuch fetchGesuch(String gID) throws NoSuchObjectException {

        for (Dienstleistungsgesuch ds : gesuche) {
            if (ds.getGesuch_ID().equals(gID))
                return ds;
        }

        throw new NoSuchObjectException("Angebot mit ID: " + gID + " nicht vorhanden.");
    }

    // Für die Gui
    public Object[][] omniAngebotDaten() throws NoSuchObjectException {
        Object[][] aliste = new Object[angebote.size()][8];

        for(int i = 0; i < angebote.size(); i++) {
            aliste[i] = getAngeboteInformationen(angebote.get(i).getAngebots_ID());
        }

        return aliste;
    }

    // Für die Gui
    public Object[][] omniGesuchDaten() throws NoSuchObjectException{
        Object[][] gliste = new Object[gesuche.size()][6];

        for (int i = 0; i < gesuche.size(); i++)
            gliste[i] = getGesucheInformationen(gesuche.get(i).getGesuch_ID());

        return gliste;
    }
    public void angebotAnnehmen(String angebotID, String erstellerID, String nutzerID, int stunden) throws Exception{

        server.users.Rollenverwaltung r= VereinssoftwareServer.rollenverwaltung;
        //Dienstleistungsangebot angebot = fetchAngebot(angebotID);


        Mitglied ersteller=r.fetch(erstellerID);
        if (ersteller.isGesperrt()) throw new NoPermissionException("Mitglied ist gesperrt.");
        //Mitglied nutzer=r.fetch(nutzerID);
        Anfragenliste l= ersteller.getAnfragenliste();
        l.addaAnfrage(nutzerID, angebotID, stunden);//nutzer ist der, welcher die Anfrage stellt

        dlDB.angebotAnnehmen(angebotID, erstellerID, nutzerID, stunden);
    }



}
