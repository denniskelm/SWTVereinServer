package server.db;

import server.VereinssoftwareServer;
import server.dienstleistungsmodul.AngebotAnfrage;
import server.dienstleistungsmodul.Dienstleistungsangebot;
import server.dienstleistungsmodul.Dienstleistungsgesuch;
import server.dienstleistungsmodul.GesuchAnfrage;
import server.users.Mitglied;

import java.rmi.NoSuchObjectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AnfragenDB extends Database {
    private Connection conn;

    public AnfragenDB() throws SQLException {
        super();
        this.conn = getConnection();
    }

    /**
     * Liest alle Gesuchanfragen an die Person, welche die angegebene PersonenID besitzt
     */
    public ArrayList<GesuchAnfrage> getGesuchanfragenForId(Mitglied person) {
        ArrayList<GesuchAnfrage> gesuchAnfragen = new ArrayList<>();

        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM dienstleistungsgesuchanfragen");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                //Werte aus DB auslesen
                String dienstleistungsId = resultSet.getString("DienstleistungsID");
                int stunden = resultSet.getInt("Stunden");

                //Das Dienstleistungsgesuch erstellen
                Dienstleistungsgesuch gesuch = getGesuchFromId(dienstleistungsId);

                //Sicherstellen, dass das Gesuch von der Person mit der Anfragenliste ist
                if(gesuch == null || !gesuch.getSuchender().equals(person.getPersonenID()))
                    continue;

                //GesuchAnfrage-Objekt erstellen
                GesuchAnfrage anfrage = new GesuchAnfrage(
                        dienstleistungsId,
                        person,
                        fetchGesuch(dienstleistungsId),
                        stunden
                );

                //Anfrage der Liste hinzufügen
                gesuchAnfragen.add(anfrage);
            }

            statement.close();

            //Liste zurueckgeben
            return gesuchAnfragen;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dienstleistungsgesuch getGesuchFromId(String gesuchId) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM dienstleistung WHERE DienstleistungsID = ? AND Typ = 'G' LIMIT 1");
            statement.setString(1, gesuchId);

            ResultSet result = statement.executeQuery();
            Dienstleistungsgesuch gesuch = null;

            if(result.next()) {
                gesuch = new Dienstleistungsgesuch(
                        gesuchId,
                        result.getString("Titel"),
                        result.getString("Beschreibung"),
                        result.getString("Kategorie"),
                        result.getString("ImageUrl"),
                        result.getString("PersonenID")
                );
            }

            statement.close();

            return gesuch;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dienstleistungsangebot getAngebotFromId(String angebotId) {
        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM dienstleistung WHERE DienstleistungsID = ? AND Typ = 'A' LIMIT 1");
            statement.setString(1, angebotId);

            ResultSet result = statement.executeQuery();
            Dienstleistungsangebot angebot = null;

            if(result.next()) {
                angebot = new Dienstleistungsangebot(
                        angebotId,
                        result.getString("Titel"),
                        result.getString("Beschreibung"),
                        result.getString("Kategorie"),
                        result.getTimestamp("Ab").toLocalDateTime(),
                        result.getTimestamp("Bis").toLocalDateTime(),
                        result.getString("ImageUrl"),
                        result.getString("PersonenID")
                );
            }

            statement.close();

            return angebot;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<AngebotAnfrage> getAngebotanfragenForId(Mitglied person){
        ArrayList<AngebotAnfrage> angebotAnfragen = new ArrayList<>();

        try {
            PreparedStatement statement = conn.prepareStatement("SELECT * FROM dienstleistungsgesuchanfragen");

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                //Werte aus DB auslesen
                String dienstleistungsId = resultSet.getString("DienstleistungsID");
                int stunden = resultSet.getInt("Stunden");

                //Das Dienstleistungsgesuch erstellen
                Dienstleistungsangebot angebot = getAngebotFromId(dienstleistungsId);

                //Sicherstellen, dass das Gesuch von der Person mit der Anfragenliste ist
                if(angebot == null || !angebot.getPersonenID().equals(person.getPersonenID()))
                    continue;

                //AngebotAnfrage-Objekt erstellen
                AngebotAnfrage anfrage = new AngebotAnfrage(
                        dienstleistungsId,
                        person,
                        //VereinssoftwareServer.rollenverwaltung.fetch(personenId),
                        VereinssoftwareServer.dienstleistungsverwaltung.fetchAngebot(dienstleistungsId),
                        stunden
                );

                //Anfrage der Liste hinzufügen
                angebotAnfragen.add(anfrage);
            }

            statement.close();

            //Liste zurueckgeben
            return angebotAnfragen;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NoSuchObjectException e) {
            throw new RuntimeException(e);
        }
    }

    public void addaAnfrage(AngebotAnfrage a) {
        try {
            //Statement erstellen
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO dienstleistungsangebotanfragen " +
                            "(DienstleistungsID, PersonenID, Stunden) " +
                            "VALUES (?, ?, ?)"
            );

            //Variablen einsetzen
            statement.setString(1, a.angebot.getAngebots_ID());
            statement.setString(2, a.nutzer.getPersonenID());
            statement.setString(3, Integer.toString(a.stunden));

            //Ausführen und schließen
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addgAnfrage(GesuchAnfrage g) {
        try {
            //Statement erstellen
            PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO dienstleistungsgesuchanfragen " +
                            "(DienstleistungsID, PersonenID, Stunden) " +
                            "VALUES (?, ?, ?)"
            );

            //Variablen einsetzen
            statement.setString(1, g.gesuch.getGesuch_ID());
            statement.setString(2, g.nutzer.getPersonenID());
            statement.setInt(3, g.stunden);

            //Ausführen und schließen
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeaAnfrage(AngebotAnfrage a) {
        try {
            //Statement erstellen
            PreparedStatement statement = conn.prepareStatement("DELETE FROM dienstleistungsangebotanfragen WHERE DienstleistungsID = ? AND PersonenID = ? LIMIT 1");

            //Variablen einsetzen
            statement.setString(1, a.angebot.getAngebots_ID());
            statement.setString(2, a.nutzer.getPersonenID());

            //Ausführen und schließen
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removegAnfrage(GesuchAnfrage g) {
        try {
            //Statement erstellen
            PreparedStatement statement = conn.prepareStatement("DELETE FROM dienstleistungsgesuchanfragen WHERE DienstleistungsID = ? AND PersonenID = ? LIMIT 1");

            //Variablen einsetzen
            statement.setString(1, g.gesuch.getGesuch_ID());
            statement.setString(2, g.nutzer.getPersonenID());

            //Ausführen und schließen
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Dienstleistungsgesuch fetchGesuch(String dienstleistungsId) {

        try {
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM dienstleistung WHERE DienstleistungsID = ?");
            prep.setString(1, dienstleistungsId);

            ResultSet result = prep.executeQuery();
            result.next();

            return new Dienstleistungsgesuch(dienstleistungsId,
                                             result.getString("Titel"),
                                             result.getString("Beschreibung"),
                                             result.getString("Kategorie"),
                                             result.getString("ImageUrl"),
                                             result.getString("PersonenID"));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
