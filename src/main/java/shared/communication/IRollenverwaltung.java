package shared.communication;

import server.geraetemodul.Mahnung;
import server.users.*;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

/*
@author
Raphael Kleebaum
Gabriel Kleebaum
*/

// Interface, um RMI für Klasse Rollenverwaltung zu ermöglichen
public interface IRollenverwaltung extends Remote {


    void rolleAendern(String mitgliedsID, Rolle rolle) throws RemoteException, Exception;
    void nutzereintragAendern(String mitgliedsID, Personendaten attr, String wert) throws RemoteException;
    Object[] gastHinzufuegen(String nachname, String vorname, String email, String password, String anschrift, String mitgliedsnr, String telefonnummer, boolean spender) throws RemoteException;
    Object[] login(String email, String password) throws Exception;
    long getIdCounter() throws RemoteException;

    boolean istSpender(String nutzerId) throws RemoteException;

    String getMitgliedsNamen(String MitgliedsID) throws RemoteException, Exception;
    String getMitgliedsMail(String MitgliedsID) throws RemoteException, Exception;
    int getStundenzahl(String mitgliedsID) throws RemoteException;
    Object[] mahnungenVomNutzer(String mitgliedsID) throws RemoteException;
    int anzahlMahnungenVonNutzer(String mitgliedsID) throws RemoteException;
    void mahnungLoeschen(String mahnungsID) throws RemoteException;
    void mahnungErstellen(String mitgliedsID, String grund, LocalDateTime verfallsdatum) throws NoSuchObjectException, RemoteException;
    Mahnung fetchMahnung(String mahnungsID) throws NoSuchObjectException, RemoteException;
    Object[] mahnungAnzeigen(String mahnungsID) throws RemoteException;
    Object[] mitgliedDaten(String mitgliedsID) throws RemoteException;
    Object[][] mitgliederDaten() throws RemoteException;
    Object[][] vorsitzDaten() throws RemoteException;
    Object[][] mitarbeiterDaten() throws RemoteException;
    Object[][] gaesteDaten() throws RemoteException;
    Object[] gastDaten(String mitgliedsID) throws RemoteException;
    boolean existiertEMail(String email) throws RemoteException;
    public String mahnungsIdErstellen() throws RemoteException;


}
