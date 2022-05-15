package server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.rmi.NoSuchObjectException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GeraeteverwaltungTest {

    private static Geraeteverwaltung gv = new Geraeteverwaltung();

    @BeforeEach
    void setUp() {
        gv.reset();
        gv.geraetHinzufuegen("Ball", "spender", 15, "Spielzeug", "beschreibung", "abholort");
        gv.geraetHinzufuegen("Bohrer", "spender", 15, "Werkzeug", "beschreibung", "abholort");
    }

    @Test
    void geraetHinzufuegen() throws NoSuchObjectException {
        String Id = gv.geraetHinzufuegen("Schere", "spender", 15, "Werkzeug", "beschreibung", "abholort");

        assertEquals("1", gv.fetch("1").getGeraeteID());
        assertEquals("3", Id);
    }

    @Test
    void geraetEntfernen() throws NoSuchObjectException {

        assertEquals(gv.getGeraeteArrayList().size(),2);

        gv.geraetEntfernen("2");

        Throwable exception = assertThrows(NoSuchObjectException.class, () -> gv.fetch("2"));
        assertEquals("Geraet mit ID: 2 nicht vorhanden.", exception.getMessage());

        assertEquals(gv.getGeraeteArrayList().size(),1);

    }

    @Test
    void fetch() throws NoSuchObjectException {
        Throwable exception = assertThrows(NoSuchObjectException.class, () -> gv.fetch("0"));
        assertEquals("Geraet mit ID: 0 nicht vorhanden.", exception.getMessage());

        Geraet g = gv.fetch("2");

        assertEquals("2", g.getGeraeteID());
        assertEquals("spender", g.getSpenderName());
        assertEquals(15, g.getLeihfrist());
    }

    @Test
    void geraetReservieren() throws Exception { // TODO erweitern sobald Rollenverwaltung funktioniert
        Geraet g = gv.fetch("1");
        ArrayList<Ausleiher> rl = g.getReservierungsliste();

        Assertions.assertEquals(Status.FREI, g.getLeihstatus());

        Throwable exception = assertThrows(NoSuchObjectException.class, () -> gv.geraetReservieren("0","1"));
        assertEquals("Geraet mit ID: 0 nicht vorhanden.", exception.getMessage());

        gv.geraetReservieren("1","1");
        gv.geraetReservieren("1","2");
        assertEquals(Status.BEANSPRUCHT, g.getLeihstatus());
        assertEquals(rl.get(0).getFristBeginn().toLocalDate(), LocalDate.now());
        assertEquals(rl.get(1).getFristBeginn().toLocalDate(), rl.get(0).getFristBeginn().plusDays(g.getLeihfrist()).toLocalDate());
    }

    @Test
    void geraetAusgeben() throws Exception {
        Geraet g = gv.fetch("1");

        Throwable exception1 = assertThrows(Exception.class, () -> gv.geraetAusgeben("1"));
        assertEquals("keine Reservierung vorhanden.", exception1.getMessage());

        gv.geraetReservieren("1","1");
        gv.geraetAusgeben("1");
        assertEquals(g.getLeihstatus(),Status.AUSGELIEHEN);

        Throwable exception2 = assertThrows(Exception.class, () -> gv.geraetAusgeben("1"));
        assertEquals("ausgeliehenes/freies Geraet kann nicht ausgegeben werden.", exception2.getMessage());
    }

    @Test
    void geraetAnnehmen() throws Exception {
        Geraet g = gv.fetch("1");
        ArrayList<Ausleiher> rl = g.getReservierungsliste();

        Throwable exception1 = assertThrows(Exception.class, () -> gv.geraetAnnehmen("1"));
        assertEquals("keine Reservierung vorhanden.", exception1.getMessage());

        gv.geraetReservieren("1","1");
        gv.geraetReservieren("1","2");
        assertEquals(0, g.getHistorie().size());
        assertEquals(2, g.getReservierungsliste().size());
        Throwable exception2 = assertThrows(Exception.class, () -> gv.geraetAnnehmen("1"));
        assertEquals("beanspruchtes/freies Geraet kann nicht angenommen werden.", exception2.getMessage());

        gv.geraetAusgeben("1");
        gv.geraetAnnehmen("1");
        assertEquals(Status.BEANSPRUCHT, g.getLeihstatus());
        assertEquals(1, g.getHistorie().size());
        assertEquals(1, g.getReservierungsliste().size());
        //assertEquals(LocalDate.now(), rl.get(0).getFristBeginn().toLocalDate());

        gv.geraetAusgeben("1");
        gv.geraetAnnehmen("1");
        assertEquals(Status.FREI, g.getLeihstatus());
        assertEquals(2, g.getHistorie().size());
        assertEquals(0, g.getReservierungsliste().size());
    }


    @Test
    void geraeteDatenVerwalten() throws NoSuchObjectException {
        Geraet g = gv.fetch("1");

        Object newNAME = "newNAME";
        gv.geraeteDatenVerwalten("1", Geraetedaten.NAME , newNAME);
        assertEquals(g.getGeraetName(),newNAME);

        Object newSPENDERNAME = "newSPENDERNAME";
        gv.geraeteDatenVerwalten("1", Geraetedaten.SPENDERNAME , newSPENDERNAME);
        assertEquals(g.getGeraetName(),newNAME);

        Object newLEIHFRIST = 20;
        gv.geraeteDatenVerwalten("1", Geraetedaten.LEIHFRIST , newLEIHFRIST);
        assertEquals(g.getLeihfrist(),newLEIHFRIST);

        Object newBESCHREIBUNG = "newBESCHREIBUNG";
        gv.geraeteDatenVerwalten("1", Geraetedaten.BESCHREIBUNG , newBESCHREIBUNG);
        assertEquals(g.getGeraetBeschreibung(),newBESCHREIBUNG);

        Object newABHOLORT = "newABHOLORT";
        gv.geraeteDatenVerwalten("1", Geraetedaten.ABHOLORT , newABHOLORT);
        assertEquals(g.getGeraetAbholort(),newABHOLORT);
    }

    @Test
    void historieZuruecksetzen() throws Exception {
        Geraet g = gv.fetch("1");

        assertEquals(0, g.getHistorie().size());

        gv.geraetReservieren("1","1");
        gv.geraetAusgeben("1");
        gv.geraetAnnehmen("1");
        assertEquals(1, g.getHistorie().size());

        gv.historieZuruecksetzen("1");
        assertEquals(0, g.getHistorie().size());
    }

    @Test
    void geraeteAnzeigen() throws NoSuchObjectException {
        ArrayList<Geraet> gl = gv.geraeteAnzeigen();
        assertEquals(2, gl.size());
    }

    @Test
    void geraeteDatenAusgeben() throws Exception {
        Geraet g = gv.fetch("1");
        gv.geraetReservieren("1","1");

        String st =   gv.geraeteDatenAusgeben("1");
        assertEquals("ID: 1, Name: Ball, Spender: spender, Leihfrist: 15, Kategorie: Spielzeug, Beschreibung: beschreibung, Abholort: abholort",st);
    }
}