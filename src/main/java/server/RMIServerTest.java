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

import shared.communication.IGeraeteverwaltung;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

//TODO WAS MACHT DIESE KLASSE?
public class RMIServerTest {

    public static void main(String[] args) {
        Geraeteverwaltung geraeteverwaltung = new Geraeteverwaltung();

        //Klassen zur Kommunikation mit dem Server vorbereiten
        System.setProperty("java.rmi.server.hostname", "127.0.0.1");
        //TODO IP ist meta.informatik.uni-rostock.de
        try {
            //Objekte ins Interface exportieren - jedes Objekt
            IGeraeteverwaltung gVerwaltungInterface = (IGeraeteverwaltung) UnicastRemoteObject.exportObject(geraeteverwaltung, 0);


            //Einmalig - Objekte im Registry registrieren, damit RMI vom Client aus ausgeführt werden kann
            Registry registry = LocateRegistry.createRegistry(1234);

            registry.bind("Geraeteverwaltung", gVerwaltungInterface);
        } catch (RemoteException | AlreadyBoundException e) {
            throw new RuntimeException(e);
        }
    }

}
