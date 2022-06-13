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
    Object[][] omniAngebotsAnfrageDaten() throws Exception;

    Object[][] omniGesuchsAnfrageDaten() throws Exception;
    /*Object[] getGAnfragenInfo(GesuchAnfrage g) throws Exception;
    Object[] getAAnfragenInfo(AngebotAnfrage g) throws Exception;
    void removeAAnfrage(AngebotAnfrage a) throws RemoteException;
    void removeGAnfrage(GesuchAnfrage g) throws RemoteException;
    void gAnfrageAnnehmen(GesuchAnfrage g) throws Exception;
    void aAnfrageAnnehmen(AngebotAnfrage a) throws Exception;
    void addaAnfrage(Mitglied nutzer, Dienstleistungsangebot angebot, int stunden) throws RemoteException;
    void addgAnfrage(Mitglied nutzer, Dienstleistungsgesuch gesuch, int stunden) throws RemoteException;*/


}
