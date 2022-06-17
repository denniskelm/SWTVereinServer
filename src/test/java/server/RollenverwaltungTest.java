package server;

import org.junit.jupiter.api.*;
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
        rv.nutzerEntfernen(String.valueOf(anzahlNutzer));
        rv.nutzerEntfernen(String.valueOf(anzahlNutzer - 1));
        rv.nutzerEntfernen(String.valueOf(anzahlNutzer - 2));
        rv.nutzerEntfernen(String.valueOf(anzahlNutzer - 3));
    }

    @Test
    public void gastHinzufuegen() throws NoSuchObjectException {

        rv.gastHinzufuegen("Adelmann", "Ole", "O.a@.de", "1234", "Haus am See 1", "6772571836", "11238", true);
        anzahlNutzer++;

        assertEquals("Adelmann", (rv.fetchGaeste(String.valueOf(anzahlNutzer))).getNachname());

        Assertions.assertThrows(NoSuchObjectException.class, () -> {
            rv.fetch("50001");
        });
    }

    @Test
    public void MitgliedHinzufuegen() throws NoSuchObjectException {

        rv.mitgliedHinzufuegen("Mustermann", "Max", "we3.de@de.de", "hallo", "Am See 3", "12345", "87772627", false, LocalDateTime.parse("2016-11-09T11:44:44.797"));
        anzahlNutzer++;
        assertEquals("Mustermann", rv.fetch(String.valueOf(anzahlNutzer)).getNachname());

        Assertions.assertThrows(NoSuchObjectException.class, () -> {
            rv.fetch("50001");
        });
    }

    @Test
    public void fetch() throws NoSuchObjectException {

        assertEquals("Musterfrau", rv.fetch(String.valueOf(anzahlNutzer - 2)).getNachname());

        Assertions.assertThrows(NoSuchObjectException.class, () -> {
            rv.fetch("50001");
        });
    }

    @Test
    public void RolleAendern() throws Exception {
        boolean exist;

        // Rolle von Gast zu Mitglied ändern
        rv.rolleAendern(String.valueOf(anzahlNutzer), Rolle.MITARBEITER);
        Object[] mitglieder = rv.mitgliedListeAnzeigen();
        exist = false;


        for (Object mitglied : mitglieder) {
            if (((Mitglied) mitglied).getPersonenID().equals(String.valueOf(anzahlNutzer))) {
                exist = true;
                break;
            }
        }
        assertTrue(exist);

        // Rolle von Mitglied zum Mitarbeiter aendern
        rv.rolleAendern(String.valueOf(anzahlNutzer - 2), Rolle.MITARBEITER);
        Object[] mitarbeiter = rv.mitarbeiterListeAnzeigen();
        exist = false;

        for (Object m : mitarbeiter) {
            if (((Mitarbeiter) m).getPersonenID().equals(String.valueOf(anzahlNutzer - 2))) {
                exist = true;
                break;
            }
        }
        assertTrue(exist);

        // Rolle von Mitarbeiter zum Vorstand aendern
        rv.rolleAendern(String.valueOf(anzahlNutzer), Rolle.VORSITZ);
        Object[] vorsitze = rv.vorsitzListeAnzeigen();
        exist = false;

        for (Object vorsitz : vorsitze) {
            if (((Vorsitz) vorsitz).getPersonenID().equals(String.valueOf(anzahlNutzer))) {
                exist = true;
                break;
            }
        }
        assertTrue(exist);

        // Throws RuntimeException
        Assertions.assertThrows(RuntimeException.class, () -> {
            rv.rolleAendern("50001", Rolle.GAST);
        });

        //Fehlerfall Nutzer hat diese Rolle schon
        Throwable exception = assertThrows(Exception.class, () -> rv.rolleAendern(String.valueOf(anzahlNutzer - 3), Rolle.GAST));
        assertEquals("java.lang.Exception: Der Nutzer hat diese Rolle bereits.", exception.getMessage());

    }

    @Test
    public void nutzereintragAendern () throws NoSuchObjectException {

        rv.nutzereintragAendern(String.valueOf(anzahlNutzer - 2), Personendaten.ANSCHRIFT, "Am See 2");
        assertEquals("Am See 2", rv.fetch(String.valueOf(anzahlNutzer - 2)).getAnschrift());
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

}
