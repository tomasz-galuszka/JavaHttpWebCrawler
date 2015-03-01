package export;

import data.db.Car;
import data.db.Dealer;
import jxl.CellView;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import logger.Logger;

import java.util.Map;
import java.util.Set;

public class DealersSheet {

    private final String NAME_COLUMN = "Dealer name";
    private final String STREET_COLUMN = "Street";
    private final String ZIPCODE_COLUMN = "Zip code";
    private final String CITY_COLUMN = "City";
    private final String PHONE_COLUMN = "Phone";
    private final String WWW_COLUMN = "Page";
    private final String COUNT_COLUMN = "Offers";

    public void export(WritableWorkbook workbook, Map<Dealer, Set<Car>> offersMap) throws CrawlerExportException {
        WritableSheet sheet = workbook.createSheet("Dealers", 0);
        createHeader(sheet);
        try {
            fillWithData(offersMap, sheet, sheet.getRows());
        } catch (RowsExceededException e) {
            Logger.log(e.getMessage());
        }
        expandColumns(sheet, 7);
    }

    private void createHeader(WritableSheet sheet) throws CrawlerExportException {
        WritableFont arial14font = new WritableFont(WritableFont.ARIAL, 13, jxl.write.WritableFont.BOLD);
        WritableCellFormat arial14format = new WritableCellFormat(arial14font);
        try {
            arial14format.setBackground(Colour.LIGHT_GREEN);
            arial14format.setBorder(Border.ALL, BorderLineStyle.THIN);
        } catch (WriteException e1) {
            throw new CrawlerExportException("Error while creating xls file table header.", e1);
        }
        try {
            sheet.setRowView(1, 400);
            sheet.addCell(new Label(1, 1, NAME_COLUMN, arial14format));
            sheet.addCell(new Label(2, 1, STREET_COLUMN, arial14format));
            sheet.addCell(new Label(3, 1, ZIPCODE_COLUMN, arial14format));
            sheet.addCell(new Label(4, 1, CITY_COLUMN, arial14format));
            sheet.addCell(new Label(5, 1, PHONE_COLUMN, arial14format));
            sheet.addCell(new Label(6, 1, WWW_COLUMN, arial14format));
            sheet.addCell(new Label(7, 1, COUNT_COLUMN, arial14format));
        } catch (RowsExceededException e) {
            throw new CrawlerExportException("Row exceeded exception (header)", e);
        } catch (WriteException e) {
            throw new CrawlerExportException("Error while creating xls file table header.", e);
        }
    }

    private void fillWithData(Map<Dealer, Set<Car>> offersMap, WritableSheet sheet, int startRow) throws RowsExceededException, CrawlerExportException {
        Logger.log(" Filling dealers");
        try {
            WritableFont arial10font = new WritableFont(WritableFont.ARIAL, 10);
            WritableCellFormat arial10format = new WritableCellFormat(arial10font);
            arial10format.setWrap(true);
            arial10format.setBackground(Colour.GRAY_25);
            arial10format.setBorder(Border.ALL, BorderLineStyle.THIN);
            Set<Dealer> keySet = offersMap.keySet();
            int i = startRow;
            for (Dealer dealer : keySet) {
                sheet.setRowView(i, 1000);
                if (dealer == null) {
                    Logger.log("null, offers: " + offersMap.get(dealer).size());
                    continue;
                }
                sheet.addCell(new Label(1, i, dealer.getName() == null ? "" : dealer.getName(), arial10format));
                sheet.addCell(new Label(2, i, dealer.getStreet() == null ? "" : dealer.getStreet(), arial10format));
                sheet.addCell(new Label(3, i, dealer.getZipCode() == null ? "" : dealer.getZipCode(), arial10format));
                sheet.addCell(new Label(4, i, dealer.getZipCity() == null ? "" : dealer.getZipCity(), arial10format));
                sheet.addCell(new Label(5, i, dealer.getPhone() == null ? "" : dealer.getPhone(), arial10format));
                sheet.addCell(new Label(6, i, dealer.getWww() == null ? "" : dealer.getWww(), arial10format));
                sheet.addCell(new Label(7, i, String.valueOf(offersMap.get(dealer).size()), arial10format));
                i++;
            }
        } catch (WriteException e) {
            throw new CrawlerExportException("Error while filing with dealers data. Try again", e);
        }
    }

    private void expandColumns(WritableSheet sheet, int amountOfColumns) {
        int c = amountOfColumns;

        CellView cell1 = sheet.getColumnView(1);
        cell1.setSize(7 * 256);
        sheet.setColumnView(1, cell1);

        for (int x = 1; x <= c; x++) {

            if (x == 5 || x == amountOfColumns) {
                CellView cell = sheet.getColumnView(x);
                cell.setSize(17 * 256);
                sheet.setColumnView(x, cell);
                continue;
            }

            CellView cell = sheet.getColumnView(x);
            cell.setSize(35 * 256);
            sheet.setColumnView(x, cell);
        }
    }
}
