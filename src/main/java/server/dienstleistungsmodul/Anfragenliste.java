package server.dienstleistungsmodul;
/*
@author
Bastian Reichert
*/
import server.VereinssoftwareServer;
import server.db.AnfragenDB;
import server.db.DienstleistungsDB;
import server.users.Mitglied;
import shared.communication.IAnfragenliste;

import javax.naming.NoPermissionException;
import java.rmi.NoSuchObjectException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Anfragenliste implements IAnfragenliste {
    public String user_ID;

    public Mitglied nutzer;
    private ArrayList<GesuchAnfrage> gliste;
    private ArrayList<AngebotAnfrage> aliste;

    private ArrayList<String> aaidliste;
    private ArrayList<String> gaidliste;
    private final AnfragenDB aDB;

    public void createIdListen(){
        this.aaidliste = new ArrayList<String>();
        this.gaidliste = new ArrayList<String>();
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
        for (AngebotAnfrage aa: aliste) {
            for (String element : aaidliste) {
                if (aa.anfrageID.equals(element)) {
                    aaidliste.remove(element);
                    break;
                }
            }
        }
        for (GesuchAnfrage ga: gliste) {
            for (String element : gaidliste) {
                if (ga.anfrageID.equals(element)) {
                    gaidliste.remove(element);
                    break;
                }
            }
        }
    }

    public void reset() throws NoSuchObjectException{
        while (gliste.size()>0) {
            removeGAnfrage(gliste.get(0).anfrageID);
        }
        while (aliste.size()>0) {
            removeAAnfrage(aliste.get(0).anfrageID);
        }
        this.gliste = new ArrayList<GesuchAnfrage>();

        this.aliste = new ArrayList<AngebotAnfrage>();
        createIdListen();
    }

    public Anfragenliste(Mitglied nutzer){
        //String user_ID = mitglied.getPersonenID();

        try {
            aDB = new AnfragenDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //this.user_ID = user_ID;
        //this.user_ID = user_ID;
        //try {
            this.nutzer = nutzer;
        //} catch (NoSuchObjectException e) {
        //    throw new RuntimeException(e);
       // }
        user_ID= nutzer.getPersonenID();
        this.gliste = aDB.getGesuchanfragenForId(nutzer);
        this.aliste = aDB.getAngebotanfragenForId(nutzer);
        //this.gliste = new ArrayList<GesuchAnfrage>();
        //this.aliste = new ArrayList<AngebotAnfrage>();

        createIdListen();
    }

    public GesuchAnfrage gfetch(String id) throws NoSuchObjectException {
        for (GesuchAnfrage ga : gliste) {
        if (ga.anfrageID.equals(id))
            return ga;
    }

        throw new NoSuchObjectException("Anfrage mit ID: " + id + " nicht vorhanden.");
    }

    public AngebotAnfrage afetch(String id) throws NoSuchObjectException {
        for (AngebotAnfrage aa : aliste) {
            if (aa.anfrageID.equals(id))
                return aa;
        }

        throw new NoSuchObjectException("Anfrage mit ID: " + id + " nicht vorhanden.");
    }

    public Object[] getGAnfragenInfo(String id) throws NoSuchObjectException {
        //Dienstleistungsverwaltung d= VereinssoftwareServer.dienstleistungsverwaltung;
        //Dienstleistungsgesuch g = d.fetchGesuch(gesuchID);
        GesuchAnfrage g = gfetch(id);
        Object[] info = new Object[8];
        info[0] = g.nutzer.getPersonenID();
        info[1] = g.gesuch.getGesuch_ID();
        info[2] = g.stunden;
        Dienstleistungsverwaltung dv = VereinssoftwareServer.dienstleistungsverwaltung;
        Object[] daInfo = dv.getGesucheInformationen(g.gesuch.getGesuch_ID());

        info[3] = daInfo[0]; //Titel
        info[4] = daInfo[1]; //Beschreibung
        info[5] = daInfo[2]; //Kategorie
        info[6] = daInfo[5]; //bild
        info[7] = g.anfrageID; //anfrageID
        return info;
    }

    public Object[] getAAnfragenInfo(String id) throws NoSuchObjectException {
        AngebotAnfrage a = afetch(id);
        Object[] info = new Object[10];

        Dienstleistungsverwaltung dv = VereinssoftwareServer.dienstleistungsverwaltung;
        Object[] daInfo = dv.getAngeboteInformationen(a.angebot.getAngebots_ID());

        info[0] = a.nutzer.getPersonenID();
        info[1] = a.angebot.getAngebots_ID();
        info[2] = a.stunden;
        info[3] = daInfo[0]; //Titel
        info[4] = daInfo[1]; //Beschreibung
        info[5] = daInfo[2]; //Kategorie
        info[6] = daInfo[3]; //ab
        info[7] = daInfo[4]; //bis
        info[8] = daInfo[7]; //bild
        info[9] = a.anfrageID; //anfrageID
        return info;
    }

    public Object[][] omniAngebotsAnfrageDaten() throws NoSuchObjectException {
        Object[][] liste = new Object[aliste.size()][10];//TODO index out of range

        for(int i = 0; i < aliste.size(); i++) {
            liste[i] = getAAnfragenInfo(aliste.get(i).anfrageID);
        }

        return liste;
    }

    public Object[][] omniGesuchsAnfrageDaten() throws NoSuchObjectException {

        Object[][] liste = new Object[gliste.size()][8];

        for(int i = 0; i < gliste.size(); i++) {

            liste[i] = getGAnfragenInfo(gliste.get(i).anfrageID);
        }

        return liste;
    }


    public void addaAnfrage(String anfragenderID, String angebotID, int stunden) throws NoSuchObjectException {
        Dienstleistungsangebot angebot= VereinssoftwareServer.dienstleistungsverwaltung.fetchAngebot(angebotID);
        Mitglied anfragender=VereinssoftwareServer.rollenverwaltung.fetch(anfragenderID);
        String anfrageID=this.aaidliste.get(0);
        this.aaidliste.remove(0);
        AngebotAnfrage a =new AngebotAnfrage(anfrageID, anfragender, angebot, stunden);
        if (aliste.size() <= 50) {
            aDB.addaAnfrage(a);
            this.aliste.add(a);
        }else throw new ArrayIndexOutOfBoundsException("Mitglied hat bereits 50 Anfragen");
    }

    public void addgAnfrage(String anfragenderID, String gesuchID, int stunden) throws NoSuchObjectException{
        Dienstleistungsgesuch gesuch= VereinssoftwareServer.dienstleistungsverwaltung.fetchGesuch(gesuchID);
        Mitglied anfragender=VereinssoftwareServer.rollenverwaltung.fetch(anfragenderID);
        String anfrageID=this.gaidliste.get(0);
        this.gaidliste.remove(0);
        GesuchAnfrage g = new GesuchAnfrage(anfrageID, anfragender, gesuch, stunden);
        if (gliste.size() <= 50){
            aDB.addgAnfrage(g);
            this.gliste.add(g);
        }
        else throw new ArrayIndexOutOfBoundsException("Mitglied hat bereits 50 Anfragen");
    }
    public void removeAAnfrage(String id) throws NoSuchObjectException{
        AngebotAnfrage a= null;
        a = afetch(id);
        this.aaidliste.add(a.anfrageID);
        this.aliste.remove(a);
        aDB.removeaAnfrage(a);
    }

    public void removeGAnfrage(String id) throws NoSuchObjectException{
        GesuchAnfrage g= null;
        g = gfetch(id);
        this.gaidliste.add(g.anfrageID);
        this.gliste.remove(g);
        aDB.removegAnfrage(g);
    }

    public void gAnfrageAnnehmen(String id) throws Exception{
        GesuchAnfrage g= null;
        g = gfetch(id);

        if (g.nutzer.isGesperrt()) throw new NoPermissionException("Mitglied ist gesperrt.");

        this.gaidliste.add(g.anfrageID);
        this.gliste.remove(g);



        aDB.removegAnfrage(g);

        for (GesuchAnfrage ga : gliste) {
            if (ga.gesuch==g.gesuch) {
                this.gliste.remove(ga);
                aDB.removegAnfrage(ga);
            }
        }

        if (!VereinssoftwareServer.dienstleistungsverwaltung.gesuchLoeschen(g.gesuch.getGesuch_ID()))return;
        this.nutzer.veraendereStundenkonto(g.stunden);
        g.nutzer.veraendereStundenkonto(-g.stunden);
        //VereinssoftwareServer.dienstleistungsverwaltung.gidliste.add(g.gesuch.getGesuch_ID());

    }

    public void aAnfrageAnnehmen(String id) throws Exception{
        AngebotAnfrage a= null;
        a = afetch(id);

        if (a.nutzer.isGesperrt()) throw new NoPermissionException("Mitglied ist gesperrt.");


        this.aaidliste.add(a.anfrageID);
        this.aliste.remove(a);
        aDB.removeaAnfrage(a);

        for (AngebotAnfrage aa : aliste) {
                if (aa.angebot == a.angebot){
                    this.aliste.remove(aa);
                    aDB.removeaAnfrage(aa);
            }
        }

        if (!VereinssoftwareServer.dienstleistungsverwaltung.angebotLoeschen(a.angebot.getAngebots_ID()))return;
        //VereinssoftwareServer.dienstleistungsverwaltung.aidliste.add(a.angebot.getAngebots_ID());

        this.nutzer.veraendereStundenkonto(a.stunden);
        a.nutzer.veraendereStundenkonto(-a.stunden);
    }


}