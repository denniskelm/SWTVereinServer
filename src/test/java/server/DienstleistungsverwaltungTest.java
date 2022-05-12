package server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class DienstleistungsverwaltungTest {

    private ArrayList<Dienstleistungsgesuch> gesuche;



    @BeforeEach
    void init(){
        gesuche = new ArrayList<>();
    }

    @Test
    void gesuchErstellenTest() {

        String expectedGesuchID = "dg00001";


    }

    @Test
    void angebotErstellen() {
    }

    @Test
    void gesuchLoeschen() {
    }

    @Test
    void angebotLoeschen() {
    }

    @Test
    void gesuchAendern() {
    }

    @Test
    void angebotAendern() {
    }

    @Test
    void gesuchAnnehmen() {
    }

    @Test
    void angebotAnfragen() {
    }
}