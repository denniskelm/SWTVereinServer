package shared.communication;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

public interface IAusleiher extends Remote {

    LocalDateTime getFristBeginn() throws RemoteException;
    void setFristBeginn(LocalDateTime fristBeginn) throws RemoteException;
    String getMitgliedsID() throws RemoteException;
    LocalDateTime getReservierdatum() throws RemoteException;
    void setReservierdatum(LocalDateTime reservierdatum) throws RemoteException;
    boolean isAbgegeben() throws RemoteException;
    void setAbgegeben(boolean abgegeben) throws RemoteException;

}
