package src;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CsvDbImporterTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        List<String> files = new ArrayList<String>();
        files.add("/home/ali/repos/Flugplan/flugplan.csv");
        files.add("/home/ali/repos/Flugplan/flieger.csv");

        CsvDbImporter csvDbImporter = new CsvDbImporter();
        List<List<String>> csvEntries = csvDbImporter.readCsvFile(files);
        
        csvDbImporter.writeToDb(csvEntries);
    }

    //TOOD get first entrie from Flugplan Table
    //TODO create array of the desired result and assert true if they're equal
}
