package server.dienstleistungsmodul;

import server.VereinssoftwareServer;
import server.db.DienstleistungsDB;
import server.dienstleistungsmodul.*;
import server.users.Mitglied;
import shared.communication.IDienstleistungsverwaltung;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

/*
@author
//TODO Raphael Kleebaum
Jonny Schlutter
Gabriel Kleebaum
Mhd Esmail Kanaan
//TODO Gia Huy Hans Tran
Ole Björn Adelmann
Bastian Reichert
Dennis Kelm
*/

public class Dienstleistungsverwaltung implements IDienstleistungsverwaltung {

    private ArrayList<Dienstleistungsangebot> angebote;
    private ArrayList<Dienstleistungsgesuch> gesuche;
    private DienstleistungsDB database;

    public ArrayList<Dienstleistungsangebot> getAngeboteArrayList() {
        return angebote;
    }

    public ArrayList<Dienstleistungsgesuch> getGesucheArrayList() {
        return gesuche;
    }

    public ArrayList<String> aidliste;
    public ArrayList<String> gidliste;

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



    //TODO??? - public int howManyAngebote
    //TODO??? - public int howManyGesuche


    public Dienstleistungsverwaltung() {
        try {
            database = new DienstleistungsDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        angebote = database.getAngeboteArrayList();
        gesuche = database.getGesucheArrayList();
        aidliste = new ArrayList<>();
        gidliste = new ArrayList<>();

        //Liste mit verfuegbaren IDs für Gesuche fuellen
        int anzahl=0;
        while (anzahl<500) {
            if (anzahl < 9)  // todo : nach dem testen ist die Rueckgabe nicht wie gewunscht???
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
        while (anzahl<500) {
            if (anzahl < 9)  // todo : nach dem testen ist die Rueckgabe nicht wie gewunscht???
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

        try {
            //gesuchErstellen("Rasen maehen", "Ich brauche jemanden, der meine Wiese maeht", "Gartenarbeiten", "https://www.gartentipps.com/wp-content/uploads/2013/08/spindelmaeher-1.jpg", "1");
            //gesuchErstellen("Pinsel", "Ich möchte meinen Zaun streichen und brauche dafür einen Pinsel.", "Werkzeuge", "https://cdn.hornbach.de/data/shop/D04/001/780/491/350/564/DV_8_8447763_02_4c_DE_20180410170240.jpg", "2");

            //angebotErstellen("Gartenschere", "Braucht jemand eine Schere?", "Werkzeuge", LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(3), "https://bilder.gartenpaul.de/item/images/456/full/456-IMG-0840.JPG", "3");
            //angebotErstellen("Vogelhaus", "Gebe mein Vogelhäuschen ab.", "Sonstiges", LocalDateTime.now().minusDays(4), LocalDateTime.now().plusDays(1), "https://master.opitec.com/out/pictures/master/product/1/101290-01-z.jpg", "1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public ArrayList<Dienstleistungsangebot> getAngebote() {
        return angebote;
    }

    public ArrayList<Dienstleistungsgesuch> getGesuche() {
        return gesuche;
    }

    public String gesuchErstellen(String titel, String beschreibung, String kategorie, String imageUrl, String ersteller) throws Exception {
        String gesuch_ID;
        gesuch_ID=this.gidliste.get(0);
        gesuch_ID=this.gidliste.remove(0);


        /*int anzahl = gesuche.size();
        String gesuch_ID;

        if (anzahl < 9)  //
            gesuch_ID = "dg0000" + (anzahl + 1); // klammern hinzugefügt damit test richtig
        else if (anzahl < 99)
            gesuch_ID = "dg000" + (anzahl + 1);
        else if (anzahl < 999)
            gesuch_ID = "dg00" + (anzahl + 1);
        else if (anzahl < 9999)
            gesuch_ID = "dg0" + (anzahl + 1);
        else if (anzahl < 50000)
            gesuch_ID = "dg" + (anzahl + 1);
        else
            throw new Exception();
        */
        System.out.println(ersteller);
        Dienstleistungsgesuch g = new Dienstleistungsgesuch(gesuch_ID, titel, beschreibung, kategorie, imageUrl, ersteller);
        gesuche.add(g);

        database.gesuchErstellen(g);

        return gesuch_ID;
    }

    public String angebotErstellen(String titel, String beschreibung, String kategorie, LocalDateTime ab, LocalDateTime bis ,String imageUrl, String personen_ID) throws Exception {
        String angebot_ID;
        angebot_ID=this.gidliste.get(0);
        angebot_ID=this.gidliste.remove(0);

        Dienstleistungsangebot a = new Dienstleistungsangebot(angebot_ID, titel, beschreibung, kategorie, ab, bis,imageUrl, personen_ID);
        angebote.add(a);

        database.angebotErstellen(a);


        System.out.println(titel + " " + beschreibung + kategorie + ab + bis + personen_ID);

        return angebot_ID;
    }

    public void gesuchLoeschen(String gesuch_ID) {
        for (Dienstleistungsgesuch g : gesuche){
            if(g.getGesuch_ID().equals(gesuch_ID)) {
                gesuche.remove(g);
                break;
            }
        }

        database.gesuchLoeschen(gesuch_ID);
    }

    public void angebotLoeschen(String angebots_ID) {
        for (Dienstleistungsangebot a : angebote){
            if(a.getAngebots_ID().equals(angebots_ID)) {
                angebote.remove(a);
                break;
            }
        }

        database.angebotLoeschen(angebots_ID);
    }

    public void gesuchAendern(String gesuchsID, Dienstleistungsgesuchdaten attr, Object wert) {
        for (Dienstleistungsgesuch a : gesuche) {
            if (a.getGesuch_ID().equals(gesuchsID)) {
                switch (attr) {
                    case GESUCH_ID -> a.setGesuch_ID(wert.toString());
                    case TITEL -> a.setTitel(wert.toString());
                    case BESCHREIBUNG -> a.setBeschreibung(wert.toString());
                    case KATEGORIE -> a.setKategorie(wert.toString());
                    case SUCHENDER_ID -> a.setSuchender_ID((wert.toString()));
                    case URL -> a.setImageUrl(wert.toString());
                }
                break;
            }
        }

        database.gesuchAendern(gesuchsID, attr, wert);
    }
    public void angebotAendern(String angebotsID, Dienstleistungsangebotdaten attr, Object wert) {
        for (Dienstleistungsangebot a : angebote) {
            if (a.getAngebots_ID().equals(angebotsID)) {
                switch(attr){
                    case ANGEBOTS_ID -> a.setAngebots_ID(wert.toString());
                    case TITEL -> a.setTitel(wert.toString());
                    case BESCHREIBUNG -> a.setBeschreibung(wert.toString());
                    case KATEGORIE -> a.setKategorie(wert.toString());
                    case AB -> a.setAb((LocalDateTime) wert);
                    case BIS -> a.setBis((LocalDateTime) wert);
                    case PERSONEN_ID -> a.setPersonenID(wert.toString());
                    case URL -> a.setImageUrl(wert.toString());
                }
                break;
            }
        }

        database.angebotAendern(angebotsID, attr, wert);
    }

    /*public void gesuchAnnehmen(String gesuchs_ID, String ersteller_ID, String nutzer_ID, int stunden) throws Exception {
        Dienstleistungsgesuch gesuch;
        int anzahl = gesuche.size();
        int counter = 0;

        for (Dienstleistungsgesuch g : gesuche) {
            if (g.getGesuch_ID() == gesuchs_ID && counter <= anzahl) {
                gesuch = g;
                //TODO g.getSuchender().veraendereStundenkonto(-stunden);
            } else
                throw new Exception();
        }
    }*/

    public void gesuchAnnehmen(String gesuchsID, String erstellerID, String nutzerID, int stunden) throws Exception{

        server.users.Rollenverwaltung r= VereinssoftwareServer.rollenverwaltung;
        Dienstleistungsgesuch gesuch = fetchGesuch(gesuchsID);

        Mitglied ersteller=r.fetch(erstellerID);
        Mitglied nutzer=r.fetch(nutzerID);
        Anfragenliste l= ersteller.getAnfragenliste();
        l.addgAnfrage(nutzer, gesuch ,stunden);

        database.gesuchAnnhemen(gesuchsID, erstellerID, nutzerID ,stunden);
    }

    public Dienstleistungsangebot fetchAngebot(String angebotID) throws NoSuchObjectException {

        for (Dienstleistungsangebot da : angebote) {
            if (da.getAngebots_ID().equals(angebotID))
                return da;
        }

        throw new NoSuchObjectException("Angebot mit ID: " + angebotID + " nicht vorhanden.");
    }
    public Dienstleistungsgesuch fetchGesuch(String gID) throws NoSuchObjectException {

        for (Dienstleistungsgesuch ds : gesuche) {
            if (ds.getGesuch_ID().equals(gID))
                return ds;
        }

        throw new NoSuchObjectException("Angebot mit ID: " + gID + " nicht vorhanden.");
    }

    public Object[][] omniAngebotDaten() throws NoSuchObjectException {
        Object[][] aliste = new Object[50000][8];

        for(int i = 0; i < angebote.size(); i++) {
            aliste[i] = getAngeboteInformationen(angebote.get(i).getAngebots_ID());
        }

        return aliste;
    }

    public Object[][] omniGesuchDaten() throws NoSuchObjectException{
        Object[][] gliste = new Object[50000][6];

        for (int i = 0; i < gesuche.size(); i++)
            gliste[i] = getGesucheInformationen(gesuche.get(i).getGesuch_ID());

        return gliste;
    }
    public void angebotAnnehmen(String angebotID, String erstellerID, String nutzerID, int stunden) throws Exception{

        server.users.Rollenverwaltung r= VereinssoftwareServer.rollenverwaltung;
        Dienstleistungsangebot angebot = fetchAngebot(angebotID);


        Mitglied ersteller=r.fetch(erstellerID);
        Mitglied nutzer=r.fetch(nutzerID);
        Anfragenliste l= ersteller.getAnfragenliste();
        l.addaAnfrage(nutzer, angebot,stunden);

        database.angebotAnnehmen(angebotID, erstellerID, nutzerID, stunden);
    }



}
