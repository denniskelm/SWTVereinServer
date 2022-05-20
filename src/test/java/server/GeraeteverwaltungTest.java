package server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.geraetemodul.*;
import server.users.Mitglied;
import server.users.Personendaten;
import server.users.Rollenverwaltung;

import javax.naming.NoPermissionException;
import java.rmi.NoSuchObjectException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GeraeteverwaltungTest {

    private static final Geraeteverwaltung gv = new Geraeteverwaltung();
    private static final server.users.Rollenverwaltung rv = VereinssoftwareServer.rollenverwaltung;

    @BeforeAll
    static void mitglieder() {
        rv.mitgliedHinzufuegen("Mustermann","Max","bsp@gmx.de","12345","anschrift","mitgliedsnr",1234567890,true,LocalDateTime.now());
        rv.mitgliedHinzufuegen("Schmidt","Peter","schmidt@gmx.de","54321","anschrift","mitgliedsnr",987654321,true,LocalDateTime.now());
    }

    @BeforeEach
    void setUp() throws NoSuchObjectException {
        rv.fetch("1").datenVerwalten(Personendaten.RESERVIERUNGEN, String.valueOf(0));
        rv.fetch("2").datenVerwalten(Personendaten.RESERVIERUNGEN, String.valueOf(0));
        gv.reset();
        gv.geraetHinzufuegen("Ball", "spender", 15, "Spielzeug", "beschreibung", "abholort");
        gv.geraetHinzufuegen("Bohrer", "spender", 15, "Werkzeug", "beschreibung", "abholort");
    }

    @Test
    void geraetHinzufuegen() throws NoSuchObjectException {
        String Id = gv.geraetHinzufuegen("Schere", "spender", 15, "Werkzeug", "beschreibung", "abholort");

        assertEquals("g00001", gv.fetch("g00001").getGeraeteID());
        assertEquals("g00003", Id);
    }

    @Test
    void geraetEntfernen() throws NoSuchObjectException {

        assertEquals(gv.getGeraeteArrayList().size(),2);

        gv.geraetEntfernen("g00002");

        Throwable exception = assertThrows(NoSuchObjectException.class, () -> gv.fetch("g00002"));
        assertEquals("Geraet mit ID: g00002 nicht vorhanden.", exception.getMessage());

        assertEquals(gv.getGeraeteArrayList().size(),1);

    }

    @Test
    void fetch() throws NoSuchObjectException {
        Throwable exception = assertThrows(NoSuchObjectException.class, () -> gv.fetch("g00000"));
        assertEquals("Geraet mit ID: g00000 nicht vorhanden.", exception.getMessage());

        Geraet g = gv.fetch("g00002");

        assertEquals("g00002", g.getGeraeteID());
        assertEquals("spender", g.getSpenderName());
        assertEquals(15, g.getLeihfrist());
    }

    @Test
    void geraetReservieren() throws Exception {
        Geraet g = gv.fetch("g00001");
        ArrayList<Ausleiher> rl = g.getReservierungsliste();

        Assertions.assertEquals(Status.FREI, g.getLeihstatus());
        assertEquals(0,rv.fetch("1").getReservierungen());

        Throwable exception1 = assertThrows(NoSuchObjectException.class, () -> gv.geraetReservieren("g00000","1"));
        assertEquals("Geraet mit ID: g00000 nicht vorhanden.", exception1.getMessage());

        Throwable exception2 = assertThrows(NoSuchObjectException.class, () -> gv.geraetReservieren("g00001","0"));
        assertEquals("Person mit ID: 0 nicht vorhanden.", exception2.getMessage());

        gv.geraetReservieren("g00001","1");
        gv.geraetReservieren("g00001","2");
        assertEquals(1,rv.fetch("1").getReservierungen());
        assertEquals(Status.BEANSPRUCHT, g.getLeihstatus());
        assertEquals(rl.get(0).getFristBeginn().toLocalDate(), LocalDate.now());
        assertEquals(rl.get(1).getFristBeginn().toLocalDate(), rl.get(0).getFristBeginn().plusDays(g.getLeihfrist()).toLocalDate());

        Throwable exception3 = assertThrows(Exception.class, () -> gv.geraetReservieren("g00001","1"));
        assertEquals("Mitglied hat das Geraet bereits reserviert.", exception3.getMessage());

        gv.geraetHinzufuegen("Schere", "spender", 15, "Werkzeug", "beschreibung", "abholort");
        gv.geraetReservieren("g00002","1");
        gv.geraetReservieren("g00003","1");
        Throwable exception4 = assertThrows(ArrayIndexOutOfBoundsException.class, () -> gv.geraetReservieren("g00001","1"));
        assertEquals("Mitglied hat bereits 3 oder mehr Reservierungen.", exception4.getMessage());

        rv.fetch("1").datenVerwalten(Personendaten.IST_GESPERRT, String.valueOf(true));
        Throwable exception5 = assertThrows(NoPermissionException.class, () -> gv.geraetReservieren("g00001","1"));
        assertEquals("Mitglied ist gesperrt.", exception5.getMessage());
        rv.fetch("1").datenVerwalten(Personendaten.IST_GESPERRT, String.valueOf(false));
    }

    @Test
    void reservierungStornieren() throws Exception {
        Geraet g = gv.fetch("g00001");
        ArrayList<Ausleiher> rl = g.getReservierungsliste();


        gv.geraetReservieren("g00001","1");
        gv.geraetReservieren("g00001","2");
        assertEquals(2,rl.size());


        gv.reservierungStornieren("g00001","1");
        assertEquals(Status.BEANSPRUCHT, g.getLeihstatus());
        assertEquals(1,rl.size());
        assertEquals("2", rl.get(0).getMitlgiedsID());
        assertEquals(rl.get(0).getFristBeginn().toLocalDate(), LocalDate.now());

        gv.reservierungStornieren("g00001","2");
        assertEquals(Status.FREI, g.getLeihstatus());
        assertEquals(0,rl.size());

        gv.geraetReservieren("g00001","1");
        gv.geraetAusgeben("g00001");
        Throwable exception = assertThrows(Exception.class, () -> gv.reservierungStornieren("g00001","1"));
        assertEquals("Geraet ist momentan von dir ausgeliehen.", exception.getMessage());

    }

    @Test
    void geraetAusgeben() throws Exception {
        Geraet g = gv.fetch("g00001");

        Throwable exception1 = assertThrows(Exception.class, () -> gv.geraetAusgeben("g00001"));
        assertEquals("keine Reservierung vorhanden.", exception1.getMessage());

        gv.geraetReservieren("g00001","1");
        gv.geraetAusgeben("g00001");
        assertEquals(g.getLeihstatus(),Status.AUSGELIEHEN);

        Throwable exception2 = assertThrows(Exception.class, () -> gv.geraetAusgeben("g00001"));
        assertEquals("ausgeliehenes/freies Geraet kann nicht ausgegeben werden.", exception2.getMessage());
    }

    @Test
    void geraetAnnehmen() throws Exception {
        Geraet g = gv.fetch("g00001");
        ArrayList<Ausleiher> rl = g.getReservierungsliste();

        Throwable exception1 = assertThrows(Exception.class, () -> gv.geraetAnnehmen("g00001"));
        assertEquals("keine Reservierung vorhanden.", exception1.getMessage());

        gv.geraetReservieren("g00001","1");
        gv.geraetReservieren("g00001","2");
        assertEquals(0, g.getHistorie().size());
        assertEquals(2, g.getReservierungsliste().size());
        Throwable exception2 = assertThrows(Exception.class, () -> gv.geraetAnnehmen("g00001"));
        assertEquals("beanspruchtes/freies Geraet kann nicht angenommen werden.", exception2.getMessage());

        gv.geraetAusgeben("g00001");
        gv.geraetAnnehmen("g00001");
        assertEquals(Status.BEANSPRUCHT, g.getLeihstatus());
        assertEquals(1, g.getHistorie().size());
        assertEquals(1, g.getReservierungsliste().size());
        //assertEquals(LocalDate.now(), rl.get(0).getFristBeginn().toLocalDate());

        gv.geraetAusgeben("g00001");
        gv.geraetAnnehmen("g00001");
        assertEquals(Status.FREI, g.getLeihstatus());
        assertEquals(2, g.getHistorie().size());
        assertEquals(0, g.getReservierungsliste().size());
    }


    @Test
    void geraeteDatenVerwalten() throws NoSuchObjectException {
        Geraet g = gv.fetch("g00001");

        Object newNAME = "newNAME";
        gv.geraeteDatenVerwalten("g00001", Geraetedaten.NAME , newNAME);
        assertEquals(g.getGeraetName(),newNAME);

        Object newSPENDERNAME = "newSPENDERNAME";
        gv.geraeteDatenVerwalten("g00001", Geraetedaten.SPENDERNAME , newSPENDERNAME);
        assertEquals(g.getGeraetName(),newNAME);

        Object newLEIHFRIST = 20;
        gv.geraeteDatenVerwalten("g00001", Geraetedaten.LEIHFRIST , newLEIHFRIST);
        assertEquals(g.getLeihfrist(),newLEIHFRIST);

        Object newBESCHREIBUNG = "newBESCHREIBUNG";
        gv.geraeteDatenVerwalten("g00001", Geraetedaten.BESCHREIBUNG , newBESCHREIBUNG);
        assertEquals(g.getGeraetBeschreibung(),newBESCHREIBUNG);

        Object newABHOLORT = "newABHOLORT";
        gv.geraeteDatenVerwalten("g00001", Geraetedaten.ABHOLORT , newABHOLORT);
        assertEquals(g.getGeraetAbholort(),newABHOLORT);
    }

    @Test
    void historieZuruecksetzen() throws Exception {
        Geraet g = gv.fetch("g00001");

        assertEquals(0, g.getHistorie().size());

        gv.geraetReservieren("g00001","1");
        gv.geraetAusgeben("g00001");
        gv.geraetAnnehmen("g00001");
        assertEquals(1, g.getHistorie().size());

        gv.historieZuruecksetzen("g00001");
        assertEquals(0, g.getHistorie().size());
    }

    @Test
    void geraeteAnzeigen() throws NoSuchObjectException {
        ArrayList<Geraet> gl = (ArrayList<Geraet>) gv.geraeteAnzeigen();
        assertEquals(2, gl.size());
    }

    @Test
    void geraeteDatenAusgeben() throws Exception {
        Geraet g = gv.fetch("g00001");
        gv.geraetReservieren("g00001","1");

        String st =   gv.geraeteDatenAusgeben("g00001");
        assertEquals("ID: g00001, Name: Ball, Spender: spender, Leihfrist: 15, Kategorie: Spielzeug, Beschreibung: beschreibung, Abholort: abholort",st);
    }
}