import export.CrawlerExportException;
import export.XlsExporter;
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
        new Crawler(dbFile).go();
    }

    private static void exportToXls() throws FileSelector.FileSelectorException, CrawlerExportException, Reader.ReaderExceptin {
        File dbFile = new FileSelector().selectFileDat();
        File file = new FileSelector().selectPathToSaveXls();
        new XlsExporter().createXls(file.getAbsolutePath(), new Reader().read(dbFile));
    }
}