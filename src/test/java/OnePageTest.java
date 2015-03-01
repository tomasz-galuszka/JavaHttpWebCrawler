import config.Configuration;
import data.CarDataFacade;
import data.db.Car;
import logger.Logger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OnePageTest {

    @DataProvider(name = "pages")
    public Object[][] pageDataProvider() {
        return new Object[][]{
                {}
        };
    }


    @Test(dataProvider = "pages")
    public void downloadFromPage(int pageNumber) {
        // given
        CarDataFacade dataProvider = new CarDataFacade();

        // when
        List<Car> cars = new ArrayList<Car>();
        try {
            cars = dataProvider.getCars("Main", pageNumber);
        } catch (Configuration.ConfigurationException e) {
            e.printStackTrace();
        }

        // then
        for (Iterator<Car> iterator = cars.iterator(); iterator.hasNext(); ) {
            Car next = iterator.next();
            Logger.log(next.toString());
        }
    }
}
