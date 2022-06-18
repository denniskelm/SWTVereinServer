package shared.communication;

/*
@author
Bastian Reichert
*/

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;

// Interface, um RMI fuer Klasse Anfrageliste zu ermoeglichen
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

