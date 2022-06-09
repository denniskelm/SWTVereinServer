package server.db;
/*
@author
Raphael Kleebaum
//TODO Jonny Schlutter
//TODO Gabriel Kleebaum
//TODO Mhd Esmail Kanaan
//TODO Gia Huy Hans Tran
//TODO Ole Björn Adelmann
//TODO Bastian Reichert
//TODO Dennis Kelm
*/

import server.Mahnung;
import server.Mahnungsverwaltung;
import server.VereinssoftwareServer;
import server.users.*;

import java.rmi.NoSuchObjectException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static server.users.Rolle.*;

public class RollenDB {

    private final Connection conn;
    private final Rollenverwaltung rv;

    public RollenDB() {
        final String URL = "meta.informatik.uni-rostock.de";
        final String USER = "rootuser", PASSWORD = "rootuser";
        rv = VereinssoftwareServer.rollenverwaltung;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + URL + ":3306/vswt22", USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void gastHinzufuegen(Gast g) {

        try {

            String spender = g.getSpenderStatus() ? "Y" : "N";

            PreparedStatement prep = conn.prepareStatement("INSERT INTO gast VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            prep.setString(1, g.getPersonenID());
            prep.setString(2, g.getMitgliedsNr());
            prep.setString(3, g.getAnschrift());
            prep.setString(4, g.getEmail());
            prep.setString(5, spender);
            prep.setString(6, g.getTelefonNr());
            prep.setInt(7, g.getPassword());
            prep.setString(8, g.getVorname());
            prep.setString(9, g.getNachname());

            prep.executeUpdate();
            prep.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void MitgliedHinzufuegen(Mitglied m) {

        try {
            PreparedStatement istGastVorhanden = conn.prepareStatement("SELECT * FROM gast WHERE MitgliedsNr = ?");
            istGastVorhanden.setString(1, m.getMitgliedsNr());

            ResultSet gastVorhandenResult = istGastVorhanden.executeQuery();

            if (!gastVorhandenResult.next()) // Gast muss erst erstellt werden
                gastHinzufuegen(m);

            String gesperrt = m.isGesperrt() ? "Y" : "N";

            zuDBHinzufuegen(m.getPersonenID(), m.getMitgliedSeit(), gesperrt, "mitglied");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void rolleAendern(String personenID, Rolle rolle) {
        String[] dbs = {"vorstand", "mitarbeiter", "mitglied", "gast"};
        String dbFound = null;
        LocalDateTime mitglied_seit;
        Mahnungsverwaltung mahnungsverwaltung = null;
        Gast g;

        try {
            try {
                g = rv.fetch(personenID);

                PreparedStatement getMitgliedSeit = conn.prepareStatement("SELECT Mitglied_seit FROM mitglied WHERE PersonenID = ?");
                getMitgliedSeit.setString(1, personenID);

                ResultSet mitgliedSeit = getMitgliedSeit.executeQuery();
                mitgliedSeit.next();

                mitglied_seit = mitgliedSeit.getTimestamp(1).toLocalDateTime();
                mahnungsverwaltung = getMahnungsVerwaltungOfUser(personenID);

            } catch (NoSuchObjectException e) {
                g = rv.fetchGaeste(personenID);
                dbFound = "gast";
                mitglied_seit = LocalDateTime.now();
            }


            // herausfinden welchen Rang er hat
            if (dbFound == null) {
                for (String db : dbs) {
                    PreparedStatement getCorrectTable = conn.prepareStatement(String.format("SELECT * FROM %s WHERE PersonenID = ?", db));
                    getCorrectTable.setString(1, personenID);
                    ResultSet correctTable = getCorrectTable.executeQuery();

                    try {
                        correctTable.next();
                        if (correctTable.getString("PersonenID").equals(personenID)) {
                            dbFound = db;
                            break;
                        }
                    } catch (SQLException e) {
                        continue;
                    }
                }
            }

            switch (dbFound) {
                // TODO richtige Mahnungsverwaltung für Mitarbeiter & Vorsitz finden
                case "gast" -> g = new Gast(personenID, g.getNachname(), g.getVorname(), g.getEmail(), g.getPassword(), g.getAnschrift(), g.getMitgliedsNr(), g.getTelefonNr(), g.getSpenderStatus());
                case "mitglied" -> g = new Mitglied(personenID, g.getNachname(), g.getVorname(), g.getEmail(), g.getPassword(), g.getAnschrift(), g.getMitgliedsNr(), g.getTelefonNr(), g.getSpenderStatus(), mahnungsverwaltung, mitglied_seit);
                case "mitarbeiter" -> g = new Mitarbeiter(personenID, g.getNachname(), g.getVorname(), g.getEmail(), g.getPassword(), g.getAnschrift(), g.getMitgliedsNr(), g.getTelefonNr(), g.getSpenderStatus(), mitglied_seit, mahnungsverwaltung);
                case "vorstand" -> g = new Vorsitz(personenID, g.getNachname(), g.getVorname(), g.getEmail(), g.getPassword(), g.getAnschrift(), g.getMitgliedsNr(), g.getTelefonNr(), g.getSpenderStatus(), mitglied_seit, mahnungsverwaltung);
            }

            if (g.getClass() == rolle.getKlasse())
                throw new Exception("Der Nutzer hat diese Rolle bereits.");

            rolleAendernSwitch(g, rolle, mitglied_seit);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void rolleAendernSwitch(Gast gast, Rolle rolle, LocalDateTime mitglied_seit) throws SQLException {

        if (rolle == GAST) {
            gastHinzufuegen(gast);
            ausDBEntfernen(gast.getPersonenID(), "vorstand");
            ausDBEntfernen(gast.getPersonenID(), "mitarbeiter");
            ausDBEntfernen(gast.getPersonenID(), "mitglied");
            return;
        }

        // Wird für alles hierunter gebraucht
        if (gast.getClass() == GAST.getKlasse())
            zuDBHinzufuegen(gast.getPersonenID(), mitglied_seit, "N", "mitglied");

        // TODO wo wird das Stundenkonto gespeichert?

        if (rolle == MITGLIED) {
            if (gast.getClass() == MITARBEITER.getKlasse())
                ausDBEntfernen(gast.getPersonenID(), "mitarbeiter");
            else if (gast.getClass() == VORSITZ.getKlasse()) {
                ausDBEntfernen(gast.getPersonenID(), "vorstand");
                ausDBEntfernen(gast.getPersonenID(), "mitarbeiter");
            }
            return;
        }

        // Wird für alles hierunter gebraucht
        if (gast.getClass() == MITGLIED.getKlasse())
            zuDBHinzufuegen(gast.getPersonenID(), "mitarbeiter");

        if (rolle == MITARBEITER) {
            if (gast.getClass() == VORSITZ.getKlasse())
                ausDBEntfernen(gast.getPersonenID(), "vorstand");
            return;
        }

        if (rolle == VORSITZ) {
            if (gast.getClass() == MITARBEITER.getKlasse())
                zuDBHinzufuegen(gast.getPersonenID(), "vorstand");
        }

    }

    public void nutzerEintragAendern(String personenID, Personendaten attr, String wert) {

        try {
            PreparedStatement prep = null;
            if (attr != Personendaten.IST_GESPERRT && attr != Personendaten.RESERVIERUNGEN) { // muss in Mitglied geändert werden
                prep = conn.prepareStatement(String.format("UPDATE gast SET %s = ? WHERE PersonenID = ?", attr.toString()));
            } else if (attr == Personendaten.IST_GESPERRT) {
                prep = conn.prepareStatement("UPDATE mitglied SET ist_gesperrt = ? WHERE PersonenID = ?");
            }

            if (attr.equals(Personendaten.PASSWORD))
                prep.setInt(1, Integer.parseInt(wert));
            else
                prep.setString(1, wert);

            prep.setString(2, personenID);

            prep.executeUpdate();
            prep.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void reset() {
        try {
            resetTabelle("vorstand");
            resetTabelle("mitarbeiter");
            resetTabelle("mitglied");
            resetTabelle("gast");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getIdCounter() {

        try {
            PreparedStatement prep = conn.prepareStatement("SELECT COUNT(*) FROM gast");
            ResultSet result = prep.executeQuery();
            result.next();

            return result.getInt(1);

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }

    public ArrayList<Gast> getGaeste() {
        ArrayList<Gast> gaeste;
        Gast g;
        String personenID, nachname, vorname, email, anschrift, mitgliedsNr, telefonNr;
        int password;
        boolean spender;

        gaeste = new ArrayList<>();

        try {

            // Gäste erstellen
            PreparedStatement getGaeste = conn.prepareStatement("SELECT * FROM gast");
            ResultSet result = getGaeste.executeQuery();

            while (result.next()) {
                personenID = result.getString("PersonenID");
                nachname = result.getString("Nachname");
                vorname = result.getString("Vorname");
                email = result.getString("E-Mail");
                password = result.getInt("Passwort");
                anschrift = result.getString("Anschrift");
                mitgliedsNr = result.getString("MitgliedsNr");
                telefonNr = result.getString("Telefonnummer");
                spender = result.getString("ist_spender").equals("Y");

                g = new Gast(personenID, nachname, vorname, email, password, anschrift, mitgliedsNr, telefonNr, spender);

                gaeste.add(g);
            }

            getGaeste.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return gaeste;
    }

    public ArrayList<Mitglied> getMitglieder() {
        ArrayList<Mitglied> mitglieder;
        Mitglied m;
        LocalDateTime mitglied_seit;
        Mahnungsverwaltung mahnungsverwaltung;

        mitglieder = new ArrayList<>();

        try {
            // Mitglieder erstellen
            PreparedStatement getMitglieder = conn.prepareStatement("SELECT PersonenID FROM mitglied");
            ResultSet result = getMitglieder.executeQuery();

            ArrayList<String> mitgliederIDs = new ArrayList<>();

            while (result.next()) {
                mitgliederIDs.add(result.getString(1));
            }

            ArrayList<Gast> mitgliederAlsGaeste = getGaesteWithIDs(mitgliederIDs);

            for (Gast g : mitgliederAlsGaeste) {
                mitglied_seit = getMitgliedSeit(g.getPersonenID());
                mahnungsverwaltung = getMahnungsVerwaltungOfUser(g.getPersonenID());

                m = new Mitglied(g.getPersonenID(), g.getNachname(), g.getVorname(), g.getEmail(), g.getPassword(),
                        g.getAnschrift(), g.getMitgliedsNr(), g.getTelefonNr(), g.getSpenderStatus(),
                        mahnungsverwaltung, mitglied_seit);

                mitglieder.add(m);
            }

            getMitglieder.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return mitglieder;
    }

    public ArrayList<Mitarbeiter> getMitarbeiter() {
        ArrayList<Mitarbeiter> mitarbeiter;
        Mitarbeiter m;
        LocalDateTime mitglied_seit;
        Mahnungsverwaltung mahnungsverwaltung;

        mitarbeiter = new ArrayList<>();

        try {
            // Mitarbeiter erstellen
            PreparedStatement getMitarbeiter = conn.prepareStatement("SELECT PersonenID FROM mitarbeiter");
            ResultSet result = getMitarbeiter.executeQuery();

            ArrayList<String> vorstandIDs = new ArrayList<>();

            while (result.next()) {
                vorstandIDs.add(result.getString(1));
            }

            ArrayList<Gast> mitarbeiterAlsGaeste = getGaesteWithIDs(vorstandIDs);

            for (Gast g : mitarbeiterAlsGaeste) {
                mitglied_seit = rv.fetch(g.getPersonenID()).getMitgliedSeit();
                mahnungsverwaltung = getMahnungsVerwaltungOfUser(g.getPersonenID());

                m = new Mitarbeiter(g.getPersonenID(), g.getNachname(), g.getVorname(), g.getEmail(), g.getPassword(),
                        g.getAnschrift(), g.getMitgliedsNr(), g.getTelefonNr(), g.getSpenderStatus(),
                        mitglied_seit, mahnungsverwaltung);

                mitarbeiter.add(m);
            }

            getMitarbeiter.close();

        } catch (SQLException | NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

        return mitarbeiter;
    }

    public ArrayList<Vorsitz> getVorsitze() {
        ArrayList<Vorsitz> vorsitze;
        Vorsitz v;
        LocalDateTime mitglied_seit;
        Mahnungsverwaltung mahnungsverwaltung;

        vorsitze = new ArrayList<>();

        try {
            // Vorsitze erstellen
            PreparedStatement getVorsitze = conn.prepareStatement("SELECT PersonenID FROM vorstand");
            ResultSet result = getVorsitze.executeQuery();

            ArrayList<String> vorstandIDs = new ArrayList<>();

            while (result.next())
                vorstandIDs.add(result.getString(1));

            ArrayList<Gast> vorstaendeAlsGaeste = getGaesteWithIDs(vorstandIDs);

            for (Gast g : vorstaendeAlsGaeste) {
                mitglied_seit = rv.fetch(g.getPersonenID()).getMitgliedSeit();
                mahnungsverwaltung = getMahnungsVerwaltungOfUser(g.getPersonenID());

                v = new Vorsitz(g.getPersonenID(), g.getNachname(), g.getVorname(), g.getEmail(), g.getPassword(),
                        g.getAnschrift(), g.getMitgliedsNr(), g.getTelefonNr(), g.getSpenderStatus(),
                        mitglied_seit, mahnungsverwaltung);

                vorsitze.add(v);
            }

            getVorsitze.close();

        } catch (SQLException | NoSuchObjectException e) {
            throw new RuntimeException(e);
        }

        return vorsitze;
    }

    // für Mitarbeiter- und Vorstandstabellen
    private void zuDBHinzufuegen(String personenID, String tabelle) throws SQLException {
        PreparedStatement prep = conn.prepareStatement(String.format("INSERT INTO %S VALUES (?)", tabelle));
        prep.setString(1, personenID);
        prep.executeUpdate();
        prep.close();
    }

    // für Mitgliedertabelle
    private void zuDBHinzufuegen(String personenID, LocalDateTime mitglied_seit, String ist_gesperrt, String tabelle) throws SQLException {
        PreparedStatement prep = conn.prepareStatement(String.format("INSERT INTO %S VALUES (?, ?, ?)", tabelle));
        prep.setString(1, personenID);
        prep.setTimestamp(2, Timestamp.valueOf(mitglied_seit));
        prep.setString(3, ist_gesperrt);
        prep.executeUpdate();
        prep.close();
    }

    private void ausDBEntfernen(String personenID, String tabelle) throws SQLException {
        PreparedStatement prep = conn.prepareStatement(String.format("DELETE FROM %s WHERE PersonenID = ?", tabelle));
        prep.setString(1, personenID);
        prep.executeUpdate();
        prep.close();
    }

    private void resetTabelle(String tabelle) throws SQLException {
        PreparedStatement prep = conn.prepareStatement(String.format("DELETE FROM %s", tabelle));
        prep.executeUpdate();
        prep.close();
    }

    private ArrayList<Gast> getGaesteWithIDs(ArrayList<String> ids) {
        ArrayList<Gast> gaeste = getGaeste(), result = new ArrayList<>();

        for (Gast g : gaeste) {
            if (ids.contains(g.getPersonenID()))
                result.add(g);
        }

        return result;
    }

    // TODO aus Mahnungsverwaltung holen
    private Mahnungsverwaltung getMahnungsVerwaltungOfUser(String personenID) throws SQLException {
        Mahnungsverwaltung mv;

        mv = new Mahnungsverwaltung();

        PreparedStatement prep = conn.prepareStatement("SELECT * FROM mahnung WHERE MitgliedID = ?");
        prep.setString(1, personenID);

        ResultSet result = prep.executeQuery();

        while (result.next()) {
            String mahnungID = result.getString("MahnungID"),
                    grund = result.getString("Grund"),
                    mitgliedID = result.getString("MitgliedID");
            LocalDateTime datum = result.getTimestamp("Verfallsdatum").toLocalDateTime();

            Mahnung m = new Mahnung(mahnungID, mitgliedID, grund, datum);

            mv.mahnungen.add(m);
        }

        return mv;
    }

    private LocalDateTime getMitgliedSeit(String personenID) throws SQLException {
        PreparedStatement getMitgliedSeit = conn.prepareStatement("SELECT Mitglied_seit FROM mitglied WHERE PersonenID = ?");
        getMitgliedSeit.setString(1, personenID);

        ResultSet mitgliedSeit = getMitgliedSeit.executeQuery();
        mitgliedSeit.next();

        return mitgliedSeit.getTimestamp(1).toLocalDateTime();
    }

}
