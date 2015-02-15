import config.Configuration;
import org.testng.annotations.Test;

/**
 * Created by tomasz on 07.02.15.
 */
public class ConfigurationTest {

    @Test
    public void create() {
        try {
            System.out.println(new Configuration());
        } catch (Configuration.ConfigurationException e) {
            e.printStackTrace();
        }
    }
}
