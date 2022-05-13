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
        // check if the id correct
        // check if gesuch is erstellt

        String expectedGesuchID = "dg00001";


    }

    @Test
    void angebotErstellen() {
        // check if the id correct
        // check if angebot is made

    }

    @Test
    void gesuchLoeschen() {
        // check if gesuch is deleted
    }

    @Test
    void angebotLoeschen() {
        // check if angebot is deleted
    }

    @Test
    void gesuchAendern() {
        // check if Attributs are chaged

    }

    @Test
    void angebotAendern() {
        // check if Attributs are chaged
    }

    @Test
    void gesuchAnnehmen() {
    }

    @Test
    void angebotAnfragen() {
    }
}