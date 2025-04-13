package Utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private Properties prop;

    public ConfigReader() {
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            prop = new Properties();
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getLambdaUsername() {
        return prop.getProperty("lt.username");
    }

    public String getLambdaPassword() {
        return prop.getProperty("lt.password");
    }
    
    public String getAppValidUsername() {
        return prop.getProperty("app.validusername");
    }
    
    public String getAppValidPassword() {
        return prop.getProperty("app.validpassword");
    }
    
    public String getAppInvalidUsername() {
        return prop.getProperty("app.invalidusername");
    }
    
    public String getAppInvalidPassword() {
        return prop.getProperty("app.invalidpassword");
    }
}
