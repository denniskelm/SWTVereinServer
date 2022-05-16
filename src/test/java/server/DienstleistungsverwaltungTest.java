package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DienstleistungsverwaltungTest {

    private ArrayList<Dienstleistungsgesuch> gesuche;




    @BeforeEach
    void init(){
        gesuche = new ArrayList<>();
    }

    @Test
    void gesuchErstellenTest() {
        // check if the id correct
        // check if gesuch is erstellt

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
        // check if the id correct
        // check if angebot is made

        Dienstleistungsverwaltung dv = new Dienstleistungsverwaltung();


        LocalDateTime ab = LocalDateTime.now();
        LocalDateTime bis = LocalDateTime.now().plusDays(15);
        String angebotID ;
        try {
            angebotID = dv.angebotErstellen( "titel",  "beschreibung",  "kategorie",  ab ,  bis ,  "personen_ID");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String expectedAngebotID = "da00001";

        assertEquals(expectedAngebotID,angebotID);


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
            angebotID = dv.angebotErstellen( "titel",  "beschreibung",  "kategorie",  ab ,  bis ,  "personen_ID");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        String expectedAngebotID = "da00001";

        assertEquals(expectedAngebotID,angebotID);

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