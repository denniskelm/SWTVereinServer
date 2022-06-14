package shared.communication;

/*
@author
Bastian Reichert
*/

//import server.dienstleistungsmodul.AngebotAnfragedaten;
//import server.dienstleistungsmodul.GesuchAnfragedaten;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;

// Interface, um RMI für Klasse Dienstleistungsverwaltung zu ermöglichen
public interface IAnfragenliste extends Remote {
    Object[][] omniAngebotsAnfrageDaten() throws Exception;

    Object[][] omniGesuchsAnfrageDaten() throws Exception;
    Object[] getGAnfragenInfo(String id) throws Exception;
    Object[] getAAnfragenInfo(String id) throws Exception;
    void removeAAnfrage(String id) throws NoSuchObjectException, RemoteException;
    void removeGAnfrage(String id) throws NoSuchObjectException, RemoteException;
    void gAnfrageAnnehmen(String id) throws Exception;
    void aAnfrageAnnehmen(String id) throws Exception;
    void addaAnfrage(String nutzerId, String angebotId, int stunden) throws RemoteException;
    void addgAnfrage(String nutzerId, String gesuchId, int stunden) throws RemoteException;


}
