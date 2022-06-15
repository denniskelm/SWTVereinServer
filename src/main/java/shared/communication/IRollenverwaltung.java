package shared.communication;

import server.users.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

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

    Object[] gastListeAnzeigen() throws RemoteException;    //Object[]
    Object[] mitgliedListeAnzeigen() throws RemoteException;    //Object[]
    Object[] mitarbeiterListeAnzeigen() throws RemoteException; //Object[]
    Object[] vorsitzListeAnzeigen() throws RemoteException; //Object[]
    void rolleAendern(String mitgliedsID, Rolle rolle) throws RemoteException, Exception;
    void nutzereintragAendern(String mitgliedsID, Personendaten attr, String wert) throws RemoteException;
    Object[] mahnungsverwaltungAnzeigen() throws RemoteException;   //Object[]
    void gastHinzufuegen(String nachname, String vorname, String email, String password, String anschrift, String mitgliedsnr, String telefonnummer, boolean spender) throws RemoteException;
    boolean login(String email, String password) throws Exception;
    ArrayList<Gast> getGaeste() throws RemoteException;  //ArrayList<Gast>
    ArrayList<Mitglied> getMitglieder() throws RemoteException;  //ArrayList<Mitglied>
    ArrayList<Mitarbeiter> getMitarbeiter() throws RemoteException; //ArrayList<Mitarbeiter>
    ArrayList<Vorsitz> getVorsitze() throws RemoteException;    //ArrayList<Vorsitz>
    //ArrayList<IMahnung> getMahnungen() throws RemoteException;   //ArrayList<Mahnung>
    long getIdCounter() throws RemoteException;
    public String getMitgliedsNamen(String MitgliedsID) throws RemoteException, Exception;
    public String getMitgliedsMail(String MitgliedsID) throws RemoteException, Exception;

}
