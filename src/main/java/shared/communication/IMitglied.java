package shared.communication;


import server.dienstleistungsmodul.Anfragenliste;
import server.users.Personendaten;

import java.rmi.RemoteException;
import java.time.LocalDateTime;

public interface IMitglied extends IGast {

    void reservierungenErhöhen() throws RemoteException;
    void reservierungenVerringern() throws RemoteException;
    int getReservierungen() throws RemoteException;
    void veraendereStundenkonto(int change) throws RemoteException;
    boolean isGesperrt() throws RemoteException;
    Anfragenliste getAnfragenliste() throws RemoteException;
    LocalDateTime getMitgliedSeit() throws RemoteException;
    void datenVerwalten(Personendaten attr, String wert) throws RemoteException;

}
