package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesReader {

    public static void main(String[] args) {
        System.out.println(getProperty("login.properties", "urlStart"));
    }

    public static String getProperty(String fileName, String key) {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(
                "src/test/resources/properties/" + fileName)) {
            properties.load(fileInputStream);
            return properties.getProperty(key);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
