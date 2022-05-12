package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GeraeteverwaltungTest {

    private static ArrayList<Geraet> geraete;

    @BeforeEach
    void setUp() {
        geraet g = fetch();
        geraete.add(g);
    }

    @Test
    void geraetHinzufuegen() {
    }

    @Test
    void fetch() {
    }

    @Test
    void geraetReservieren() {
    }

    @Test
    void geraetAusgeben() {
    }

    @Test
    void geraetAnnehmen() {
    }

    @Test
    void geraetEntfernen() {
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