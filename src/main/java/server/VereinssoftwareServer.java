package server;

import server.dienstleistungsmodul.Anfragenliste;
import server.dienstleistungsmodul.Dienstleistungsverwaltung;
import server.geraetemodul.Geraeteverwaltung;
import server.users.Rollenverwaltung;
import shared.communication.*;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * @author Gabriel Kleebaum
 * @author Dennis Kelm
 * Initiert den Server der Vereinsserver - startet die Serverinstanz und kuemmert sich vorallem um RMI
 */
public class VereinssoftwareServer {
    public static Geraeteverwaltung geraeteverwaltung;
    public static Dienstleistungsverwaltung dienstleistungsverwaltung;
    public static Mahnungsverwaltung mahnungsverwaltung;

    //public static Anfragenliste anfragenliste;
    public static Rollenverwaltung rollenverwaltung = new Rollenverwaltung();

    public static void main(String[] args) {
        geraeteverwaltung = new Geraeteverwaltung();
        dienstleistungsverwaltung = new Dienstleistungsverwaltung();
        mahnungsverwaltung = new Mahnungsverwaltung();
        rollenverwaltung = new Rollenverwaltung();
        //anfragenliste = new Anfragenliste();

        //Klassen zur Kommunikation mit dem Server vorbereiten
        System.setProperty("java.rmi.server.hostname", "meta.informatik.uni-rostock.de");
        //System.setProperty("java.rmi.server.hostname", "127.0.0.1");

        try {
            //Objekte ins Interface exportieren - jedes Objekt
            IGeraeteverwaltung gVerwaltungInterface = (IGeraeteverwaltung) UnicastRemoteObject.exportObject(geraeteverwaltung, 0);
            IDienstleistungsverwaltung dVerwaltungInterface = (IDienstleistungsverwaltung) UnicastRemoteObject.exportObject(dienstleistungsverwaltung, 0);
            IMahnungsverwaltung mVerwaltungInterface = (IMahnungsverwaltung) UnicastRemoteObject.exportObject(mahnungsverwaltung, 0);
            IRollenverwaltung rVerwaltungInterface = (IRollenverwaltung) UnicastRemoteObject.exportObject(rollenverwaltung, 0);
            //IAnfragenliste rAnfragelisteInterface = (IAnfragenliste) UnicastRemoteObject.exportObject(anfragenliste, 0);

            //Einmalig - Objekte im Registry registrieren, damit RMI vom Client aus ausgeführt werden kann
            Registry registry = LocateRegistry.createRegistry(1234);

            //Interfaces mit Namen auf Server registrieren, damit der Client sie finden kann - jedes Objekt
            registry.bind("Geraeteverwaltung", gVerwaltungInterface);
            registry.bind("Dienstleistungsverwaltung", dVerwaltungInterface);
            registry.bind("Mahnungsverwaltung", mVerwaltungInterface);
            registry.bind("Rollenverwaltung", rVerwaltungInterface);
            //registry.bind("Anfragenliste", rAnfragelisteInterface);

            System.out.println("Server erfolgreich gestartet!");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (AlreadyBoundException e) { //Sollte nicht weiter schlimm sein
            System.err.println(e.getMessage());
        }
    }

}
