package server.dienstleistungsmodul;

import server.VereinssoftwareServer;
import server.dienstleistungsmodul.*;
import shared.communication.IDienstleistungsverwaltung;

import java.lang.reflect.Array;
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

    public ArrayList<Dienstleistungsangebot> getAngeboteArrayList() {
        return angebote;
    }

    public ArrayList<Dienstleistungsgesuch> getGesucheArrayList() {
        return gesuche;
    }

    public ArrayList<String> aidliste;
    public ArrayList<String> gidliste;

    public Object[] getAngeboteInformationen(String angebotID) throws NoSuchObjectException {

        Dienstleistungsangebot a= fetchAngebot(angebotID);
        Object[] info = new Object[6];
        info[0] = a.getTitel();
        info[1] = a.getBeschreibung();
        info[2] = a.getKategorie();
        info[3] = a.getTime1();
        info[4] = a.getTime2();
        info[5] = a.getAngebots_ID();
        return info;
    }

    public Object[] getGesucheInformationen(String gesucheID) throws NoSuchObjectException {

        Dienstleistungsgesuch g= fetchGesuch(gesucheID);
        Object[] info = new Object[4];
        info[0] = g.getTitel();
        info[1] = g.getBeschreibung();
        info[2] = g.getKategorie();
        info[3] = g.getGesuch_ID();
        return info;
    }



    //TODO??? - public int howManyAngebote
    //TODO??? - public int howManyGesuche


    public Dienstleistungsverwaltung() {
        angebote = new ArrayList<>();
        gesuche = new ArrayList<>();
        aidliste = new ArrayList<>();
        gidliste = new ArrayList<>();
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
        Dienstleistungsgesuch g = new Dienstleistungsgesuch(gesuch_ID, titel, beschreibung, kategorie, imageUrl, ersteller);
        gesuche.add(g);
        return gesuch_ID;
    }

    public String angebotErstellen(String titel, String beschreibung, String kategorie, LocalDateTime ab, LocalDateTime bis ,String imageUrl, String personen_ID) throws Exception {
        String angebot_ID;
        angebot_ID=this.gidliste.get(0);
        angebot_ID=this.gidliste.remove(0);

        Dienstleistungsangebot g = new Dienstleistungsangebot(angebot_ID, titel, beschreibung, kategorie, ab, bis,imageUrl, personen_ID);
        angebote.add(g);
        return angebot_ID;
    }

    public void gesuchLoeschen(String gesuch_ID) {
        for (Dienstleistungsgesuch g : gesuche){
            if(g.getGesuch_ID().equals(gesuch_ID)) {
                gesuche.remove(g);
                break;
            }
        }

    }

    public void angebotLoeschen(String angebots_ID) {
        for (Dienstleistungsangebot a : angebote){
            if(a.getAngebots_ID().equals(angebots_ID)) {
                angebote.remove(a);
                break;
            }
        }

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
                }
                break;
            }
        }
    }
    public void angebotAendern(String angebotsID, Dienstleistungsangebotdaten attr, Object wert) {
        for (Dienstleistungsangebot a : angebote) {
            if (a.getAngebots_ID().equals(angebotsID)) {
                switch(attr){
                    case ANGEBOTS_ID -> a.setAngebots_ID(wert.toString());
                    case TITEL -> a.setTitel(wert.toString());
                    case BESCHREIBUNG -> a.setBeschreibung(wert.toString());
                    case KATEGORIE -> a.setKategorie(wert.toString());
                    case AB -> a.setAb(LocalDateTime.parse(wert.toString()));
                    case BIS -> a.setBis(LocalDateTime.parse(wert.toString()));
                    case PERSONEN_ID -> a.setPersonenID(wert.toString());
                }
                break;
            }
        }
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

        server.users.Mitglied ersteller=r.fetch(erstellerID);
        server.users.Mitglied nutzer=r.fetch(nutzerID);
        Anfragenliste l= ersteller.getAnfragenliste();
        l.addgAnfrage(nutzer, gesuch ,stunden);
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

    public Object[][] OmniAngebotDaten() throws NoSuchObjectException {
        Object[][] aliste = new Object[50000][7];

        for(int i = 0; i < angebote.size(); i++) {
            aliste[i] = getAngeboteInformationen(angebote.get(i).getAngebots_ID());
        }

        return aliste;
    }

    public Object[][] OmniGesuchDaten() throws NoSuchObjectException{
        Object[][] gliste = new Object[50000][5];
        
        for (int i = 0; i < gliste.length; i++)
            gliste[i] = getGesucheInformationen(gesuche.get(i).getGesuch_ID());

        return gliste;
    }
    public void angebotAnnehmen(String angebotID, String erstellerID, String nutzerID, int stunden) throws Exception{

        server.users.Rollenverwaltung r= VereinssoftwareServer.rollenverwaltung;
        Dienstleistungsangebot angebot = fetchAngebot(angebotID);


        server.users.Mitglied ersteller=r.fetch(erstellerID);
        server.users.Mitglied nutzer=r.fetch(nutzerID);
        Anfragenliste l= ersteller.getAnfragenliste();
        l.addaAnfrage(nutzer, angebot,stunden);
    }



}
