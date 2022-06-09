package server;

import java.sql.*;

/*
@author
TODO Raphael Kleebaum
TODO Jonny Schlutter
TODO Gabriel Kleebaum
TODO Mhd Esmail Kanaan
TODO Gia Huy Hans Tran
TODO Ole Björn Adelmann
TODO Bastian Reichert
Dennis Kelm
*/

//TODO WAS MACHT DIESE KLASSE?
public class MysqlConnectionTest {

        public static void main(String[] args){

            String url = "jdbc:mysql://localhost:3306/sakila";
            String user = "root";
            String pass = "root";

            try {
                Connection con = DriverManager.getConnection(url, user, pass);
                Statement stm = con.createStatement();
                String abfrage = "SELECT * FROM actor";
                ResultSet rs = stm.executeQuery(abfrage);

                while(rs.next()){
                    System.out.println(rs.getString(1) + " " +
                            rs.getString(2) + " " +
                            rs.getString(3) + " " +
                            rs.getString(4));
                }


            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }

}
