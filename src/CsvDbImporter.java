package src;

import java.sql.*;

class CsvDbImporter {

    final static String DB_URL = "jdbc:mysql://localhost:3306/FlightPlan";
    final static String USER = "root";
    final static String PASSWORD = "";
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  
    
            // Open a connection with MySQL server
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();

            // fetch all the entries inside of the table
            ResultSet result = stmt.executeQuery("select * from test;");

            while (result.next()) {
                // print out each row inside the 'name' column
                System.out.println(result.getString("name"));
            }
        } catch (ClassNotFoundException e) {
            System.out.println("There's a missing class or dependecy: " + e);
        } catch (SQLException e) {
            System.out.println("There's an issue inside your SQL Statement: " + e);
        }
    }
}