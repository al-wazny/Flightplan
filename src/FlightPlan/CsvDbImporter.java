package src.FlightPlan;

import java.sql.*;

class CsvDbImporter {

    final static String DB_URL = "jdbc:mysql://localhost:3306/FlightPlan";
    final static String USER = "root";
    final static String PASSWORD = "";
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        
        try {
            Class.forName("com.mysql.jdbc.Driver");  
    
            // Open a connection with MySQL server
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM test;");

            while (rs.next()) {
                System.out.println(rs.getString(0));
            }
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
    }
}