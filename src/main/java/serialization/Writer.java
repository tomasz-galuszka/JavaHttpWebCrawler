package serialization;

import logger.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Writer {


    public class WriterException extends Exception {

        public WriterException() {
        }

        public WriterException(String message) {
            super(message);
        }

        public WriterException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public synchronized void write(DealersContainer data, File f) throws Exception {
        try {
            FileOutputStream fout = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(data);
            oos.close();

            Logger.log("");
            Logger.log(" Writting to temporary file: " + f.getAbsolutePath());
            Logger.log(" Downloaded dealers: " + data.getOffersMap().size());
            Logger.log(" Total pages: " + data.getTotalCount() / 80);
            Logger.log("");

        } catch (Exception e) {
            throw new Exception(" Can't write dealers to file" + f.getAbsolutePath(), e);
        }
    }
}
