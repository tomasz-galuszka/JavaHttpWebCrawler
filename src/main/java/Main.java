import config.Configuration;
import data.offer.OfferMdt;
import export.CrawlerExportException;
import export.XlsExporter;
import serialization.DealersContainer;
import serialization.Reader;

import javax.swing.*;
import java.io.File;

public class Main {

    private static String ARG_COMMAND = "";

    /**
     * java crawler -- okiendo do wyboru pliku , jezęli plik nie iestnieje wpisujemy nazwe
     * java crawler xls -- exportuje plik do xls
     */
    public static void main(String[] args) {
        try {
            readInputParams(args);
            if (ARG_COMMAND.equalsIgnoreCase("xls")) {
                exportToXls();
            } else {
                startCrawler();
            }
            System.exit(0);
        } catch (Exception e) {
            handleCriticalException(e);
        }
    }

    public static void handleCriticalException(Exception e) {
        JOptionPane.showMessageDialog(new JFrame(), e.getMessage());
        e.printStackTrace();
        System.exit(-1);
    }

    private static void readInputParams(String[] args) {
        try {
            ARG_COMMAND = args[0];
        } catch (Exception e) {
        }
    }

    private static void startCrawler() throws FileSelector.FileSelectorException {
        File dbFile = new FileSelector().selectFileDat();
        try {
            OfferMdt offerMdt = new OfferMdt();
            Integer tmp = Integer.valueOf(offerMdt.getPagesTotalCountFromMainString(offerMdt.getMainStringFromPage("Main", 10)));
            int pagesTotalCount = tmp / 80;
            pagesTotalCount += (tmp % 80 > 0 ? 1 : 0);


            // divide total count for 4 threads

            int threadTotal = pagesTotalCount / 4;
            int rest = pagesTotalCount % 4;

            // create threads
            DealersContainer data = new DealersContainer();
            Crawler c1 = new Crawler("Crawler 1", dbFile, 1, threadTotal, data);
            Crawler c2 = new Crawler("Crawler 2", dbFile, threadTotal + 1, threadTotal * 2, data);
            Crawler c3 = new Crawler("Crawler 3", dbFile, threadTotal * 2 + 2, threadTotal * 3, data);
            Crawler c4 = new Crawler("Crawler 4", dbFile, threadTotal * 3 + 3, threadTotal * 4 + rest, data);

            Thread t1 = new Thread(c1);
            Thread t2 = new Thread(c2);
            Thread t3 = new Thread(c3);
            Thread t4 = new Thread(c4);

            // start threads
            t1.start();
            t2.start();
            t3.start();
            t4.start();

            // join threads
            try {
                t1.join();
                t2.join();
                t3.join();
                t4.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        } catch (OfferMdt.OfferMdtException e) {
            handleCriticalException(new Exception("Cannot download pages total count."));
        } catch (Configuration.ConfigurationException e) {
            handleCriticalException(new Exception("Cannot download pages total count."));
        }

//        new Crawler(dbFile).go();
    }

    private static void exportToXls() throws FileSelector.FileSelectorException, CrawlerExportException, Reader.ReaderExceptin {
        File dbFile = new FileSelector().selectFileDat();
        File file = new FileSelector().selectPathToSaveXls();
        new XlsExporter().createXls(file.getAbsolutePath(), new Reader().read(dbFile));
    }
}