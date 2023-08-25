import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import static org.apache.logging.log4j.core.util.Loader.getClassLoader;


public class ApplicationProperties {

    private static Properties instance = null;  // Singleton implementation
    private static final String APPLICATION_PREFIX = "application";
    private static final String APPLICATION_SUFFIX = "properties";
    private static final Logger logger = LogManager.getLogger(ApplicationProperties.class);

    private ApplicationProperties(){ }

    // Singleton implementation
    public static synchronized Properties getInstance(){
        if (instance == null){
            instance = loadPropertiesFile();
        }
        return instance;
    }

    private static Properties loadPropertiesFile(){

        String environment = Optional.ofNullable(System.getenv("env"))
                .orElse("dev");
        String filename = String.format("%s-%s.%s",APPLICATION_PREFIX, environment, APPLICATION_SUFFIX);

        logger.info("Property file to read {}", filename);

        Properties properties = new Properties();

        try {
            properties.load(getClassLoader().getResourceAsStream(filename));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Unable to load the file {}", filename);
        }

        return properties;
    }
}
