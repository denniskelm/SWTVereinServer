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

    public Object[] getGAnfragenInfo(GesuchAnfrage g) throws NoSuchObjectException {

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
    public Anfragenliste(String user_ID) {
        this.user_ID = user_ID;
        this.gliste = new ArrayList<GesuchAnfrage>();
        this.aliste = new ArrayList<AngebotAnfrage>();
    }

    public void addaAnfrage(Mitglied nutzer, Dienstleistungsangebot angebot, int stunden) {
        AngebotAnfrage a =new AngebotAnfrage(nutzer, angebot,stunden);
        this.aliste.add(a);
    }

    public void addgAnfrage(Mitglied nutzer, Dienstleistungsgesuch gesuch, int stunden) {
        GesuchAnfrage g = new GesuchAnfrage(nutzer, gesuch, stunden);
        this.gliste.add(g);
    }
    public void removeAAnfrage(AngebotAnfrage a){
        this.aliste.remove(a);
    }

    public void removeGAnfrage(GesuchAnfrage g){
        this.gliste.remove(g);
    }

    public void gAnfrageAnnehmen(GesuchAnfrage g) throws Exception{
        /*int i=0;
        while (i<this.gliste.size()){
            if (this.gliste.get(i)==g){

            }

        }*/

        this.gliste.remove(g);
        this.nutzer.veraendereStundenkonto(g.stunden);
        g.nutzer.veraendereStundenkonto(-g.stunden);
        VereinssoftwareServer.dienstleistungsverwaltung.gidliste.add(g.gesuch.getGesuch_ID());
    }

    public void aAnfrageAnnehmen(AngebotAnfrage a) throws Exception{
        this.gliste.remove(a);
        this.nutzer.veraendereStundenkonto(a.stunden);
        a.nutzer.veraendereStundenkonto(-a.stunden);
        VereinssoftwareServer.dienstleistungsverwaltung.aidliste.add(a.angebot.getAngebots_ID());
    }
}