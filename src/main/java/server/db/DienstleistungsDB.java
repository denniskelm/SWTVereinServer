package server.db;

import server.dienstleistungsmodul.Dienstleistungsangebot;
import server.dienstleistungsmodul.Dienstleistungsangebotdaten;
import server.dienstleistungsmodul.Dienstleistungsgesuch;
import server.dienstleistungsmodul.Dienstleistungsgesuchdaten;

import java.rmi.NoSuchObjectException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

/*
@author
Gabriel Kleebaum
 */

//Diese Klasse verbindet die Dienstleistungsverwaltung mit der Datenbank
public class DienstleistungsDB extends Database {
    private Connection conn;
    final LocalDateTime dummyDate = LocalDateTime.of(2022, 1, 1, 12, 0);

    public DienstleistungsDB() throws SQLException {
        super();
        conn = super.getConnection();
    }




        public ArrayList<Dienstleistungsangebot> getAngeboteArrayList() {
        ArrayList<Dienstleistungsangebot> angebote = new ArrayList<>();

        try {
            PreparedStatement getAngebote = conn.prepareStatement("SELECT * FROM dienstleistung WHERE Typ = 'A'");
            ResultSet result = getAngebote.executeQuery();

            while (result.next()) {
                String id = result.getString("DienstleistungsID");
                String titel = result.getString("Titel");
                String beschreibung = result.getString("Beschreibung");
                String kategorie = result.getString("Kategorie");
                Timestamp abDate = result.getTimestamp("Ab");
                Timestamp bisDate = result.getTimestamp("Bis");
                String imageUrl = result.getString("ImageUrl");
                String personenID = result.getString("PersonenID");

                Dienstleistungsangebot angebot = new Dienstleistungsangebot(id, titel, beschreibung, kategorie, abDate.toLocalDateTime(), bisDate.toLocalDateTime(), imageUrl, personenID);

                angebote.add(angebot);
            }

            getAngebote.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return angebote;
    }

    public ArrayList<Dienstleistungsgesuch> getGesucheArrayList() {
        ArrayList<Dienstleistungsgesuch> gesuche = new ArrayList<>();

        try {
            PreparedStatement getGesuche = conn.prepareStatement("SELECT * FROM dienstleistung WHERE Typ = 'G'");
            ResultSet result = getGesuche.executeQuery();

            while (result.next()) {
                String id = result.getString("DienstleistungsID"),
                        titel = result.getString("Titel"),
                        beschreibung = result.getString("Beschreibung"),
                        kategorie = result.getString("Kategorie"),
                        imageUrl = result.getString("ImageUrl"),
                        suchenderID = result.getString("PersonenID");

                Dienstleistungsgesuch gesuch = new Dienstleistungsgesuch(id, titel, beschreibung, kategorie, imageUrl, suchenderID);

                gesuche.add(gesuch);
            }

            getGesuche.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return gesuche;
    }

    public void reset(){
        try {
            resetTabelle("dienstleistung");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void resetTabelle(String tabelle) throws SQLException {
        PreparedStatement prep = conn.prepareStatement(String.format("DELETE FROM %s", tabelle));
        prep.executeUpdate();
        prep.close();
    }

    public void gesuchErstellen(Dienstleistungsgesuch gesuch) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO dienstleistung VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

            statement.setString(1, gesuch.getGesuch_ID());
            statement.setString(2, gesuch.getTitel());
            statement.setString(3, gesuch.getBeschreibung());
            statement.setString(4, gesuch.getKategorie());
            statement.setString(5, "G");
            statement.setString(6, dummyDate.toString());   //Dummy-Datum, da hier kein Datum benötigt wird
            statement.setString(7, dummyDate.toString());   //Dummy-Datum...
            statement.setString(8, gesuch.getImageUrl());
            statement.setString(9, gesuch.getSuchender());

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void angebotErstellen(Dienstleistungsangebot angebot) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO dienstleistung VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

            statement.setString(1, angebot.getAngebots_ID());
            statement.setString(2, angebot.getTitel());
            statement.setString(3, angebot.getBeschreibung());
            statement.setString(4, angebot.getKategorie());
            statement.setString(5, "A");
            statement.setString(6, angebot.getTime1().toString());
            statement.setString(7, angebot.getTime2().toString());
            statement.setString(8, angebot.getImageUrl());
            statement.setString(9, angebot.getPersonenID());

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void gesuchLoeschen(String gesuchID) {
        try {
            //aus DB dienstleistungsgesuchanfragen loeschen
            PreparedStatement delAnfragen = conn.prepareStatement("DELETE FROM dienstleistungsgesuchanfragen WHERE DienstleistungsID = ? LIMIT 1");
            delAnfragen.setString(1, gesuchID);
            delAnfragen.executeUpdate();
            delAnfragen.close();

            //aus DB dienstleistungsgesucherstellt loeschen
            PreparedStatement delErstellt = conn.prepareStatement("DELETE FROM dienstleistungsgesucherstellt WHERE DienstleistungsID = ? LIMIT 1");
            delErstellt.setString(1, gesuchID);
            delErstellt.executeUpdate();
            delErstellt.close();

            //aus DB dienstleistung loeschen
            PreparedStatement delDienstleistung = conn.prepareStatement("DELETE FROM dienstleistung WHERE DienstleistungsID = ? LIMIT 1");
            delDienstleistung.setString(1, gesuchID);
            delDienstleistung.executeUpdate();
            delDienstleistung.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void angebotLoeschen(String angebotsID) {
        try {
            //aus DB dienstleistungsgesuchanfragen loeschen
            PreparedStatement delAnfragen = conn.prepareStatement("DELETE FROM dienstleistungsangebotanfragen WHERE DienstleistungsID = ? LIMIT 1");
            delAnfragen.setString(1, angebotsID);
            delAnfragen.executeUpdate();
            delAnfragen.close();

            //aus DB dienstleistungsgesucherstellt loeschen
            PreparedStatement delErstellt = conn.prepareStatement("DELETE FROM dienstleistungsangeboterstellt WHERE DienstleistungsID = ? LIMIT 1");
            delErstellt.setString(1, angebotsID);
            delErstellt.executeUpdate();
            delErstellt.close();

            //aus DB dienstleistung loeschen
            PreparedStatement delDienstleistung = conn.prepareStatement("DELETE FROM dienstleistung WHERE DienstleistungsID = ? LIMIT 1");
            delDienstleistung.setString(1, angebotsID);
            delDienstleistung.executeUpdate();
            delDienstleistung.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void gesuchAendern(String gesuchsID, Dienstleistungsgesuchdaten attr, Object wert) {
        String spalte = "";

        try {
            switch (attr) {
                case GESUCH_ID -> spalte = "DienstleistungsID";
                case TITEL -> spalte = "Titel";
                case BESCHREIBUNG -> spalte = "Beschreibung";
                case KATEGORIE -> spalte = "Kategorie";
                case SUCHENDER_ID -> spalte = "PersonenID";
                case URL -> spalte = "ImageUrl";
                default -> throw new NoSuchObjectException("Wert " + attr + " nicht gefunden!");
            }

            PreparedStatement statement = conn.prepareStatement("UPDATE dienstleistung SET %s = ? WHERE DienstleistungsID = ? LIMIT 1".formatted(spalte));

            statement.setString(1, wert.toString());
            statement.setString(2, gesuchsID);

            statement.executeUpdate();
        } catch (SQLException | NoSuchObjectException e) {
            throw new RuntimeException(e);
        }
    }

    public void angebotAendern(String angebotsID, Dienstleistungsangebotdaten attr, Object wert) {
        String spalte;

        try {
            spalte = switch (attr) {
                case ANGEBOTS_ID -> "DienstleistungsID";
                case TITEL -> "Titel";
                case BESCHREIBUNG -> "Beschreibung";
                case KATEGORIE -> "Kategorie";
                case AB -> "Ab";
                case BIS -> "Bis";
                case PERSONEN_ID -> "PersonenID";
                case URL -> "ImageUrl";
                default -> throw new NoSuchObjectException("Wert " + attr + " nicht gefunden!");
            };

            PreparedStatement statement = conn.prepareStatement("UPDATE dienstleistung SET %s = ? WHERE DienstleistungsID = ?".formatted(spalte));

            statement.setString(1, wert.toString());
            statement.setString(2, angebotsID);

            statement.executeUpdate();
        } catch (SQLException | NoSuchObjectException e) {
            throw new RuntimeException(e);
        }
    }

    public void gesuchAnnhemen(String gesuchsID, String erstellerID, String nutzerID, int stunden) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO dienstleistungsgesuchanfragen VALUES (?, ?, ?)");
            statement.setString(1, gesuchsID);
            statement.setString(2, nutzerID);
            statement.setString(3, Integer.toString(stunden));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void angebotAnnehmen(String angebotsID, String erstellerID, String nutzerID, int stunden) {
        try {
            PreparedStatement statement = conn.prepareStatement("INSERT INTO dienstleistungsangebotanfragen VALUES (?, ?, ?)");
            statement.setString(1, angebotsID);
            statement.setString(2, erstellerID);
            statement.setString(3, Integer.toString(stunden));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}