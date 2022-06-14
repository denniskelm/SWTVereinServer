package server;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.dienstleistungsmodul.Dienstleistungsgesuch;
import server.dienstleistungsmodul.Dienstleistungsverwaltung;
import server.geraetemodul.Geraeteverwaltung;
import server.users.Personendaten;
import server.users.Rollenverwaltung;

import java.rmi.NoSuchObjectException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DienstleistungsverwaltungTest {

    private LocalDateTime ab = LocalDateTime.now();
    private LocalDateTime bis = LocalDateTime.now().plusDays(15);

    private ArrayList<Dienstleistungsgesuch> gesuche;
    private static final Dienstleistungsverwaltung dv = new Dienstleistungsverwaltung();
    private static final Rollenverwaltung rv = VereinssoftwareServer.rollenverwaltung;

    private static final Rollenverwaltung rvv = VereinssoftwareServer.rollenverwaltung;


    @BeforeAll
    static void mitglieder() {
        rv.mitgliedHinzufuegen("Mustermann","Max","bsp@gmx.de","12345","anschrift","mitgliedsnr","1234567890",true, new Mahnungsverwaltung(), LocalDateTime.now());
        rv.mitgliedHinzufuegen("Schmidt","Peter","schmidt@gmx.de","54321","anschrift","mitgliedsnr2","987654321",true, new Mahnungsverwaltung(), LocalDateTime.now());
    }

    @BeforeEach
    void setUp() throws Exception {
        rv.fetch("1").datenVerwalten(Personendaten.RESERVIERUNGEN, String.valueOf(0));
        rv.fetch("2").datenVerwalten(Personendaten.RESERVIERUNGEN, String.valueOf(0));
        //dv.reset();
        dv.angebotErstellen("rasieren","Haar rasieren","Handwerk",ab,bis,"ImageURL","dg00001");
        dv.angebotErstellen("babysitten","babysitten","Housewife",ab,bis,"ImageURL","dg00002");

        dv.gesuchErstellen("Nachhilfe","NAchhilfe geben","Studium","ImageURl","gondula"); //warum ersteller name und nicht sein id
        dv.gesuchErstellen("putzen","Zimmer putzen","Housekeeping","ImageURl","Drakola");
    }

    static void reset() {
        //dv.reset();
        rv.reset();
    }

    @Test
    void gesuchErstellenTest() {

        Dienstleistungsverwaltung dv = new Dienstleistungsverwaltung();
        String gesuchID ;
        try {
            gesuchID = dv.gesuchErstellen( "titel",  "beschreibung",  "kategorie",  "imageUrl",  "ersteller");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String expectedGesuchID = "dg00001";

        assertEquals(expectedGesuchID,gesuchID);

        int size = dv.getGesucheArrayList().size();

        assertEquals(1,size);


    }

    @Test
    void angebotErstellen() {


        Dienstleistungsverwaltung dv = new Dienstleistungsverwaltung();



        String angebotID ;
        try {
            angebotID = dv.angebotErstellen("titel","beschreibung","kategorie",ab,bis,"imageUrl","00001");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String expectedAngebotID = "da00001";

        assertEquals(expectedAngebotID,"angebotID");


        int size = dv.getAngeboteArrayList().size();

        assertEquals(1,size);


    }

    @Test
    void gesuchLoeschen() {
        // check if gesuch is deleted

        Dienstleistungsverwaltung dv = new Dienstleistungsverwaltung();
        String gesuchID ;
        try {
            gesuchID = dv.gesuchErstellen( "titel",  "beschreibung",  "kategorie",  "imageUrl",  "ersteller");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println(gesuchID);
        dv.gesuchLoeschen("dg00001");

        int size = dv.getGesucheArrayList().size();

        assertEquals(0,size);


    }

    @Test
    void angebotLoeschen() {
        // check if angebot is deleted

        Dienstleistungsverwaltung dv = new Dienstleistungsverwaltung();


        LocalDateTime ab = LocalDateTime.now();
        LocalDateTime bis = LocalDateTime.now().plusDays(15);
        String angebotID ;
        try {
            //angebotID = dv.angebotErstellen( "titel",  "beschreibung",  "kategorie",  ab ,  bis ,  "personen_ID");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String expectedAngebotID = "da00001";

        assertEquals(expectedAngebotID,"angebotID");

        dv.angebotLoeschen("da00001");
        int size = dv.getAngeboteArrayList().size();
        assertEquals(0,size);


    }

    @Test
    void gesuchAendern() { //todo bitte bescheid sagen wenn die BackendMethode fertig ist
        // check if Attributs are chaged

    }

    @Test
    void angebotAendern() { //todo bitte bescheid sagen wenn die BackendMethode fertig ist
        // check if Attributs are chaged
    }

    @Test
    void gesuchAnnehmen() {  //todo bitte bescheid sagen wenn die BackendMethode fertig ist


        Dienstleistungsverwaltung dv = new Dienstleistungsverwaltung();
        String gesuchID ;
        try {
            gesuchID = dv.gesuchErstellen( "titel",  "beschreibung",  "kategorie",  "imageUrl",  "ersteller");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String expectedGesuchID = "dg00001";

        assertEquals(expectedGesuchID,gesuchID);

        int size = dv.getGesucheArrayList().size();

        assertEquals(1,size);

        try {
            dv.gesuchAnnehmen( "dg00001",  "ersteller_ID",  "nutzer_ID",  10);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }




    }

    @Test
    void angebotAnfragen() { //todo bitte bescheid sagen wenn die BackendMethode fertig ist
    }
}