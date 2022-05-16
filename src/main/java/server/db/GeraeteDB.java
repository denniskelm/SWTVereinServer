package server.db;

import server.Ausleiher;
import server.Geraet;
import server.Geraetedaten;
import server.Status;

import java.sql.*;

public class GeraeteDB {

    private Connection conn;


    public GeraeteDB() {
        final String URL = "meta.informatik.uni-rostock.de";
        final String USER = "rootuser", PASSWORD = "rootuser";

        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + URL + ":3306/vswt22", USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void geraetHinzufuegen(Geraet g) {
        try {
            String gID = g.getGeraeteID();
            int leihfrist = g.getLeihfrist();
            String leihstatus = g.getLeihstatus().toString();
            String abholort = g.getGeraetAbholort();
            String name = g.getGeraetName();
            String beschreibung = g.getGeraetBeschreibung();
            String kategorie = g.getKategorie();
            String spender = g.getSpenderName();

            PreparedStatement prep = conn.prepareStatement("INSERT INTO geraet VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            prep.setString(1, gID);
            prep.setInt(2, leihfrist);
            prep.setString(3, leihstatus);
            prep.setString(4, abholort);
            prep.setString(5, name);
            prep.setString(6, beschreibung);
            prep.setString(7, kategorie);
            prep.setString(8, spender);


            prep.executeUpdate();
            prep.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void geraetEntfernen(String geraeteID) {
        try {
            PreparedStatement prep = conn.prepareStatement("DELETE FROM geraet WHERE geraeteID = ?");
            prep.setString(1, geraeteID);

            prep.executeUpdate();
            prep.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void geraetReservieren(String geraeteID, Ausleiher ausleiher) {
        // diese Klasse muss über Geraet aufgerufen werden
        // TODO Methode noch testen

        try {
            PreparedStatement prep = conn.prepareStatement("UPDATE geraet SET Leihstatus = 'BEANSPRUCHT' WHERE geraeteID = ? AND Leihstatus = 'FREI' ");
            prep.setString(1, geraeteID);

            prep.executeUpdate();
            prep.close();

            prep = conn.prepareStatement("INSERT INTO reserviert VALUES (?, ?, ?, ?, ?)");
            prep.setString(1, geraeteID);
            prep.setString(2, ausleiher.getMitlgiedsID());
            prep.setTimestamp(3, Timestamp.valueOf(ausleiher.getReservierdatum()));
            prep.setTimestamp(4, Timestamp.valueOf(ausleiher.getFristBeginn()));
            prep.setString(5, ausleiher.isAbgegeben() ? "Y" : "N");

            prep.executeUpdate();
            prep.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void reservierungStornieren(String geraeteID, String personenID) {

        try {
            PreparedStatement prep = conn.prepareStatement("DELETE FROM reserviert WHERE GeraeteID = ? AND PersonenID = ?");
            prep.setString(1, geraeteID);
            prep.setString(2, personenID);

            prep.executeUpdate();
            prep.close();

            // herausfinden, ob die Reservierungsliste leer ist
            prep = conn.prepareStatement("SELECT COUNT(GeraeteID) FROM reserviert WHERE GeraeteID = ? AND PersonenID = ?");
            prep.setString(1, geraeteID);
            prep.setString(2, personenID);

            ResultSet result = prep.executeQuery();
            prep.close();

            result.next();

            // wenn leer, dann Gerät auf Frei setzen
            // TODO sonst auf Beansprucht?
            if (result.getInt(1) == 0) {
                prep = conn.prepareStatement("UPDATE geraet SET Leihstatus = 'FREI' WHERE geraeteID = ?");
                prep.setString(1, geraeteID);

                prep.executeUpdate();
                prep.close();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void geraetAusgeben(String geraeteID) throws Exception {
        PreparedStatement prep;
        ResultSet result;

        try {
            prep = conn.prepareStatement("SELECT Leihstatus FROM geraet WHERE GeraeteID = ?");
            prep.setString(1, geraeteID);

            result = prep.executeQuery();
            result.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String status = result.getString(1);

        if (status == null)
            throw new Exception("keine Reservierung vorhanden.");

        if (!status.equals(Status.BEANSPRUCHT.toString()))
            throw new Exception("ausgeliehenes/freies Geraet kann nicht ausgegeben werden.");

        try {
            prep = conn.prepareStatement("UPDATE geraet SET Leihstatus = 'AUSGELIEHEN' WHERE GeraeteID = ?");
            prep.setString(1, geraeteID);

            prep.executeUpdate();
            prep.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void geraetAnnehmen(String geraeteID) throws Exception {
        PreparedStatement prep;
        ResultSet result;
        String status;
        String personenID;

        // Geraeteverwaltung.geraetAnnehmen(...)
        try {
            prep = conn.prepareStatement("SELECT Leihstatus FROM geraet WHERE GeraeteID = ?");
            prep.setString(1, geraeteID);

            result = prep.executeQuery();
            result.next();

            status = result.getString(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (status == null)
            throw new Exception("keine Reservierung vorhanden.");

        if (!status.equals(Status.AUSGELIEHEN.toString()))
            throw new Exception("ausgeliehenes/freies Geraet kann nicht angenommen werden.");

        // Geraet.annehmen()
        try {

            // TODO aktuellen Ausleiher zur Historie hinzufügen
            //prep = conn.prepareStatement("INSERT INTO historie VALUES (?, ?, ?, ?, ?)");
            prep.setString(1, geraeteID);
            //prep.setString(2, pers);
            // TODO aktuellen Ausleiher aus Reservierungsliste entfernen
            // TODO Fristbeginn der anderen Ausleiher neu berechnen

            // herausfinden, ob die Reservierungsliste leer ist
            prep = conn.prepareStatement("SELECT COUNT(GeraeteID) FROM reserviert WHERE GeraeteID = ?");
            prep.setString(1, geraeteID);

            result = prep.executeQuery();
            prep.close();

            result.next();

            if (result.getInt(1) == 0)
                prep = conn.prepareStatement("UPDATE geraet SET Leihstatus = 'FREI' WHERE geraeteID = ?");
            else
                prep = conn.prepareStatement("UPDATE geraet SET Leihstatus = 'BEANSPRUCHT' WHERE geraeteID = ?");

            prep.setString(1, geraeteID);

            prep.executeUpdate();
            prep.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void geraeteDatenVerwalten(String geraeteID, Geraetedaten attr, Object wert) {
        String value = String.valueOf(wert);

        try {
            PreparedStatement prep = null;


            switch (attr) {
                case NAME -> prep = conn.prepareStatement("UPDATE geraet SET Name = ? WHERE GeraeteID = ?");
                case SPENDERNAME -> prep = conn.prepareStatement("UPDATE geraet SET Spendername = ? WHERE GeraeteID = ?");
                case LEIHFRIST -> prep = conn.prepareStatement("UPDATE geraet SET Leihfrist = ? WHERE GeraeteID = ?");
                case KATEGORIE -> prep = conn.prepareStatement("UPDATE geraet SET Kategorie = ? WHERE GeraeteID = ?");
                case BESCHREIBUNG -> prep = conn.prepareStatement("UPDATE geraet SET Beschreibung    = ? WHERE GeraeteID = ?");
                case ABHOLORT -> prep = conn.prepareStatement("UPDATE geraet SET Abholort = ? WHERE GeraeteID = ?");
            }

            if (attr == Geraetedaten.LEIHFRIST)
                prep.setInt(1, Integer.parseInt(value));
            else
                prep.setString(1, value);

            prep.setString(2, geraeteID);

            prep.executeUpdate();
            prep.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



    }

    public void historieZuruecksetzen(String geraeteID) {

        try {
            PreparedStatement prep = conn.prepareStatement("DELETE FROM historie WHERE GeraeteID = ?");
            prep.setString(1, geraeteID);

            prep.executeUpdate();
            prep.close();
            } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }

}
