import config.Configuration;
import data.CarDataFacade;
import data.db.Car;
import serialization.DealersContainer;
import serialization.Reader;
import serialization.Writer;

import java.io.File;

public class Crawler {

    private CarDataFacade dataFacade;
    private DealersContainer dataContainer;
    private Reader reader = new Reader();
    private Writer writer = new Writer();
    private File dataFile;

    public Crawler(File f) {
        try {
            dataFile = f;
            dataFacade = new CarDataFacade();
            dataContainer = reader.read(dataFile);
        } catch (Reader.ReaderExceptin readerExceptin) {
            readerExceptin.printStackTrace();
        }
    }

    public void go() {
        if (dataContainer.getOffersmap().isEmpty()) {
            downloadPremiumCars();
        }

        if (dataContainer.getTotalCount() < 1) {
            dataContainer.setPage(1);
        }

        dataContainer.setTotalCount(dataFacade.getPagesTotalCount());

        for (int i = dataContainer.getPage(); i <= dataContainer.getTotalCount(); i++) {
            downloadNormalCars(i);
        }
    }

    public void downloadNormalCars(int page) {
        try {
            for (Car car : dataFacade.getCars(page)) {
                dataContainer.put(car);
            }
            dataContainer.setPage(page);
            dataContainer.setTotalCount(dataFacade.getPagesTotalCount());
            writer.write(dataContainer, dataFile);

        } catch (Configuration.ConfigurationException e) {
            Main.handleCriticalException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downloadPremiumCars() {
        try {
            for (Car car : dataFacade.getPremiumCars()) {
                dataContainer.put(car);
            }
            writer.write(dataContainer, dataFile);
        } catch (Configuration.ConfigurationException e) {
            Main.handleCriticalException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
