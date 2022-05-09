package shared.communication;

import server.Geraet;

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
    void geraetReservieren(Geraet geraet, String ausleiherID) throws RemoteException;
    void geraetAusgeben(Geraet geraet) throws RemoteException;
    void geraetAnnehmen(Geraet geraet) throws RemoteException;
    void geraetEntfernen(Geraet geraet) throws RemoteException;
    void gereateDatenVerwalten(Geraet g, Object attr, Object wert) throws RemoteException;
    void historieZuruecksetzen(Geraet geraet) throws RemoteException;
    void geraeteAnzeigen() throws RemoteException;

}
