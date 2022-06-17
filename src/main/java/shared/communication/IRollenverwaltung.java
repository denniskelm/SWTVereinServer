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

    Object[] gastListeAnzeigen() throws RemoteException;    //Object[]

    Object[] mitgliedListeAnzeigen() throws RemoteException;    //Object[]

    Object[] mitarbeiterListeAnzeigen() throws RemoteException; //Object[]

    Object[] vorsitzListeAnzeigen() throws RemoteException; //Object[]

    void rolleAendern(String mitgliedsID, Rolle rolle) throws RemoteException, Exception;

    void nutzereintragAendern(String mitgliedsID, Personendaten attr, String wert) throws RemoteException;

    Object[] mahnungsverwaltungAnzeigen() throws RemoteException;   //Object[]

    Object[] gastHinzufuegen(String nachname, String vorname, String email, String password, String anschrift, String mitgliedsnr, String telefonnummer, boolean spender) throws RemoteException;

    Object[] login(String email, String password) throws Exception;

    //ArrayList<Gast> getGaeste() throws RemoteException;  //ArrayList<Gast>
    //ArrayList<Mitglied> getMitglieder() throws RemoteException;  //ArrayList<Mitglied>
    //ArrayList<Mitarbeiter> getMitarbeiter() throws RemoteException; //ArrayList<Mitarbeiter>
    //ArrayList<Vorsitz> getVorsitze() throws RemoteException;    //ArrayList<Vorsitz>
    long getIdCounter() throws RemoteException;

    String getMitgliedsNamen(String MitgliedsID) throws RemoteException, Exception;

    String getMitgliedsMail(String MitgliedsID) throws RemoteException, Exception;

    int getStundenzahl(String mitgliedsID) throws RemoteException;

    Object[] mahnungenVomNutzer(String mitgliedsID) throws RemoteException;

    int anzahlMahnungenVonNutzer(String mitgliedsID) throws RemoteException;

    void mahnungLoeschen(String mahnungsID) throws RemoteException;

    void mahnungErstellen(String mitgliedsID, String grund, LocalDateTime verfallsdatum) throws NoSuchObjectException, RemoteException;

    Mahnung fetchMahnung(String mahnungsID) throws NoSuchObjectException, RemoteException;

    public Object[] mahnungAnzeigen(String mahnungsID) throws RemoteException;

    public Object[] mitgliedDaten(String mitgliedsID) throws RemoteException;

    public Object[][] mitgliederDaten() throws RemoteException;

    public Object[][] vorsitzDaten() throws RemoteException;

    public Object[][] mitarbeiterDaten() throws RemoteException;
    public Object[][] gaesteDaten() throws RemoteException;
    public Object[] gastDaten(String mitgliedsID) throws RemoteException;

}
