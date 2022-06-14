package shared.communication;

import server.geraetemodul.Geraetedaten;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/*
@author
TODO Raphael Kleebaum
TODO Jonny Schlutter
Gabriel Kleebaum
TODO Mhd Esmail Kanaan
TODO Gia Huy Hans Tran
TODO Ole Bjoern Adelmann
TODO Bastian Reichert
TODO Dennis Kelm
*/

// Interface, um RMI fuer Klasse Geraeteverwaltung zu ermoeglichen
public interface IGeraeteverwaltung extends Remote {

    String geraetHinzufuegen(String name, String spender, int leihfrist, String kategorie, String beschreibung, String abholort, String bild) throws RemoteException;
    void geraetReservieren(String geraeteID, String ausleiherID) throws Exception;
    void geraetAusgeben(String geraeteID) throws Exception;
    void geraetAnnehmen(String geraeteID) throws Exception;
    void geraetEntfernen(String geraeteID) throws RemoteException;
    void geraeteDatenVerwalten(String geraeteID, Geraetedaten attr, Object wert) throws RemoteException;
    void historieZuruecksetzen(String geraeteID) throws RemoteException;
    String geraeteDatenAusgeben(String geraeteID) throws RemoteException;
    int getIdCounter() throws RemoteException;
    Object[][] omniGeraeteDaten() throws NoSuchObjectException, RemoteException;

}
