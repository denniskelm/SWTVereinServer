package server;

import org.junit.jupiter.api.*;
import server.dienstleistungsmodul.AnfragenVerwaltung;
import server.geraetemodul.Geraeteverwaltung;
import server.users.*;

import java.rmi.NoSuchObjectException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
/*
@author
Ole Adelmann
 */

class RollenverwaltungTest {
    public static Rollenverwaltung rv = new Rollenverwaltung();
    public static int anzahlNutzer = rv.getGaeste().size() + rv.getMitglieder().size() + rv.getMitarbeiter().size() + rv.getVorsitze().size();

    @BeforeAll
    static void init() {
        if (!rv.getGaeste().isEmpty())
            anzahlNutzer--;
        if (!rv.getMitglieder().isEmpty())
            anzahlNutzer--;
        if (!rv.getMitarbeiter().isEmpty())
            anzahlNutzer--;
        if (!rv.getVorsitze().isEmpty())
            anzahlNutzer--;

        rv.gastHinzufuegen("Mustermann", "Max", "web.de@de.de", "hallo", "Am See 3", "123", "87772627", false);
        anzahlNutzer++;
        rv.mitgliedHinzufuegen("Musterfrau", "Maxi", "we.de@de.de", "hallo1", "Am See 32", "1234", "87772627", false, LocalDateTime.parse("2016-11-09T11:44:44.797"));
        anzahlNutzer++;
    }
    @AfterAll
    static void reset() {
        rv.nutzerEntfernen(konvertiereZuID(anzahlNutzer));
        rv.nutzerEntfernen(konvertiereZuID(anzahlNutzer - 1));
        rv.nutzerEntfernen(konvertiereZuID(anzahlNutzer - 2));
        rv.nutzerEntfernen(konvertiereZuID(anzahlNutzer - 3));
    }

    @Test
    public void gastHinzufuegen() throws NoSuchObjectException {

        rv.gastHinzufuegen("Adelmann", "Ole", "O.a@.de", "1234", "Haus am See 1", "6772571836", "11238", true);
        anzahlNutzer++;

        assertEquals("Adelmann", (rv.fetchGaeste(konvertiereZuID(anzahlNutzer))).getNachname());

        Assertions.assertThrows(NoSuchObjectException.class, () -> {
            rv.fetch("p50001");
        });
    }

    @Test
    public void MitgliedHinzufuegen() throws NoSuchObjectException {

        rv.mitgliedHinzufuegen("Mustermann", "Max", "we3.de@de.de", "hallo", "Am See 3", "12345", "87772627", false, LocalDateTime.parse("2016-11-09T11:44:44.797"));
        anzahlNutzer++;
        assertEquals("Mustermann", rv.fetch(konvertiereZuID(anzahlNutzer)).getNachname());

        Assertions.assertThrows(NoSuchObjectException.class, () -> {
            rv.fetch("p50001");
        });
    }

    @Test
    public void fetch() throws NoSuchObjectException {

        assertEquals("Musterfrau", rv.fetch(konvertiereZuID(anzahlNutzer - 2)).getNachname());

        Assertions.assertThrows(NoSuchObjectException.class, () -> {
            rv.fetch("p50001");
        });
    }

    @Test
    public void RolleAendern() throws Exception {
        boolean exist;

        // Rolle von Gast zu Mitglied ändern
        rv.rolleAendern(konvertiereZuID(anzahlNutzer), Rolle.MITARBEITER);
        Object[] mitglieder = rv.mitgliedListeAnzeigen();
        exist = false;


        for (Object mitglied : mitglieder) {
            if (((Mitglied) mitglied).getPersonenID().equals(konvertiereZuID(anzahlNutzer))) {
                exist = true;
                break;
            }
        }
        assertTrue(exist);

        // Rolle von Mitglied zum Mitarbeiter aendern
        rv.rolleAendern(konvertiereZuID(anzahlNutzer - 2), Rolle.MITARBEITER);
        Object[] mitarbeiter = rv.mitarbeiterListeAnzeigen();
        exist = false;

        for (Object m : mitarbeiter) {
            if (((Mitarbeiter) m).getPersonenID().equals(konvertiereZuID(anzahlNutzer - 2))) {
                exist = true;
                break;
            }
        }
        assertTrue(exist);

        // Rolle von Mitarbeiter zum Vorstand aendern
        rv.rolleAendern(konvertiereZuID(anzahlNutzer), Rolle.VORSITZ);
        Object[] vorsitze = rv.vorsitzListeAnzeigen();
        exist = false;

        for (Object vorsitz : vorsitze) {
            if (((Vorsitz) vorsitz).getPersonenID().equals(konvertiereZuID(anzahlNutzer))) {
                exist = true;
                break;
            }
        }
        assertTrue(exist);

        // Throws RuntimeException
        Assertions.assertThrows(RuntimeException.class, () -> {
            rv.rolleAendern("p50001", Rolle.GAST);
        });

        //Fehlerfall Nutzer hat diese Rolle schon
        Throwable exception = assertThrows(Exception.class, () -> rv.rolleAendern(konvertiereZuID(anzahlNutzer - 3), Rolle.GAST));
        assertEquals("java.lang.Exception: Der Nutzer hat diese Rolle bereits.", exception.getMessage());

    }

    @Test
    public void nutzereintragAendern () throws NoSuchObjectException {

        rv.nutzereintragAendern(konvertiereZuID(anzahlNutzer - 2), Personendaten.ANSCHRIFT, "Am See 2");
        assertEquals("Am See 2", rv.fetch(konvertiereZuID(anzahlNutzer - 2)).getAnschrift());
    }

    @Test
    public void login () throws Exception {

        // Richtige Anmeldedaten
        Object[] eingeloggt;
        eingeloggt = rv.login("we.de@de.de", "hallo1");
        assertNotNull(eingeloggt);

        // Falsche Anmeldedaten
        Throwable exception = assertThrows(Exception.class, () -> rv.login("1", "2"));
        assertEquals("E-Mail oder Passwort falsch!", exception.getMessage());
    }

    private static String konvertiereZuID(int nummer) {
        if (nummer < 10) return "p0000" + (nummer);
        else if (nummer < 100) return "p000" + (nummer);
        else if (nummer < 1000) return "p00" + (nummer);
        else if (nummer < 10000) return "p0" + (nummer);
        else return "p" + (nummer);
    }

}
