package server;
/*
@author
TODO Raphael Kleebaum
TODO Jonny Schlutter
Gabriel Kleebaum
TODO Mhd Esmail Kanaan
TODO Gia Huy Hans Tran
TODO Ole Björn Adelmann
TODO Bastian Reichert
Dennis Kelm
*/

import server.users.Rollenverwaltung;
import shared.communication.IDienstleistungsverwaltung;
import shared.communication.IGeraeteverwaltung;
import shared.communication.IMahnungsverwaltung;
import shared.communication.IRollenverwaltung;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

//TODO WAS MACHT DIESE KLASSE?
public class RMIServerTest {

    public static void main(String[] args) {
        Geraeteverwaltung geraeteverwaltung = new Geraeteverwaltung();
        Dienstleistungsverwaltung dienstleistungsverwaltung = new Dienstleistungsverwaltung();
        Mahnungsverwaltung mahnungsverwaltung = new Mahnungsverwaltung();
        Rollenverwaltung rollenverwaltung = new Rollenverwaltung();

        //Klassen zur Kommunikation mit dem Server vorbereiten
        //System.setProperty("java.rmi.server.hostname", "meta.informatik.uni-rostock.de");
        System.setProperty("java.rmi.server.hostname", "127.0.0.1");

        try {
            //Objekte ins Interface exportieren - jedes Objekt
            IGeraeteverwaltung gVerwaltungInterface = (IGeraeteverwaltung) UnicastRemoteObject.exportObject(geraeteverwaltung, 0);
            IDienstleistungsverwaltung dVerwaltungInterface = (IDienstleistungsverwaltung) UnicastRemoteObject.exportObject(dienstleistungsverwaltung, 0);
            IMahnungsverwaltung mVerwaltungInterface = (IMahnungsverwaltung) UnicastRemoteObject.exportObject(mahnungsverwaltung, 0);
            IRollenverwaltung rVerwaltungInterface = (IRollenverwaltung) UnicastRemoteObject.exportObject(rollenverwaltung, 0);

            //Einmalig - Objekte im Registry registrieren, damit RMI vom Client aus ausgeführt werden kann
            Registry registry = LocateRegistry.createRegistry(1234);

            //Interfaces mit Namen auf Server registrieren, damit der Client sie finden kann - jedes Objekt
            registry.bind("Geraeteverwaltung", gVerwaltungInterface);
            registry.bind("Dienstleistungsverwaltung", dVerwaltungInterface);
            registry.bind("Mahnungsverwaltung", mVerwaltungInterface);
            registry.bind("Rollenverwaltung", rVerwaltungInterface);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        } catch (AlreadyBoundException e) { //Sollte nicht weiter schlimm sein
            System.err.println(e.getMessage());
        }
    }

}
