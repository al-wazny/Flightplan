package src;

import java.sql.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

class CsvDbImporter {

    final static String DB_URL = "jdbc:mysql://localhost:3306/FlightPlan";
    final static String USER = "root";
    final static String PASSWORD = "";
    final static String TABLENAME = "Flugplan";

    Statement stmt;
    List<String> columnNames;
    HashMap<String, String> csvToTableMap = new HashMap<String, String>();
    
    // TODO pass both csv files
    // each line is in an own array / list
    public List<List<String>> readCsvFile(List<String> files) throws IOException {
        List<List<String>> lines = new ArrayList<>();
        String row = "";

        for (String file : files) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            columnNames = Arrays.asList(reader.readLine().split(","));

            while ((row = reader.readLine()) != null) {
                List<String> values = Arrays.asList(row.split(","));
                lines.add(values);
            }
        }
        
        return lines;
    }
    
    public void writeToDb(List<List<String>> csvEntries) throws SQLException, ClassNotFoundException {
        createDbConnection();
        // createDbTable();
        mapCsvToDbColumns();

        for(int i = 0; i < csvEntries.size(); i++) {
            createDbRow(csvEntries.get(i));
        }
    }

    private void mapCsvToDbColumns() {
        // Flugplan Table
        csvToTableMap.put("Flugname", "Flugplan.Flugname");
        csvToTableMap.put("EntfernungInMeilen", "Flugplan.EntfernungInMeilen");
        csvToTableMap.put("Startzeit", "Flugplan.Startzeit");
        csvToTableMap.put("Landezeit", "Flugplan.Landezeit");
        csvToTableMap.put("Verpflegung", "Flugplan.Verpflegung");

        // Flughafen Table
        csvToTableMap.put("StartName", "Flughafen.Stadt");
        csvToTableMap.put("StartkurzBez", "Flughafen.Kuerzel");
        csvToTableMap.put("StartLand", "Flughafen.Land");
        csvToTableMap.put("ZielName", "Flughafen.Stadt");
        csvToTableMap.put("ZielkurzBez", "Flughafen.Kuerzel");
        csvToTableMap.put("ZielLand", "Flughafen.Land");

        // Flugzeug Table
        csvToTableMap.put("Flugzeugname", "Flugzeug.Flugzeugname");        
        csvToTableMap.put("Erstinbetriebnahme", "Flugzeug.Erstinbetriebnahme");
        
        // FlugzeugModell Table
        csvToTableMap.put("Modellname", "FlugzeugModell.name");
        csvToTableMap.put("Sitzplaetze", "FlugzeugModell.AnzSitzplaetze");
    }
    
    private void createDbConnection() throws SQLException, ClassNotFoundException {        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
            
            stmt = conn.createStatement();
        } catch (ClassNotFoundException e) {
            System.out.println(e);
        }
    }

    //TODO Check if the creation of the table is part of the programming task

    // private void createDbTable() throws SQLException, NullPointerException {
    //     String columns = String.join(" VARCHAR(255), ", columnNames);
    //     String createTableQuery = "CREATE TABLE IF NOT EXISTS Flugplan (" + columns + " VARCHAR(255))";

    //     try {
    //         stmt.executeUpdate(createTableQuery);            
    //     } catch (NullPointerException e) {
    //         System.out.println(e + ": " + createTableQuery);
    //     }
    // }

    private void createDbRow(List<String> entry) throws SQLException {
        // TODO check within the Hashmap which table and column the current csv column is associated to
        String insert = "INSERT INTO "+ TABLENAME +" ("+ String.join(",", columnNames) +") VALUES ('"+ String.join("','", entry) +"');";

        stmt.executeUpdate(insert);
    }
}