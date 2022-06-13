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

    //todo reset methode machen
    /*
    public void reset() {
        try {
            resetTabelle("historie");
            resetTabelle("reserviert");
            resetTabelle("geraet");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

     */



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
            PreparedStatement statement = conn.prepareStatement("DELETE FROM ? WHERE DienstleistungsID = ? LIMIT 1");
            statement.setString(2, gesuchID);

            //aus DB dienstleistungsgesuchanfragen loeschen
            statement.setString(1, "dienstleistungsgesuchanfragen");
            statement.executeUpdate();

            //aus DB dienstleistungsgesucherstellt loeschen
            statement.setString(1, "dienstleistungsgesucherstellt");
            statement.executeUpdate();

            //aus DB dienstleistung loeschen
            statement.setString(1, "dienstleistung");
            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void angebotLoeschen(String angebotsID) {
        try {
            PreparedStatement statement = conn.prepareStatement("DELETE FROM ? WHERE DienstleistungsID = ? LIMIT 1");
            statement.setString(2, angebotsID);

            //aus DB dienstleistungsangebotanfragen loeschen
            statement.setString(1, "dienstleistungsangebotanfragen");
            statement.executeUpdate();

            //aus DB dienstleistungsangeboterstellt loeschen
            statement.setString(1, "dienstleistungsangeboterstellt");
            statement.executeUpdate();

            //aus DB dienstleistung loeschen
            statement.setString(1, "dienstleistung");
            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void gesuchAendern(String gesuchsID, Dienstleistungsgesuchdaten attr, Object wert) {
        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE dienstleistung SET ? = ? WHERE DienstleistungsID = ? LIMIT 1");

            switch (attr) {
                case GESUCH_ID -> statement.setString(1, "DienstleistungsID");
                case TITEL -> statement.setString(1, "Titel");
                case BESCHREIBUNG -> statement.setString(1, "Beschreibung");
                case KATEGORIE -> statement.setString(1, "Kategorie");
                case SUCHENDER_ID -> statement.setString(1, "PersonenID");
                case URL -> statement.setString(1, "ImageUrl");
                default -> throw new NoSuchObjectException("Wert " + attr + " nicht gefunden!");
            }

            statement.setString(2, wert.toString());
            statement.setString(3, gesuchsID);

            statement.executeUpdate();
        } catch (SQLException | NoSuchObjectException e) {
            throw new RuntimeException(e);
        }
    }

    public void angebotAendern(String angebotsID, Dienstleistungsangebotdaten attr, Object wert) {
        String wertAsString = wert.toString();
        Timestamp wertAsTime = Timestamp.valueOf(wertAsString);

        try {
            PreparedStatement statement = conn.prepareStatement("UPDATE dienstleistung SET ? = ? WHERE DienstleistungsID = ?");

            switch (attr) {
                case ANGEBOTS_ID:
                    statement.setString(1, "DienstleistungsID");
                    statement.setString(2, wertAsString);
                    break;
                case TITEL:
                    statement.setString(1, "Titel");
                    statement.setString(2, wertAsString);
                    break;
                case BESCHREIBUNG:
                    statement.setString(1, "Beschreibung");
                    statement.setString(2, wertAsString);
                    break;
                case KATEGORIE:
                    statement.setString(1, "Kategorie");
                    statement.setString(2, wertAsString);
                    break;
                case AB:
                    statement.setString(1, "Ab");
                    statement.setString(2, wertAsTime.toString());
                    break;
                case BIS:
                    statement.setString(1, "Bis");
                    statement.setString(2, wertAsTime.toString());
                    break;
                case PERSONEN_ID:
                    statement.setString(1, "PersonenID");
                    statement.setString(2, wertAsString);
                    break;
                case URL:
                    statement.setString(1, "ImageUrl");
                    statement.setString(2, wertAsString);
                    break;
                default:
                    throw new NoSuchObjectException("Wert " + attr + " nicht gefunden!");
            }
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