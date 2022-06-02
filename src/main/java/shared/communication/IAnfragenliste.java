package shared.communication;

/*
@author
Bastian Reichert
*/

//import server.dienstleistungsmodul.AngebotAnfragedaten;
//import server.dienstleistungsmodul.GesuchAnfragedaten;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
//TODO Objekte durch Idetifikator ersetzen

// Interface, um RMI für Klasse Dienstleistungsverwaltung zu ermöglichen
public interface IAnfragenliste extends Remote {
    Object[][] omniAngebotsAnfrageDaten() throws NoSuchObjectException;

    Object[][] omniGesuchsAnfrageDaten() throws NoSuchObjectException;
    /*Object[] getGAnfragenInfo(GesuchAnfrage g) throws NoSuchObjectException;
    Object[] getAAnfragenInfo(AngebotAnfrage g) throws NoSuchObjectException;
    void removeAAnfrage(AngebotAnfrage a);
    void removeGAnfrage(GesuchAnfrage g);
    void gAnfrageAnnehmen(GesuchAnfrage g) throws Exception;
    void aAnfrageAnnehmen(AngebotAnfrage a) throws Exception;
    void addaAnfrage(Mitglied nutzer, Dienstleistungsangebot angebot, int stunden);
    void addgAnfrage(Mitglied nutzer, Dienstleistungsgesuch gesuch, int stunden);*/


}
