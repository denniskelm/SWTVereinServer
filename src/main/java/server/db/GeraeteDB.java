package server.db;

import server.geraetemodul.*;

import java.rmi.NoSuchObjectException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

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

    public ArrayList<Geraet> getGeraeteList() {
        ArrayList<Geraet> geraete;
        Geraet g;

        geraete = new ArrayList<>();

        try {

            // Geräte erstellen
            PreparedStatement getGeraete = conn.prepareStatement("SELECT * FROM geraet");
            ResultSet result = getGeraete.executeQuery();

            while (result.next()) {
                String gID = result.getString("GeraeteID");
                int leihfrist = result.getInt("Leihfrist");
                Status leihstatus = Status.valueOf(result.getString("Leihstatus"));
                String abholort = result.getString("Abholort");
                String name = result.getString("Name");
                String beschreibung = result.getString("Beschreibung");
                String kategorie = result.getString("Kategorie");
                String spenderName = result.getString("Spendername");

                g = new Geraet(gID, name, spenderName, leihfrist, kategorie, beschreibung, abholort, leihstatus);

                geraete.add(g);
            }

            getGeraete.close();

            // Historien laden
            for (Geraet geraet : geraete) {

                    PreparedStatement getHistorie = conn.prepareStatement("SELECT * FROM historie WHERE GeraeteID = ? ");
                    getHistorie.setString(1, geraet.getGeraeteID());

                    geraet.setHistorie(getAusleiherList(getHistorie));

            }

            // Reservierungslisten laden
            for (Geraet geraet : geraete) {
                if (geraet.getLeihstatus() == Status.FREI)
                    geraet.setReservierungsliste(new ArrayList<>());
                else {

                    PreparedStatement getResListe = conn.prepareStatement("SELECT * FROM reserviert WHERE GeraeteID = ? ");
                    getResListe.setString(1, geraet.getGeraeteID());

                    geraet.setReservierungsliste(getAusleiherList(getResListe));

                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return geraete;
    }

    private ArrayList<Ausleiher> getAusleiherList(PreparedStatement prep) throws SQLException {
        ArrayList<Ausleiher> result;
        ResultSet reservierer = prep.executeQuery();

        result = new ArrayList<>();

        while (reservierer.next()) {

            String personenID = reservierer.getString("PersonenID");
            LocalDateTime fristBeginn = reservierer.getTimestamp("Fristbeginn").toLocalDateTime();
            LocalDateTime reservierDatum = reservierer.getTimestamp("Reservierdatum").toLocalDateTime();
            boolean isAbgegeben = reservierer.getString("Abgegeben").equals("Y");

            Ausleiher a = new Ausleiher(personenID);
            a.setFristBeginn(fristBeginn);
            a.setReservierdatum(reservierDatum);
            a.setAbgegeben(isAbgegeben);

            result.add(a);
        }

        prep.close();

        return result;
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
            result.next();

            // wenn leer, dann Gerät auf Frei setzen
            // TODO sonst auf Beansprucht?
            if (result.getInt(1) == 0) {
                PreparedStatement updateLeihstatus = conn.prepareStatement("UPDATE geraet SET Leihstatus = 'FREI' WHERE geraeteID = ?");
                updateLeihstatus.setString(1, geraeteID);

                updateLeihstatus.executeUpdate();
                updateLeihstatus.close();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void geraetAusgeben(String geraeteID) {

        try {
            PreparedStatement prep = conn.prepareStatement("UPDATE geraet SET Leihstatus = 'AUSGELIEHEN' WHERE GeraeteID = ?");
            prep.setString(1, geraeteID);

            prep.executeUpdate();
            prep.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void geraetAnnehmen(String geraeteID) {
        // Geraet.annehmen()
        try {
            Object[] ausleiherDaten = getAusleiherDaten(geraeteID);
            Timestamp fristBeginn = (Timestamp) ausleiherDaten[2];

            moveCurrentAusleiherToHistorie(geraeteID, (String) ausleiherDaten[0], fristBeginn, (Timestamp) ausleiherDaten[3], (String) ausleiherDaten[1]);

            // Fristbeginn der anderen Ausleiher neu berechnen
            int leihfrist = getLeihfrist(geraeteID);
            long tageZuFrueh = LocalDateTime.now().until(fristBeginn.toLocalDateTime().plusDays(leihfrist), ChronoUnit.DAYS);
            Timestamp[] fristBeginne = getFristbeginne(geraeteID);

            fristBeginneUpdaten(geraeteID, fristBeginne, tageZuFrueh);

            updateLeihstatus(geraeteID);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void geraeteDatenVerwalten(String geraeteID, Geraetedaten attr, Object wert) throws NoSuchObjectException {
        String value = String.valueOf(wert);

        try {
            PreparedStatement prep;

            switch (attr) {
                case NAME -> prep = conn.prepareStatement("UPDATE geraet SET Name = ? WHERE GeraeteID = ?");
                case SPENDERNAME -> prep = conn.prepareStatement("UPDATE geraet SET Spendername = ? WHERE GeraeteID = ?");
                case LEIHFRIST -> prep = conn.prepareStatement("UPDATE geraet SET Leihfrist = ? WHERE GeraeteID = ?");
                case KATEGORIE -> prep = conn.prepareStatement("UPDATE geraet SET Kategorie = ? WHERE GeraeteID = ?");
                case BESCHREIBUNG -> prep = conn.prepareStatement("UPDATE geraet SET Beschreibung    = ? WHERE GeraeteID = ?");
                case ABHOLORT -> prep = conn.prepareStatement("UPDATE geraet SET Abholort = ? WHERE GeraeteID = ?");
                default -> throw new NoSuchObjectException("Dieses Attribut existiert nicht.");
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
    
    public int getIdCounter() {

        try {
            PreparedStatement prep = conn.prepareStatement("SELECT COUNT(*) FROM geraet");
            ResultSet result = prep.executeQuery();
            result.next();
            
            return result.getInt(1);
            
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        
    }

    public void reset() {
        try {
            PreparedStatement historieReset = conn.prepareStatement("DELETE FROM historie");
            historieReset.executeUpdate();
            historieReset.close();

            PreparedStatement reserviertReset = conn.prepareStatement("DELETE FROM reserviert");
            reserviertReset.executeUpdate();
            reserviertReset.close();

            PreparedStatement gereateReset = conn.prepareStatement("DELETE FROM geraet");
            gereateReset.executeUpdate();
            gereateReset.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Object[] getAusleiherDaten(String geraeteID) throws SQLException {
        Object[] ausleiherDaten = new Object[4];

        PreparedStatement getAusleiherDaten = conn.prepareStatement("SELECT * FROM reserviert WHERE GeraeteID = ?");
        getAusleiherDaten.setString(1, geraeteID);

        ResultSet result = getAusleiherDaten.executeQuery();
        result.next();

        ausleiherDaten[0] = result.getString("PersonenID");
        ausleiherDaten[1] = result.getString("Abgegeben");
        ausleiherDaten[2] = result.getTimestamp("Reservierdatum");
        ausleiherDaten[3] = result.getTimestamp("Fristbeginn");

        return ausleiherDaten;
    }

    private void moveCurrentAusleiherToHistorie(String geraeteID, String personenID, Timestamp resevierDatum, Timestamp fristBeginn, String abgegeben) throws SQLException {
        // aktuellen Ausleiher zur Historie hinzufügen
        PreparedStatement addToHistorie = conn.prepareStatement("INSERT INTO historie VALUES (?, ?, ?, ?, ?)");
        addToHistorie.setString(1, geraeteID);
        addToHistorie.setString(2, personenID);
        addToHistorie.setTimestamp(3, resevierDatum);
        addToHistorie.setTimestamp(4, fristBeginn);
        addToHistorie.setString(5, abgegeben);

        addToHistorie.executeUpdate();
        addToHistorie.close();

        // aktuellen Ausleiher aus Reservierungsliste entfernen
        PreparedStatement removeFromResliste = conn.prepareStatement("DELETE FROM reserviert WHERE GeraeteID = ? AND PersonenID = ?");
        removeFromResliste.setString(1, geraeteID);
        removeFromResliste.setString(2, personenID);

        removeFromResliste.executeUpdate();
        removeFromResliste.close();
    }

    private int getLeihfrist(String geraeteID) throws SQLException {
        PreparedStatement prep = conn.prepareStatement("SELECT Leihfrist FROM geraet WHERE GeraeteID = ?");
        prep.setString(1, geraeteID);

        ResultSet result = prep.executeQuery();
        result.next();

        return result.getInt(1);
    }

    private int getReslisteSize(String geraeteID) throws SQLException {
        PreparedStatement prep = conn.prepareStatement("SELECT COUNT(GeraeteID) FROM reserviert WHERE GeraeteID = ?");
        prep.setString(1, geraeteID);

        ResultSet result = prep.executeQuery();

        result.next();

        return result.getInt(1);
    }

    private void updateLeihstatus(String geraeteID) throws SQLException {
        PreparedStatement prep;

        // herausfinden, ob die Reservierungsliste leer ist
        int resListeGroesse = getReslisteSize(geraeteID);

        if (resListeGroesse == 0)
            prep = conn.prepareStatement("UPDATE geraet SET Leihstatus = 'FREI' WHERE geraeteID = ?");
        else
            prep = conn.prepareStatement("UPDATE geraet SET Leihstatus = 'BEANSPRUCHT' WHERE geraeteID = ?");

        prep.setString(1, geraeteID);

        prep.executeUpdate();
        prep.close();
    }

    private Timestamp[] getFristbeginne(String geraeteID) throws SQLException {
        PreparedStatement prep = conn.prepareStatement("SELECT Fristbeginn FROM reserviert WHERE GeraeteID = ? AND Abgegeben = 'N' ");
        prep.setString(1, geraeteID);
        ResultSet result = prep.executeQuery();

        int anzahlPersonen = getReslisteSize(geraeteID);

        Timestamp[] fristBeginne = new Timestamp[anzahlPersonen];

        int j = 0;

        while (result.next())
            fristBeginne[j] = result.getTimestamp(++j); // ++j da SQL eins höher zählt als Java

        prep.close();

        return fristBeginne;
    }

    private void fristBeginneUpdaten(String geraeteID, Timestamp[] fristBeginne, long tage) throws SQLException {
        PreparedStatement prep = conn.prepareStatement("UPDATE reserviert SET Fristbeginn = ? WHERE GeraeteID = ? AND Abgegeben = 'N'");

        for (Timestamp timestamp : fristBeginne) {
            prep.setTimestamp(1, Timestamp.valueOf(timestamp.toLocalDateTime().plusDays(tage)));
            prep.setString(2, geraeteID);
            prep.addBatch();
        }

        prep.executeBatch();
        prep.close();
    }

}
