package shared.communication;

import server.users.Personendaten;
import server.users.Rolle;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

/*
@author
Raphael Kleebaum
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
    void rolleAendern(String mitgliedsID, Rolle rolle) throws RemoteException, Exception;
    void nutzereintragAendern(String mitgliedsID, Personendaten attr, String wert) throws RemoteException;
    Object[] mahnungsverwaltungAnzeigen() throws RemoteException;
    void gastHinzufuegen(String nachname, String vorname, String email, String password, String anschrift, String mitgliedsnr, int telefonnummer, boolean spender);
    public boolean login(String email, int password) throws Exception;

}
