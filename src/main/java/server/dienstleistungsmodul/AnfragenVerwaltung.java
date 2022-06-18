package server.dienstleistungsmodul;

import server.VereinssoftwareServer;
import server.users.Mitglied;
import shared.communication.IAnfragenVerwaltung;

import java.rmi.NoSuchObjectException;

/**
 * @author Bastian Reichert
 */
public class AnfragenVerwaltung implements IAnfragenVerwaltung {

    public Object[][] omniAngebotsAnfrageDaten(String nid) throws NoSuchObjectException {
        Mitglied m = VereinssoftwareServer.rollenverwaltung.fetch(nid);
        return m.getAnfragenliste().omniAngebotsAnfrageDaten();
    }

    public Object[][] omniGesuchsAnfrageDaten(String nid) throws NoSuchObjectException {
        Mitglied m = VereinssoftwareServer.rollenverwaltung.fetch(nid);
        return m.getAnfragenliste().omniGesuchsAnfrageDaten();
    }

    public Object[] getGAnfragenInfo(String nid, String id) throws NoSuchObjectException {
        Mitglied m = VereinssoftwareServer.rollenverwaltung.fetch(nid);
        return m.getAnfragenliste().getGAnfragenInfo(id);
    }

    public Object[] getAAnfragenInfo(String nid, String id) throws NoSuchObjectException {
        Mitglied m = VereinssoftwareServer.rollenverwaltung.fetch(nid);
        return m.getAnfragenliste().getAAnfragenInfo(id);
    }

    public void removeAAnfrage(String nid, String id) throws NoSuchObjectException {
        Mitglied m = VereinssoftwareServer.rollenverwaltung.fetch(nid);
        m.getAnfragenliste().removeAAnfrage(id);
    }

    public void removeGAnfrage(String nid, String id) throws NoSuchObjectException {
        Mitglied m = VereinssoftwareServer.rollenverwaltung.fetch(nid);
        m.getAnfragenliste().removeGAnfrage(id);
    }

    public void gAnfrageAnnehmen(String nid, String id) throws Exception {
        Mitglied m = VereinssoftwareServer.rollenverwaltung.fetch(nid);
        m.getAnfragenliste().gAnfrageAnnehmen(id);
    }

    public void aAnfrageAnnehmen(String nid, String id) throws Exception {
        Mitglied m = VereinssoftwareServer.rollenverwaltung.fetch(nid);
        m.getAnfragenliste().aAnfrageAnnehmen(id);
    }

    public void addaAnfrage(String nid, String nutzerId, String angebotId, int stunden) throws NoSuchObjectException {
        Mitglied m = VereinssoftwareServer.rollenverwaltung.fetch(nid);
        m.getAnfragenliste().addaAnfrage(nutzerId, angebotId, stunden);
    }

    public void addgAnfrage(String nid, String nutzerId, String angebotId, int stunden) throws NoSuchObjectException {
        Mitglied m = VereinssoftwareServer.rollenverwaltung.fetch(nid);
        m.getAnfragenliste().addgAnfrage(nutzerId, angebotId, stunden);
    }


}
