package shared.communication;

import server.geraetemodul.Ausleiher;
import server.geraetemodul.Status;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IGeraet extends Remote {

    ArrayList<Ausleiher> getHistorie() throws RemoteException;
    void reservierungHinzufuegen(String personenID) throws RemoteException;
    void reservierungEntfernen(String personenID) throws RemoteException;
    void ausgeben() throws RemoteException;
    void updateFristen() throws RemoteException;
    void annehmen() throws RemoteException;
    String getGeraeteID() throws RemoteException;
    String getSpenderName() throws RemoteException;
    String getGeraetName() throws RemoteException;
    String getGeraetAbholort() throws RemoteException;
    String getKategorie() throws RemoteException;
    String getGeraetBeschreibung() throws RemoteException;
    int getLeihfrist() throws RemoteException;
    Status getLeihstatus() throws RemoteException;
    ArrayList<Ausleiher> getReservierungsliste() throws RemoteException;
    void setHistorie(ArrayList<Ausleiher> historie) throws RemoteException;
    void setName(String name) throws RemoteException;
    void setSpenderName(String spenderName) throws RemoteException;
    void setLeihfrist(int leihfrist) throws RemoteException;
    void setKategorie(String kategorie) throws RemoteException;
    void setBeschreibung(String beschreibung) throws RemoteException;
    void setAbholort(String abholort) throws RemoteException;
    void setReservierungsliste(ArrayList<Ausleiher> reservierungsliste) throws RemoteException;

}
