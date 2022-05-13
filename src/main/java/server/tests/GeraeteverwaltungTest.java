package server.tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Geraet;
import server.Geraeteverwaltung;
import server.Status;

import java.rmi.NoSuchObjectException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GeraeteverwaltungTest {

    private static ArrayList<Geraet> geraete;

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

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> gv.fetch("0"));
        assertEquals("Geraet mit ID: 0 nicht vorhanden.", exception.getMessage());

        Geraet g = null;
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
        String Id = gv.geraetHinzufuegen("jojo", "spender", 15, "spiel", "beschreibung", "abholort");

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> gv.geraetReservieren("0","1"));
        assertEquals("Geraet mit ID: 0 nicht vorhanden.", exception.getMessage());

        Geraet g = null;
        try {
            g = gv.fetch("1");
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(g.getLeihstatus(), Status.BEANSPRUCHT);

    }

    @Test
    void geraetAusgeben() {
        Geraeteverwaltung gv = new Geraeteverwaltung();
        String Id = gv.geraetHinzufuegen("jojo", "spender", 15, "spiel", "beschreibung", "abholort");

        try {
            gv.geraetReservieren("1","1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Geraet g = null;
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
        String Id = gv.geraetHinzufuegen("jojo", "spender", 15, "spiel", "beschreibung", "abholort");

        try {
            gv.geraetReservieren("1","1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Geraet g = null;
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
        String Id = gv.geraetHinzufuegen("jojo", "spender", 15, "spiel", "beschreibung", "abholort");

        try {
            gv.geraetEntfernen("1");
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void geraeteDatenVerwalten() {
    }

    @Test
    void historieZuruecksetzen() {
    }

    @Test
    void geraeteAnzeigen() {
    }

    @Test
    void geraeteDatenAusgeben() {
    }
}