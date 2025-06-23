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

    private static final String CSV_PATH = "src/test/resources/test-data.csv";

    @DataProvider(name = "loginData")
    public static Iterator<Object[]> loginDataProvider() throws CsvValidationException {
        List<Object[]> testData = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(CSV_PATH))) {
            String[] row;
            while ((row = reader.readNext()) != null) {
                if (row.length >= 2) {
                    String username = row[0].trim();
                    String password = row[1].trim();
                    testData.add(new Object[]{username, password});
                } else {
                    System.err.println("Skipping malformed CSV row: " + String.join(",", row));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return testData.iterator();
    }
}
