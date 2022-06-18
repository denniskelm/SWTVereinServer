package server;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.dienstleistungsmodul.*;
import server.geraetemodul.Geraet;
import server.geraetemodul.Geraetedaten;
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
        rv.mitgliedHinzufuegen("Mustermann","Max","bsp@gmx.de","12345","anschrift","mitgliedsnr","1234567890",true, LocalDateTime.now());
        rv.mitgliedHinzufuegen("Schmidt","Peter","schmidt@gmx.de","54321","anschrift","mitgliedsnr2","987654321",true, LocalDateTime.now());
    }

    @BeforeEach
    void setUp() throws Exception {
        rv.fetch("1").datenVerwalten(Personendaten.RESERVIERUNGEN, String.valueOf(0));
        rv.fetch("2").datenVerwalten(Personendaten.RESERVIERUNGEN, String.valueOf(0));
        dv.reset();
        dv.angebotErstellen("rasieren","Haar rasieren","Handwerk",ab,bis,"ImageURL","dg00001");
        dv.angebotErstellen("babysitten","babysitten","Housewife",ab,bis,"ImageURL","dg00002");

        dv.gesuchErstellen("Nachhilfe","NAchhilfe geben","Studium","ImageURl","gondula"); //warum ersteller name und nicht sein id
        dv.gesuchErstellen("putzen","Zimmer putzen","Housekeeping","ImageURl","Drakola");
    }

    static void reset() {
        dv.reset();
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
    void gesuchAendern() throws NoSuchObjectException { //todo bitte bescheid sagen wenn die BackendMethode fertig ist
        Dienstleistungsverwaltung dv = new Dienstleistungsverwaltung();


            Dienstleistungsgesuch dg= dv.fetchGesuch("dg00001");


        Object newGESUCH_ID = "newGESUCH_ID";
        dv.gesuchAendern("dg00001", Dienstleistungsgesuchdaten.GESUCH_ID, newGESUCH_ID);
        assertEquals(dg.getGesuch_ID(),newGESUCH_ID);

        Object newTITEL = "newTITEL";
        dv.gesuchAendern("dg00001", Dienstleistungsgesuchdaten.TITEL , newTITEL);
        assertEquals(dg.getTitel(),newTITEL);

        Object newBESCHREIBUNG = "newBESCHREIBUNG";
        dv.gesuchAendern("dg00001", Dienstleistungsgesuchdaten.BESCHREIBUNG , newBESCHREIBUNG);
        assertEquals(dg.getBeschreibung(),newBESCHREIBUNG);

        Object newKATEGORIE = "newKATEGORIE";
        dv.gesuchAendern("dg00001", Dienstleistungsgesuchdaten.KATEGORIE , newKATEGORIE);
        assertEquals(dg.getKategorie(),newKATEGORIE);

        Object newSUCHENDER_ID = "newSUCHENDER_ID";
        dv.gesuchAendern("dg00001", Dienstleistungsgesuchdaten.SUCHENDER_ID , newSUCHENDER_ID);
        assertEquals(dg.getSuchender(),newSUCHENDER_ID);

    }

    @Test
    void angebotAendern() throws NoSuchObjectException {

        Dienstleistungsverwaltung dv = new Dienstleistungsverwaltung();


        Dienstleistungsangebot da = dv.fetchAngebot("da00001");


        Object newANGEBOTS_ID = "newANGEBOTS_ID";
        dv.angebotAendern("da00001", Dienstleistungsangebotdaten.ANGEBOTS_ID, newANGEBOTS_ID);
        assertEquals(da.getAngebots_ID(),newANGEBOTS_ID);

        Object newTITEL = "newTITEL";
        dv.angebotAendern("da00001", Dienstleistungsangebotdaten.TITEL , newTITEL);
        assertEquals(da.getTitel(),newTITEL);

        Object newBESCHREIBUNG = "newBESCHREIBUNG";
        dv.angebotAendern("da00001", Dienstleistungsangebotdaten.BESCHREIBUNG , newBESCHREIBUNG);
        assertEquals(da.getBeschreibung(),newBESCHREIBUNG);

        Object newKATEGORIE = "newKATEGORIE";
        dv.angebotAendern("da00001", Dienstleistungsangebotdaten.KATEGORIE , newKATEGORIE);
        assertEquals(da.getKategorie(),newKATEGORIE);

        Object newAb = ab;
        dv.angebotAendern("da00001", Dienstleistungsangebotdaten.AB , newAb);
        assertEquals(da.getTime1(),newAb);

        Object newBis = bis;
        dv.angebotAendern("da00001", Dienstleistungsangebotdaten.BIS , newBis);
        assertEquals(da.getTime2(),newBis);

        Object newPERSONEN_ID = "newPERSONEN_ID";
        dv.angebotAendern("da00001", Dienstleistungsangebotdaten.PERSONEN_ID, newPERSONEN_ID);
        assertEquals(da.getPersonenID(),newPERSONEN_ID);

        Object newImageUrl = "newImageUrl";
        dv.angebotAendern("da00001", Dienstleistungsangebotdaten.URL, newImageUrl);
        assertEquals(da.getImageUrl(),newImageUrl);



    }

    @Test
    void gesuchAnnehmen() {


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


}