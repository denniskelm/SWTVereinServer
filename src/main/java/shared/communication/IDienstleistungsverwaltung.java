package shared.communication;

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

import server.dienstleistungsmodul.Dienstleistungsangebot;
import server.dienstleistungsmodul.Dienstleistungsangebotdaten;
import server.dienstleistungsmodul.Dienstleistungsgesuch;
import server.dienstleistungsmodul.Dienstleistungsgesuchdaten;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.ArrayList;

// Interface, um RMI für Klasse Dienstleistungsverwaltung zu ermöglichen
public interface IDienstleistungsverwaltung extends Remote {


    String gesuchErstellen(String titel, String beschreibung, String kategorie, String imageUrl, String ersteller) throws RemoteException, Exception;
    String angebotErstellen(String titel, String beschreibung, String kategorie, LocalDateTime ab, LocalDateTime bis, String ersteller) throws RemoteException, Exception;
    void gesuchLoeschen(String gesuchsID) throws RemoteException;
    void angebotLoeschen(String angebotsID) throws RemoteException;
    void gesuchAendern(String gesuchsID, Dienstleistungsgesuchdaten attr, Object wert) throws RemoteException;
    void angebotAendern(String angebotsID, Dienstleistungsangebotdaten attr, Object wert) throws RemoteException;
    void gesuchAnnehmen(String gesuchsID, String ersteller, String nutzer, int stunden) throws RemoteException, Exception;
    void angebotAnnehmen(String gesuchsID, String ersteller, String nutzer, int stunden) throws RemoteException, Exception;
    Object[] getAngeboteInformationen(String geraeteID) throws RemoteException;
    Object[] getGesucheInformationen(String geraeteID) throws RemoteException;
    Object[][] OmniAngebotDaten() throws RemoteException;

}
