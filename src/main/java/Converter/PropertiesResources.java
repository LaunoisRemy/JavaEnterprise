package Converter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Class to manage files properties(Private files)
 */
public class PropertiesResources {

    /**
     * Get properties of one files
     * @param url location of the file
     * @return all properties of the files
     */
    private static Properties getFilesProperties(String url)
    {
        Properties p = new Properties();
        try (InputStream in = PropertiesResources.class.getClassLoader().getResourceAsStream(url))
        {
            if (in == null)
            {
                throw new NullPointerException("You must specify a files.properties file");
            }
            p.load(new InputStreamReader(in));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return p;
    }

    /**
     * Get properties for the file database
     * @return Properties of database(url,user,password)
     */
    public static Properties getDatabaseProperties() {
        return  PropertiesResources.getFilesProperties("properties/database.properties");
    }

    /**
     * Get properties for the file mail
     * @return Properties for connected mail (USer, password)
     */
    public static Properties getMailProperties() {
        return  PropertiesResources.getFilesProperties("properties/mail.properties");
    }
}
