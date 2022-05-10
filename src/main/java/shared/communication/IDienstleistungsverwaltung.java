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
import server.dienstleistungsmodul.Dienstleistungsgesuch;
import server.users.Mitglied;

import java.awt.*;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

// Interface, um RMI für Klasse Dienstleistungsverwaltung zu ermöglichen
public interface IDienstleistungsverwaltung extends Remote {

    //TODO Statt Image[]: String der URL
    void gesuchErstellen(String titel, String beschreibung, String kategorie, Image[] bilder, String ersteller) throws RemoteException;
    void angebotErstellen(String titel, String beschreibung, String kategorie, LocalDateTime ab, LocalDateTime bis, Mitglied ersteller) throws RemoteException;
    void gesuchLoeschen(Dienstleistungsgesuch gesuch) throws RemoteException;
    void angebotLoeschen(Dienstleistungsangebot angebot) throws RemoteException;
    void gesuchAendern(Dienstleistungsgesuch gesuch, Object attr, Object wert) throws RemoteException;
    void angebotAendern(Dienstleistungsangebot angebot, Object attr, Object wert) throws RemoteException;
    void gesuchAnnehmen(Dienstleistungsgesuch gesuch, String ersteller, String nutzer, int stunden) throws RemoteException;
    void angebotAnfragen(Dienstleistungsangebot angebot, String ersteller, String fragender) throws RemoteException;

}
