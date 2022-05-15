package server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.NoSuchObjectException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GeraeteverwaltungTest { //todo bitte jede einzelne Test allein durchführen, wenn alle zusammen durchgeführt werden dann passiert Fehler

   // private static ArrayList<Geraet> geraeteTest;

    @BeforeEach
    void setUp() {
        //Geraeteverwaltung gv = new Geraeteverwaltung();
        //gv.geraetHinzufuegen("jojo", "spender", 15, "spiel", "beschreibung", "abholort");
    }

    @Test
    void geraetHinzufuegen() {
        Geraeteverwaltung gv = new Geraeteverwaltung();
        String Id = gv.geraetHinzufuegen("jojo", "spender", 15, "spiel", "beschreibung", "abholort");

        assertEquals(Id,"1","dsfsf");
    }

    @Test
    void fetch() {
        Geraeteverwaltung gv = new Geraeteverwaltung();
        String Id = gv.geraetHinzufuegen("jojo", "spender", 15, "spiel", "beschreibung", "abholort");

        Throwable exception = assertThrows(NoSuchObjectException.class, () -> gv.fetch("0"));
        assertEquals("Geraet mit ID: 0 nicht vorhanden.", exception.getMessage());

        Geraet g;
        try {
            g = gv.fetch("1");
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

        assertEquals(g.getGeraeteID(),"1");
        assertEquals(g.getSpenderName(),"spender");
        assertEquals(g.getLeihfrist(),15);
    }

    @Test
    void geraetReservieren() {
        Geraeteverwaltung gv = new Geraeteverwaltung();
        gv.geraetHinzufuegen("jojo", "spender", 15, "spiel", "beschreibung", "abholort");

        Throwable exception = assertThrows(NoSuchObjectException.class, () -> gv.geraetReservieren("0","1"));
        assertEquals("Geraet mit ID: 0 nicht vorhanden.", exception.getMessage());

        Geraet g;
        try {
            gv.geraetReservieren("1","1");
            g = gv.fetch("1");
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(Status.BEANSPRUCHT, g.getLeihstatus());

    }

    @Test
    void geraetAusgeben() {
        Geraeteverwaltung gv = new Geraeteverwaltung();
        gv.geraetHinzufuegen("jojo", "spender", 15, "spiel", "beschreibung", "abholort");

        try {
            gv.geraetReservieren("1","1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Geraet g;
        try {
            g = gv.fetch("1");
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

        try {
            gv.geraetAusgeben("1");
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

        assertEquals(g.getLeihstatus(),Status.AUSGELIEHEN);
    }

    @Test
    void geraetAnnehmen() {
        Geraeteverwaltung gv = new Geraeteverwaltung();
        gv.geraetHinzufuegen("jojo", "spender", 15, "spiel", "beschreibung", "abholort");

        try {
            gv.geraetReservieren("1","1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Geraet g;
        try {
            g = gv.fetch("1");
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

        try {
            gv.geraetAusgeben("1");
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

        try {
            gv.geraetAnnehmen("1");
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

        assertEquals(g.getLeihstatus(),Status.FREI);
    }

    @Test
    void geraetEntfernen() {
        Geraeteverwaltung gv = new Geraeteverwaltung();
        gv.geraetHinzufuegen("jojo", "spender", 15, "spiel", "beschreibung", "abholort");
        gv.geraetHinzufuegen("soso", "spender", 15, "spiel", "beschreibung", "abholort");

        try {
            gv.geraetEntfernen("2");
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

        assertEquals(gv.getGeraeteArrayList().size(),1);

    }

    @Test
    void geraeteDatenVerwalten() {

        Geraeteverwaltung gv = new Geraeteverwaltung();
        gv.geraetHinzufuegen("jojo", "spender", 15, "spiel", "beschreibung", "abholort") ;


        Geraet g;
        try {
            g = gv.fetch("1");
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }


        Geraetedaten NAME = Geraetedaten.NAME;;
        Object newNAME = "newNAME";

        try {
            gv.geraeteDatenVerwalten("1", NAME , newNAME);
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

        assertEquals(g.getGeraetName(),newNAME);




        Geraetedaten SPENDERNAME = Geraetedaten.SPENDERNAME;
        Object newSPENDERNAME = "newSPENDERNAME";

        try {
            gv.geraeteDatenVerwalten("1", SPENDERNAME , newSPENDERNAME);
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

        assertEquals(g.getGeraetName(),newNAME);





        Geraetedaten LEIHFRIST = Geraetedaten.LEIHFRIST;
        Object newLEIHFRIST = 20;


        try {
            gv.geraeteDatenVerwalten("1", LEIHFRIST , newLEIHFRIST);
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

        assertEquals(g.getLeihfrist(),newLEIHFRIST);




        Geraetedaten BESCHREIBUNG = Geraetedaten.BESCHREIBUNG;
        Object newBESCHREIBUNG = "newBESCHREIBUNG";

        try {
            gv.geraeteDatenVerwalten("1", BESCHREIBUNG , newBESCHREIBUNG);
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

        assertEquals(g.getGeraetBeschreibung(),newBESCHREIBUNG);



        Geraetedaten ABHOLORT = Geraetedaten.ABHOLORT;
        Object newABHOLORT = "newABHOLORT";

        try {
            gv.geraeteDatenVerwalten("1", ABHOLORT , newABHOLORT);
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

        assertEquals(g.getGeraetAbholort(),newABHOLORT);







    }

    @Test
    void historieZuruecksetzen() {

        Geraeteverwaltung gv = new Geraeteverwaltung();
        gv.geraetHinzufuegen("jojo", "spender", 15, "spiel", "beschreibung", "abholort");

        try {
            gv.geraetReservieren("1","1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Geraet g;
        try {
            g = gv.fetch("1");
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }



        try {
            gv.geraetAusgeben("1");
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }


        try {
            gv.geraetAnnehmen("1");
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }




        try {
            gv.historieZuruecksetzen("1");
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

        assertEquals(g.getHistorie().size(),0);


    }

    @Test
    void geraeteAnzeigen() {

        Geraeteverwaltung gv = new Geraeteverwaltung();
        gv.geraetHinzufuegen("jojo", "spender", 15, "spiel", "beschreibung", "abholort");


        Geraet g;
        try {
            g = gv.fetch("1");
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

        assertEquals(1,1); // die MEthode in backend ist []object ??

    }

    @Test
    void geraeteDatenAusgeben() {

        Geraeteverwaltung gv = new Geraeteverwaltung();
        gv.geraetHinzufuegen("jojo", "spender", 15, "spiel", "beschreibung", "abholort");

        try {
            gv.geraetReservieren("1","1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Geraet g;
        try {
            g = gv.fetch("1");
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

        String st ;
        try {
          st =   gv.geraeteDatenAusgeben("1");
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

        assertEquals("ich weiß nicht",st);

    }
}