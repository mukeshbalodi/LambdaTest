package Utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.testng.annotations.DataProvider;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CSVReaderUtil {

    // Centralized CSV path
    private static final String CSV_PATH = "src/test/resources/test-data.csv"; 

   
    public static List<String[]> readCSV() throws CsvValidationException {
        List<String[]> data = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader(CSV_PATH))) {
            String[] line;
            while ((line = reader.readNext()) != null) {
                data.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    // DataProvider method for TestNG
    @DataProvider(name = "loginData")
    public static Iterator<Object[]> loginDataProvider() throws CsvValidationException {
        List<String[]> csvData = readCSV();
        List<Object[]> testData = new ArrayList<>();

        for (String[] row : csvData) {
            testData.add(new Object[]{row[0], row[1]}); //  2 columns: username, password
        }

        return testData.iterator();
    }
}
