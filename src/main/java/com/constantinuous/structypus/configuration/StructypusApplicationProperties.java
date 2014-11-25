package com.constantinuous.structypus.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class StructypusApplicationProperties {

    // ~~~ Property definition
    public static final String PROP = "com.foo";

    private static Properties properties;

    // ~~~ Path and name of the properties file
    private static final String CONFIG_FILE = "com.com.constantinuous.structypus.configuration.file";
    private static final String RELATIVE_CONFIG_DIR = "target/classes/";
    private static final String PROPERTY_FILE_NAME = "structypusApplication.properties";

    private static String getPropertyFilePath() {
        return System.getProperty(CONFIG_FILE, getDefaultPropertyFilePath());
    }

    private static String getDefaultPropertyFilePath() {
        return RELATIVE_CONFIG_DIR + PROPERTY_FILE_NAME;
    }

    /**
     * By calling this method, the caller ensures, that properties are loaded.
     */
    public synchronized void initialize() {
        if (properties == null) {
            properties = loadProperties();
        }
    }

    /**
     * Initially loads the properties from application.properties.
     */
    private Properties loadProperties() {

        Properties properties = new Properties();
        String propertyFile = getPropertyFilePath();

        InputStream input = null;
        try {
            File f = new File(propertyFile);
            input = new FileInputStream(f);
            properties.load(new InputStreamReader(input, "UTF-8"));

            System.out.println("Property file was successfully loaded: " + propertyFile);
        } catch (UnsupportedEncodingException e) {
            System.out.println("Property file could not be loaded: " + propertyFile + "\n" + e.toString());
        } catch (IOException e) {
            System.out.println("Property file could not be loaded: " + propertyFile + "\n" + e.toString());
        }

        return properties;
    }

    // ~~~ Property access
    public String getString(String key) {
        // try to load property from Environment
        String value = null;

        value = System.getenv(key);

        if (value != null) {
            System.out.println("Getting property> " + key + " with value " + value + " from system variables.");
            return value;
        }

        // try to load property from Environment
        value = System.getProperty(key);

        if (value != null) {
            System.out.println("Getting property> " + key + " with value " + value
                    + " from runtime environment variables.");
            return value;
        }

        // try to load property from File
        value = (properties == null) ? null : properties.getProperty(key);
        System.out.println("Getting property> " + key + " with value " + value + " from properties file: "
                + getPropertyFilePath());

        return value;
    }

}
