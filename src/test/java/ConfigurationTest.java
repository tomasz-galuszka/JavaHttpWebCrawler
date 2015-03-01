import config.Configuration;
import logger.Logger;
import org.testng.annotations.Test;

/**
 * Created by tomasz on 07.02.15.
 */
public class ConfigurationTest {

    @Test
    public void create() {
        try {
            Logger.log(new Configuration().toString());
        } catch (Configuration.ConfigurationException e) {
            e.printStackTrace();
        }
    }
}
