package server.dienstleistungsmodul;
/*
@author
Bastian Reichert
*/
import server.VereinssoftwareServer;
import server.users.Mitglied;
import shared.communication.IAnfragenliste;

import java.rmi.NoSuchObjectException;
import java.util.ArrayList;

public class Anfragenliste implements IAnfragenliste {
    private String user_ID;

    public Mitglied nutzer;
    private ArrayList<GesuchAnfrage> gliste;
    private ArrayList<AngebotAnfrage> aliste;

    private ArrayList<String> aaidliste;
    private ArrayList<String> gaidliste;

    public void createIdListen(){
        int anzahl=0;
        while (anzahl<50) {
            if (anzahl < 9)  // todo : nach dem testen ist die Rueckgabe nicht wie gewunscht???
                aaidliste.add("aa0000" + (anzahl + 1)); // klammern hinzugefügt damit test richtig
            else if (anzahl < 99)
                aaidliste.add("aa000" + (anzahl + 1));
            anzahl++;
        }

        //Liste mit verfuegbaren IDs für Angebote fuellen
        anzahl=0;
        while (anzahl<50) {
            if (anzahl < 9)  // todo : nach dem testen ist die Rueckgabe nicht wie gewunscht???
                gaidliste.add("ga0000" + (anzahl + 1)); // klammern hinzugefügt damit test richtig
            else if (anzahl < 99)
                gaidliste.add("ga000" + (anzahl + 1));
            anzahl++;
        }
    }

    public Anfragenliste(/*String user_ID*/) {
        //this.user_ID = user_ID;
        this.gliste = new ArrayList<GesuchAnfrage>();
        this.aliste = new ArrayList<AngebotAnfrage>();
        createIdListen();
    }

    public Object[] getGAnfragenInfo(GesuchAnfrage g) throws NoSuchObjectException {
        //Dienstleistungsverwaltung d= VereinssoftwareServer.dienstleistungsverwaltung;
        //Dienstleistungsgesuch g = d.fetchGesuch(gesuchID);
        Object[] info = new Object[3];
        info[0] = g.nutzer.getPersonenID();
        info[1] = g.gesuch.getGesuch_ID();
        info[2] = g.stunden;
        return info;
    }

    public Object[] getAAnfragenInfo(AngebotAnfrage a) throws NoSuchObjectException {

        Object[] info = new Object[3];
        info[0] = a.nutzer.getPersonenID();
        info[1] = a.angebot.getAngebots_ID();
        info[2] = a.stunden;
        return info;
    }

    public Object[][] omniAngebotsAnfrageDaten() throws NoSuchObjectException {
        Object[][] liste = new Object[50][3];//TODO index out of range

        for(int i = 0; i < aliste.size(); i++) {
            liste[i] = getAAnfragenInfo(aliste.get(i));
        }

        return liste;
    }

    public Object[][] omniGesuchsAnfrageDaten() throws NoSuchObjectException {
        Object[][] liste = new Object[50][3];

        for(int i = 0; i < gliste.size(); i++) {
            liste[i] = getGAnfragenInfo(gliste.get(i));
        }

        return liste;
    }

    //TODO @Bastian Exception für fehlende ofUserID Angabe (Zeile this+2) - RMI darf keine Parameter bekommen im Konstruktor
    public Anfragenliste ofUser_ID(String user_ID){
        this.user_ID = user_ID;
        return this;
    }

    public void addaAnfrage(Mitglied nutzer, Dienstleistungsangebot angebot, int stunden) {
        String anfrageID=this.gaidliste.get(0);
        this.gaidliste.remove(0);
        AngebotAnfrage a =new AngebotAnfrage(anfrageID, nutzer, angebot,stunden);
        this.aliste.add(a);
    }

    public void addgAnfrage(Mitglied nutzer, Dienstleistungsgesuch gesuch, int stunden) {
        String anfrageID=this.aaidliste.get(0);
        this.aaidliste.remove(0);
        GesuchAnfrage g = new GesuchAnfrage(anfrageID, nutzer, gesuch, stunden);
        this.gliste.add(g);
    }
    public void removeAAnfrage(AngebotAnfrage a){
        this.aaidliste.add(a.anfrageID);
        this.aliste.remove(a);
    }

    public void removeGAnfrage(GesuchAnfrage g){
        this.gaidliste.add(g.anfrageID);
        this.gliste.remove(g);
    }

    public void gAnfrageAnnehmen(GesuchAnfrage g) throws Exception{
        /*int i=0;
        while (i<this.gliste.size()){
            if (this.gliste.get(i)==g){

            }

        }*/
        this.gaidliste.add(g.anfrageID);
        this.gliste.remove(g);
        this.nutzer.veraendereStundenkonto(g.stunden);
        g.nutzer.veraendereStundenkonto(-g.stunden);
        VereinssoftwareServer.dienstleistungsverwaltung.gidliste.add(g.gesuch.getGesuch_ID());
    }

    public void aAnfrageAnnehmen(AngebotAnfrage a) throws Exception{
        this.aaidliste.add(a.anfrageID);
        this.gliste.remove(a);
        this.nutzer.veraendereStundenkonto(a.stunden);
        a.nutzer.veraendereStundenkonto(-a.stunden);
        VereinssoftwareServer.dienstleistungsverwaltung.aidliste.add(a.angebot.getAngebots_ID());
    }
}