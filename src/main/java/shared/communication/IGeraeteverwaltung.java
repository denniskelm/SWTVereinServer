package shared.communication;

import server.Geraetedaten;

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

// Interface, um RMI für Klasse Geraeteverwaltung zu ermöglichen
public interface IGeraeteverwaltung extends Remote {

    void geraetHinzufuegen(String name, String spender, int leihfrist, String kategorie, String beschreibung, String abholort) throws RemoteException;
    void geraetReservieren(String geraeteID, String ausleiherID) throws RemoteException;
    void geraetAusgeben(String geraeteID) throws RemoteException;
    void geraetAnnehmen(String geraeteID) throws RemoteException;
    void geraetEntfernen(String geraeteID) throws RemoteException;
    void geraeteDatenVerwalten(String geraeteID, Geraetedaten attr, Object wert) throws RemoteException;
    void historieZuruecksetzen(String geraeteID) throws RemoteException;
    void geraeteAnzeigen() throws RemoteException;

}
