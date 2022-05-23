package shared.communication;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

public interface IMahnungsverwaltung  extends Remote {

    void mahnungErstellen(String mitgliedsID, String grund, LocalDateTime verfallsdatum) throws RemoteException;
    void mahnungLoeschen(String mahnungsID) throws RemoteException;

}
