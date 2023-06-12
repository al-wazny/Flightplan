package src;

import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class CsvDbImporter {

    final static String DB_URL = "jdbc:mysql://localhost:3306/FlightPlan";
    final static String USER = "root";
    final static String PASSWORD = "";
    
    public static void main(String[] args) throws ClassNotFoundException, SQLException, FileNotFoundException {
        
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

        readCsvFile();
    }

    // each line is in an own array / list
    public static void readCsvFile() throws FileNotFoundException {
        //parsing a CSV file into Scanner class constructor  
        Scanner sc = new Scanner(new File("/home/ali/repos/Flugplan/flieger.csv"));  

        List<List<String>> lines = new ArrayList();
        String line;

        while (sc.hasNext()) {  
            line = sc.next();
            List values = Arrays.asList(line);
            lines.add(values);
        }   
        lines.forEach(l -> System.out.println(l));
        sc.close();  //closes the scanner  
    }  
    
    // TODO: create DB table using the CSV File
}