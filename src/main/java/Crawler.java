import config.Configuration;
import data.CarDataFacade;
import data.db.Car;
import serialization.DealersContainer;
import serialization.Reader;
import serialization.Writer;

import java.io.File;

public class Crawler implements Runnable {

    private final String name;
    private CarDataFacade dataFacade = new CarDataFacade();
    private DealersContainer dataContainer;
    private Reader reader = new Reader();
    private Writer writer = new Writer();
    private File dataFile;
    private int startPage;
    private int endPage;

    public Crawler(String name, File dataFile, int startPage, int endPage, DealersContainer dataContainer) {
        this.dataFile = dataFile;
        this.startPage = startPage;
        this.endPage = endPage;
        this.name = name;
        this.dataContainer = dataContainer;
        this.dataContainer.getCrawlerCurrentPage().put(this.name, startPage);
    }

    @Override
    public void run() {
        downloadPremiumData();
        readFile();
        downloadStandardData();
        saveToFile();
    }

    private synchronized void readFile() {
        try {
            DealersContainer readedData = reader.read(dataFile);
            dataContainer.clearOffers();
            dataContainer.getOffersMap().putAll(readedData.getOffersMap());
            dataContainer.setCurrentPage(this.name, readedData.getCrawlerCurrentPage().get(this.name));
            dataContainer.setPremiumDownloaded(readedData.isPremiumDownloaded());


            try {
                startPage = dataContainer.getCrawlerCurrentPage().get(this.name);
            } catch (Exception e) {
            }

            Thread.sleep(200);

        } catch (Reader.ReaderExceptin readerExceptin) {
            readerExceptin.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void downloadPremiumData() {
        try {
            readFile();
            if (dataContainer.isPremiumDownloaded()) {
                return;
            }
            for (Car car : dataFacade.getPremiumCars(this.name)) {
                dataContainer.put(car);
            }
            dataContainer.setPremiumDownloaded(true);
            saveToFile();
        } catch (Configuration.ConfigurationException e) {
            Main.handleCriticalException(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void downloadStandardData() {
        for (int page = startPage; page <= endPage; page++) {
            try {
                for (Car car : dataFacade.getCars(this.name, page)) {
                    dataContainer.put(car);
                }
                dataContainer.setCurrentPage(name, page + 1);
                dataContainer.setTotalCount(dataFacade.getPagesTotalCount());
                writer.write(dataContainer, dataFile);
                readFile();

            } catch (Configuration.ConfigurationException e) {
                Main.handleCriticalException(e);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private synchronized void saveToFile() {
        try {
            writer.write(dataContainer, dataFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}