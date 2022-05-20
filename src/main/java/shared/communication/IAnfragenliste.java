package shared.communication;

/*
@author
Bastian Reichert
*/

import server.dienstleistungsmodul.AngebotAnfrage;
//import server.dienstleistungsmodul.AngebotAnfragedaten;
import server.dienstleistungsmodul.GesuchAnfrage;
//import server.dienstleistungsmodul.GesuchAnfragedaten;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;
//TODO Objekte durch Idetifikator ersetzen

// Interface, um RMI für Klasse Dienstleistungsverwaltung zu ermöglichen
public interface IAnfragenliste extends Remote {
    Object[][] OmniAAnfrageDaten() throws NoSuchObjectException;

    Object[][] OmniGAnfrageDaten() throws NoSuchObjectException;
    Object[] getGAnfragenInfo(GesuchAnfrage g) throws NoSuchObjectException;
    Object[] getAAnfragenInfo(AngebotAnfrage g) throws NoSuchObjectException;
    void removeAAnfrage(AngebotAnfrage a);
    void removeGAnfrage(GesuchAnfrage g);
    void gAnfrageAnnehmen(GesuchAnfrage g)throws Exception;
    void aAnfrageAnnehmen(AngebotAnfrage a)throws Exception;


}
