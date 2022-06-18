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

import server.geraetemodul.Mahnung;
import server.VereinssoftwareServer;
import server.users.*;

import java.rmi.NoSuchObjectException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static server.users.Rolle.*;

public class RollenDB extends Database {

    private final Connection conn;
    private final Rollenverwaltung rv;

    public RollenDB() throws SQLException {
        super();

        conn = super.getConnection();
        rv = VereinssoftwareServer.rollenverwaltung;
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
        String dbFound = null;
        LocalDateTime mitglied_seit;
        Gast g;

        try {
            try {
                g = rv.fetch(personenID);

                PreparedStatement getMitgliedSeit = conn.prepareStatement("SELECT Mitglied_seit FROM mitglied WHERE PersonenID = ?");
                getMitgliedSeit.setString(1, personenID);

                ResultSet mitgliedSeit = getMitgliedSeit.executeQuery();
                mitgliedSeit.next();

                mitglied_seit = mitgliedSeit.getTimestamp(1).toLocalDateTime();
                
            } catch (NoSuchObjectException e) {
                g = rv.fetchGaeste(personenID);
                dbFound = "gast";
                mitglied_seit = LocalDateTime.now();
            }

            // herausfinden welchen Rang er hat
            if (dbFound == null)
                dbFound = getRolle(personenID);

            switch (dbFound) {
                case "gast" -> g = new Gast(personenID, g.getNachname(), g.getVorname(), g.getEmail(), g.getPassword(), g.getAnschrift(), g.getMitgliedsNr(), g.getTelefonNr(), g.getSpenderStatus());
                case "mitglied" -> g = new Mitglied(personenID, g.getNachname(), g.getVorname(), g.getEmail(), g.getPassword(), g.getAnschrift(), g.getMitgliedsNr(), g.getTelefonNr(), g.getSpenderStatus(), mitglied_seit);
                case "mitarbeiter" -> g = new Mitarbeiter(personenID, g.getNachname(), g.getVorname(), g.getEmail(), g.getPassword(), g.getAnschrift(), g.getMitgliedsNr(), g.getTelefonNr(), g.getSpenderStatus(), mitglied_seit);
                case "vorstand" -> g = new Vorsitz(personenID, g.getNachname(), g.getVorname(), g.getEmail(), g.getPassword(), g.getAnschrift(), g.getMitgliedsNr(), g.getTelefonNr(), g.getSpenderStatus(), mitglied_seit);
            }

            if (g.getClass() == rolle.getKlasse())
                throw new Exception("Der Nutzer hat diese Rolle bereits.");

            rolleAendernSwitch(g, rolle, mitglied_seit);

        } catch (Exception e) {
            throw new RuntimeException(e);
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

    public void nutzerEntfernen(String personenID) {
        String[] dbs = {"vorstand", "mitarbeiter", "mitglied", "gast"};

        try {

            for (String db : dbs)
                ausDBEntfernen(personenID, db);

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

                m = new Mitglied(g.getPersonenID(), g.getNachname(), g.getVorname(), g.getEmail(), g.getPassword(),
                        g.getAnschrift(), g.getMitgliedsNr(), g.getTelefonNr(), g.getSpenderStatus(),
                        mitglied_seit);

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

        mitarbeiter = new ArrayList<>();

        try {
            // Mitglieder erstellen
            PreparedStatement getMitarbeiter = conn.prepareStatement("SELECT PersonenID FROM mitarbeiter");
            ResultSet result = getMitarbeiter.executeQuery();

            ArrayList<String> mitgliederIDs = new ArrayList<>();

            while (result.next()) {
                mitgliederIDs.add(result.getString(1));
            }

            ArrayList<Gast> mitarbeiterAlsGaeste = getGaesteWithIDs(mitgliederIDs);

            for (Gast g : mitarbeiterAlsGaeste) {
                mitglied_seit = getMitgliedSeit(g.getPersonenID());

                m = new Mitarbeiter(g.getPersonenID(), g.getNachname(), g.getVorname(), g.getEmail(), g.getPassword(),
                        g.getAnschrift(), g.getMitgliedsNr(), g.getTelefonNr(), g.getSpenderStatus(),
                        mitglied_seit);

                mitarbeiter.add(m);
            }

            getMitarbeiter.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return mitarbeiter;
    }

    public ArrayList<Vorsitz> getVorsitze() {
        ArrayList<Vorsitz> vorsitze;
        Vorsitz v;
        LocalDateTime mitglied_seit;

        vorsitze = new ArrayList<>();

        try {
            // Mitglieder erstellen
            PreparedStatement getVorsitze = conn.prepareStatement("SELECT PersonenID FROM vorstand");
            ResultSet result = getVorsitze.executeQuery();

            ArrayList<String> mitgliederIDs = new ArrayList<>();

            while (result.next()) {
                mitgliederIDs.add(result.getString(1));
            }

            ArrayList<Gast> mitarbeiterAlsGaeste = getGaesteWithIDs(mitgliederIDs);

            for (Gast g : mitarbeiterAlsGaeste) {
                mitglied_seit = getMitgliedSeit(g.getPersonenID());

                v = new Vorsitz(g.getPersonenID(), g.getNachname(), g.getVorname(), g.getEmail(), g.getPassword(),
                        g.getAnschrift(), g.getMitgliedsNr(), g.getTelefonNr(), g.getSpenderStatus(),
                        mitglied_seit);

                vorsitze.add(v);
            }

            getVorsitze.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return vorsitze;
    }

    public ArrayList<Mahnung> getMahnungenListe() {
        ArrayList<Mahnung> mahnungen = new ArrayList<>();

        try {
            PreparedStatement prep = conn.prepareStatement("SELECT * FROM mahnung");
            ResultSet result = prep.executeQuery();

            while (result.next()) {
                Mahnung m = new Mahnung(result.getString("MahnungID"),
                        result.getString("MitlgiedID"),
                        result.getString("Grund"),
                        result.getTimestamp("Verfallsdatum").toLocalDateTime());

                mahnungen.add(m);
            }

            return mahnungen;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void mahnungErstellen(Mahnung mahnung) {

        try {
            PreparedStatement prep = conn.prepareStatement("INSERT INTO mahnung VALUES(?, ?, ?, ?)");
            prep.setString(1, mahnung.getMahnungsID());
            prep.setString(2, mahnung.getGrund());
            prep.setTimestamp(3, Timestamp.valueOf(mahnung.getVerfallsdatum()));
            prep.setString(4, mahnung.getMitgliedsID());

            prep.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void mahnungLoeschen(String mahnungsID) {

        try {
            PreparedStatement prep = conn.prepareStatement("DELETE FROM mahnung WHERE MahnungID = ?");
            prep.setString(1, mahnungsID);

            prep.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private void rolleAendernSwitch(Gast gast, Rolle rolle, LocalDateTime mitglied_seit) throws SQLException {

        if (rolle == GAST) {
            gastHinzufuegen(gast);
            ausDBEntfernen(gast.getPersonenID(), "vorstand");
            ausDBEntfernen(gast.getPersonenID(), "mitarbeiter");
            ausDBEntfernen(gast.getPersonenID(), "mitglied");
            return;
        }

        String istGesperrt = "N";

        if (!(gast instanceof Gast))
            istGesperrt = ((Mitglied) gast).isGesperrt() ? "Y" : "N";

        // Wird für alles hierunter gebraucht
        if (gast.getClass() == GAST.getKlasse())
            zuDBHinzufuegen(gast.getPersonenID(), mitglied_seit, istGesperrt, "mitglied");

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

    private String getRolle(String personenID) throws SQLException, NoSuchObjectException {
        String[] dbs = {"vorstand", "mitarbeiter", "mitglied", "gast"};

        for (String db : dbs) {
            PreparedStatement getCorrectTable = conn.prepareStatement(String.format("SELECT * FROM %s WHERE PersonenID = ?", db));
            getCorrectTable.setString(1, personenID);
            ResultSet correctTable = getCorrectTable.executeQuery();

            try {
                correctTable.next();
                if (correctTable.getString("PersonenID").equals(personenID)) {
                    return db;
                }
            } catch (SQLException e) {
                continue;
            }
        }

        throw new NoSuchObjectException("Nutzer mit der ID " + personenID + " nicht gefunden.");
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

    private LocalDateTime getMitgliedSeit(String personenID) throws SQLException {
        PreparedStatement getMitgliedSeit = conn.prepareStatement("SELECT Mitglied_seit FROM mitglied WHERE PersonenID = ?");
        getMitgliedSeit.setString(1, personenID);

        ResultSet mitgliedSeit = getMitgliedSeit.executeQuery();
        mitgliedSeit.next();

        return mitgliedSeit.getTimestamp(1).toLocalDateTime();
    }

}
