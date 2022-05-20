package server;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.users.*;

import java.rmi.NoSuchObjectException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RollenverwaltungTest {

    public static Rollenverwaltung rv = new Rollenverwaltung();

    @BeforeAll
   public static void mitglieder(){
        rv.gastHinzufuegen("Mustermann", "Max", "we.de@de.de", "hallo", "Am See 3", "123", 87772627, false);
        rv.mitgliedHinzufuegen("Musterfrau", "Maxi", "we.de@de.de", "hallo1", "Am See 32", "123", 87772627, false, LocalDateTime.parse("2016-11-09T11:44:44.797" ));

    }


    @BeforeEach
    void setUp() {

    }

    @Test
        public void gastHinzufuegen() throws NoSuchObjectException {

        rv.gastHinzufuegen("Adelmann", "Ole", "O.a@.de", "1234", "Haus am See 1", "6772571836", 11238, true);

        assertEquals("Adelmann",((Gast) rv.fetchGaeste("3")).getNachname());

        Assertions.assertThrows(NoSuchObjectException.class, () -> {
            rv.fetch("3");
        });
        }

    @Test
    public void MitgliedHinzufuegen() throws NoSuchObjectException {
        rv.mitgliedHinzufuegen("Mustermann", "Max", "we.de@de.de", "hallo", "Am See 3", "123", 87772627, false, LocalDateTime.parse("2016-11-09T11:44:44.797"));
        assertEquals("Mustermann", rv.fetch("3").getNachname());

        Assertions.assertThrows(NoSuchObjectException.class, () -> {
            rv.fetch("4");
        });
    }

    @Test
    public void fetch() throws NoSuchObjectException {

        assertEquals("Musterfrau", rv.fetch("2").getNachname());

        Assertions.assertThrows(NoSuchObjectException.class, () -> {
            rv.fetch("3");
        });
    }

    @Test
    public void RolleAendern() throws Exception {

        // Rolle von Gast zu Mitglied ändern
        rv.rolleAendern("1", Rolle.MITGLIED);
        Object[] mitglieder = rv.mitgliedListeAnzeigen();
        boolean exist = false;


        for(int i = 0; i < mitglieder.length; i++){
            if(((Mitglied)mitglieder[i]).getPersonenID().equals("1")) {
                exist = true;
            }
        }
        assertTrue(exist);

        // Rolle von Mitglied zum Mitarbeiter aendern
        Object[] mitarbeiter = rv.mitarbeiterListeAnzeigen();
        rv.rolleAendern("2", Rolle.MITARBEITER);
        boolean exist1 = false;

        for(int k = 0; mitarbeiter.length > k; k++){
            if(((Mitarbeiter) mitarbeiter[k]).getPersonenID() == "2"){
                exist1 = true;
            }
        }
        assertTrue(exist1);

        // Rolle von Mitarbeiter zum Vorstand aendern
        Object[] vorsitze = rv.vorsitzListeAnzeigen();
        rv.rolleAendern("2", Rolle.VORSITZ);
        boolean exist2 = false;

        for(int i = 0; vorsitze.length > i; i++){
            if(((Vorsitz) vorsitze[i]).getPersonenID() == "2"){
                exist2 = true;
            }
        }
        assertTrue(exist2);

        // Throws RuntimeException
        Assertions.assertThrows(RuntimeException.class, () -> {
           rv.rolleAendern("3", Rolle.GAST);
        });

        //Fehlerfall Nutzer hat diese Rolle schon

        Throwable exception = assertThrows(Exception.class, () -> rv.rolleAendern("1", Rolle.GAST));
        assertEquals("Der Nutzer hat diese Rolle bereits", exception.getMessage());

    }

    @Test
    public void nutzereintragAendern() throws NoSuchObjectException {

        rv.nutzereintragAendern("1", Personendaten.ANSCHRIFT, "Am See 2");
        assertEquals("Am See 2", rv.fetch("1").getAnschrift());
    }

    }
