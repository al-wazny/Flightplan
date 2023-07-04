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

    Statement stmt;
    List<String> columnNames;
    HashMap<String, String> csvToTableMap = new HashMap<String, String>();
    
    // TODO pass both csv files
    // each line is in an own array / list
    public void readCsvFile(List<String> files) throws IOException, ClassNotFoundException, SQLException {
        writeToDb();
        
        String row = "";
        for (String file : files) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            columnNames = Arrays.asList(reader.readLine().split(","));

            while ((row = reader.readLine()) != null) {
                List<String> values = Arrays.asList(row.split(","));
                // check which column the current value belongs to and add it in here
                createDbRow(values);
            }
        }
    }
    
    public void writeToDb() throws SQLException, ClassNotFoundException {
        createDbConnection();
        mapCsvToDbColumns();
    }

    private void mapCsvToDbColumns() {
        // Flugplan Table
        csvToTableMap.put("FlugID", "Flugplan.id");
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
            stmt.executeUpdate("INSERT INTO Flugzeugmodell (name, AnzSitzplaetze) VALUES ('blubber', '2341');");
        } catch (ClassNotFoundException e) {
            System.out.println("There's an issue with your Driver: "+e);
        } catch (SQLException e) {
            System.out.println("There's an issue with your SQL: "+e);
        }
    }

    private void createDbRow(List<String> entry) throws SQLException {
        for (String columString : entry) {
            int index = entry.indexOf(columString);

            String columnName = columnNames.get(index);
            String columnMap = csvToTableMap.get(columnName);
            
            if (columnMap != null) {     
                String[] dbTableAndColumn = csvToTableMap.get(columnName).split("\\.");
    
                String insert = "INSERT INTO "+ dbTableAndColumn[0] +" ("+ dbTableAndColumn[1] +") VALUES ('"+ columString +"');";
                System.out.println(insert);
    
                try {
                    stmt.executeUpdate(insert);
                } catch (NullPointerException e) {
                    // TODO: handle exception
                }
            }
        }
    }
}