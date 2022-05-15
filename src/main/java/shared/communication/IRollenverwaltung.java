package shared.communication;

import server.users.Mitglied;
import server.users.Mitgliederdaten;
import server.users.Rolle;

import java.rmi.Remote;
import java.rmi.RemoteException;

/*
@author
TODO Raphael Kleebaum
TODO Jonny Schlutter
Gabriel Kleebaum
TODO Mhd Esmail Kanaan
TODO Gia Huy Hans Tran
TODO Ole Björn Adelmann
TODO Bastian Reichert
TODO Dennis Kelm
*/

// Interface, um RMI für Klasse Rollenverwaltung zu ermöglichen
public interface IRollenverwaltung extends Remote {

    Object[] gastListeAnzeigen() throws RemoteException;
    Object[] mitgliedListeAnzeigen() throws RemoteException;
    Object[] mitarbeiterListeAnzeigen() throws RemoteException;
    Object[] vorsitzListeAnzeigen() throws RemoteException;
    void rolleAendern(String mitgliedsID, Rolle rolle) throws RemoteException;
    void nutzereintragAendern(String mitgliedsID, Mitgliederdaten attr, Object wert) throws RemoteException, Exception;
    Object[] mahnungsverwaltungAnzeigen() throws RemoteException;

}
