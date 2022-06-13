package shared.communication;

import server.users.Personendaten;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

public interface IGast extends Remote {

    String getPersonenID() throws RemoteException;
    String getNachname() throws RemoteException;
    String getAnschrift() throws RemoteException;
    String getVorname() throws RemoteException;
    String getEmail() throws RemoteException;
    int getPassword() throws RemoteException;
    String getMitgliedsNr() throws RemoteException;
    String getTelefonNr() throws RemoteException;
    boolean getSpenderStatus() throws RemoteException;
    void datenVerwalten(Personendaten attr, String wert) throws RemoteException;

}
