package server.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author Gabriel Kleebaum
 */
public abstract class Database {
    private final String HOST = "meta.informatik.uni-rostock.de",
        DATABASE = "vswt22",
        URL = "jdbc:mysql://" + HOST + ":3306/" + DATABASE,
        USER = "rootuser",
        PASSWORD = "rootuser";
    private Connection connection;

    public Database() throws SQLException {
        connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public Connection getConnection() {
        return connection;
    }

}
