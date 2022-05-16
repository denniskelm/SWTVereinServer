package server.db;

import server.Geraet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbHandler {

    private static Connection conn;
    private static final String URL = "meta.informatik.uni-rostock.de";
    private static final String USER = "rootuser", PASSWORD = "rootuser";

    private static void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + URL + ":3306/vswt22", USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        connect();
    }

    public static void geraetHinzufuegen(Geraet g) {
        try {
            String gID = g.getGeraeteID();
            int leihfrist = g.getLeihfrist();
            String leihstatus = g.getLeihstatus().toString();
            String abholort = g.getGeraetAbholort();
            String name = g.getGeraetName();
            String beschreibung = g.getGeraetBeschreibung();
            String kategorie = g.getKategorie();
            String spender = g.getSpenderName();

            PreparedStatement prep = conn.prepareStatement(String.format("INSERT INTO geraet VALUES ('%s', '%x', '%s', '%s', '%s', '%s', '%s', '%s')",
                    gID, leihfrist, leihstatus, abholort, name, beschreibung, kategorie, spender));

            prep.executeUpdate();
            prep.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
