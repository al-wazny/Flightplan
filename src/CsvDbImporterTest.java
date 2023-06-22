package src;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CsvDbImporterTest {
    public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
        CsvDbImporter csvDbImporter = new CsvDbImporter();
        List<List<String>> csvEntries = csvDbImporter.readCsvFile("/home/ali/repos/Flugplan/flieger.csv");
        
        csvDbImporter.writeToDb(csvEntries);
    }
}
