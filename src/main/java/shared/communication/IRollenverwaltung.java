package shared.communication;

import server.users.Mitglied;
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

    void gastListeAnzeigen() throws RemoteException;
    void mitgliedListeAnzeigen() throws RemoteException;
    void mitarbeiterListeAnzeigen() throws RemoteException;
    void vorsitzListeAnzeigen() throws RemoteException;
    void rolleAendern(Mitglied mitglied, Rolle rolle) throws RemoteException;
    void nutzereintragAendern(String mitgliedsID, Object attr, Object wert) throws RemoteException;
    void mahnungsverwaltungAnzeigen() throws RemoteException;

}
