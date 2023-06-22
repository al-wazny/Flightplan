package src;

import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class CsvDbImporter {

    final static String DB_URL = "jdbc:mysql://localhost:3306/FlightPlan";
    final static String USER = "root";
    final static String PASSWORD = "";
    final static String TABLENAME = "Flieger";

    static Statement stmt;
    static List<String> columnNames;

    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        List<List<String>> csvEntries = readCsvFile("/home/ali/repos/Flugplan/flieger.csv");
        
        writeToDb(csvEntries);
    }
    
    // each line is in an own array / list
    public static List<List<String>> readCsvFile(String file) throws SQLException, ClassNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        
        List<List<String>> lines = new ArrayList<>();
        String line;
        
        while ((line = reader.readLine()) != null) {
            List<String> values = Arrays.asList(line.split(","));
            lines.add(values);
        }
        
        columnNames = lines.get(0);
        lines.remove(0);
        
        return lines;
    }
    
    public static void writeToDb(List<List<String>> csvEntries) throws SQLException, ClassNotFoundException {
        createDbConnection();
        createDbTable();

        for(int i = 0; i < csvEntries.size(); i++) {
            createDbRow(csvEntries.get(i));
        }
    }
    
    private static void createDbConnection() throws ClassNotFoundException, SQLException {        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            
            stmt = conn.createStatement();
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    private static void createDbTable() throws SQLException {
        String columns = String.join(" VARCHAR(255), ", columnNames);
        String createTableQuery = "CREATE TABLE IF NOT EXISTS "+ TABLENAME +" (" + columns + " VARCHAR(255))";

        stmt.executeUpdate(createTableQuery);
    }

    private static void createDbRow(List<String> entry) throws SQLException {
        String insert = "INSERT INTO "+ TABLENAME +" ("+ String.join(",", columnNames) +") VALUES ('"+ String.join("','", entry) +"');";
        stmt.executeUpdate(insert);
    }
}