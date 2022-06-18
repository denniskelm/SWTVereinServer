package shared.communication;

/*
@author
Dennis Kelm
Bastian Reichert
Gabriel Kleebaum
*/

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;

// Interface, um RMI fuer Klasse AnfragenVerwaltung zu ermoeglichen
public interface IAnfragenVerwaltung extends Remote {
    Object[][] omniAngebotsAnfrageDaten(String nid) throws Exception;

    Object[][] omniGesuchsAnfrageDaten(String nid) throws Exception;

    Object[] getGAnfragenInfo(String nid, String id) throws Exception;

    Object[] getAAnfragenInfo(String nid, String id) throws Exception;

    void removeAAnfrage(String nid, String id) throws NoSuchObjectException, RemoteException;

    void removeGAnfrage(String nid, String id) throws NoSuchObjectException, RemoteException;

    void gAnfrageAnnehmen(String nid, String id) throws Exception;

    void aAnfrageAnnehmen(String nid, String id) throws Exception;

    void addaAnfrage(String nid, String nutzerId, String angebotId, int stunden) throws RemoteException;

    void addgAnfrage(String nid, String nutzerId, String gesuchId, int stunden) throws RemoteException;


}
