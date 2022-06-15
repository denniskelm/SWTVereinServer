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

    @BeforeAll
    static void mitglieder() {
        rv.gastHinzufuegen("Mustermann", "Max", "web.de@de.de", "hallo", "Am See 3", "123", "87772627", false);
        rv.mitgliedHinzufuegen("Musterfrau", "Maxi", "we.de@de.de", "hallo1", "Am See 32", "1234", "87772627", false, LocalDateTime.parse("2016-11-09T11:44:44.797"));

    }

    @AfterAll
    static void reset() {
        rv.reset();
    }

    @Test
    public void gastHinzufuegen() throws NoSuchObjectException {

        rv.gastHinzufuegen("Adelmann", "Ole", "O.a@.de", "1234", "Haus am See 1", "6772571836", "11238", true);

        assertEquals("Adelmann", ((Gast) rv.fetchGaeste("3")).getNachname());

        Assertions.assertThrows(NoSuchObjectException.class, () -> {
            rv.fetch("200");
        });
    }

    @Test
    public void MitgliedHinzufuegen() throws NoSuchObjectException {

        rv.mitgliedHinzufuegen("Mustermann", "Max", "we3.de@de.de", "hallo", "Am See 3", "12345", "87772627", false, LocalDateTime.parse("2016-11-09T11:44:44.797"));
        assertEquals("Mustermann", rv.fetch("4").getNachname());

        Assertions.assertThrows(NoSuchObjectException.class, () -> {
            rv.fetch("2000");
        });
    }

    @Test
    public void fetch() throws NoSuchObjectException {

        assertEquals("Musterfrau", rv.fetch("2").getNachname());

        Assertions.assertThrows(NoSuchObjectException.class, () -> {
            rv.fetch("300");
        });
    }

    @Test
    public void RolleAendern() throws Exception {

        // Rolle von Gast zu Mitglied ändern
        rv.rolleAendern("4", Rolle.MITARBEITER);
        Object[] mitglieder = rv.mitgliedListeAnzeigen();
        boolean exist = false;


        for (int i = 0; i < mitglieder.length; i++) {
            if (((Mitglied) mitglieder[i]).getPersonenID().equals("4")) {
                exist = true;
            }
        }
        assertTrue(exist);

        // Rolle von Mitglied zum Mitarbeiter aendern
        rv.rolleAendern("2", Rolle.MITARBEITER);
        Object[] mitarbeiter = rv.mitarbeiterListeAnzeigen();
        boolean exist1 = false;

        for(int k = 0; mitarbeiter.length > k; k++){
            if(((Mitarbeiter) mitarbeiter[k]).getPersonenID().equals("2")){
                exist1 = true;
            }
        }
        assertTrue(exist1);

        // Rolle von Mitarbeiter zum Vorstand aendern
        rv.rolleAendern("4", Rolle.VORSITZ);
        Object[] vorsitze = rv.vorsitzListeAnzeigen();
        boolean exist2 = false;

        for(int j = 0; vorsitze.length > j; j++){
            if(((Vorsitz) vorsitze[j]).getPersonenID().equals("4")){
                exist2 = true;
            }
        }
        assertTrue(exist2);

        // Throws RuntimeException
        Assertions.assertThrows(RuntimeException.class, () -> {
           rv.rolleAendern("300", Rolle.GAST);
        });

        //Fehlerfall Nutzer hat diese Rolle schon
        Throwable exception = assertThrows(Exception.class, () -> rv.rolleAendern("1", Rolle.GAST));
        assertEquals("java.lang.Exception: Der Nutzer hat diese Rolle bereits.", exception.getMessage());

    }

        @Test
        public void nutzereintragAendern () throws NoSuchObjectException {

            rv.nutzereintragAendern("2", Personendaten.ANSCHRIFT, "Am See 2");
            assertEquals("Am See 2", rv.fetch("2").getAnschrift());
        }

        @Test
        public void login () throws Exception {

            // Richtige Anmeldedaten
            boolean eingeloggt = false;
            eingeloggt = rv.login("we.de@de.de", ("hallo1"));
            assertTrue(eingeloggt);

            // Falsche Anmeldedaten
            Throwable exception = assertThrows(Exception.class, () -> rv.login("1", "2"));
            assertEquals("E-Mail oder Passwort falsch!", exception.getMessage());
        }

    }
