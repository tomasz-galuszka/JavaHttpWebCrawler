package serialization;

import logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Reader {

    public class ReaderExceptin extends Exception {
        public ReaderExceptin() {
        }

        public ReaderExceptin(String message) {
            super(message);
        }

        public ReaderExceptin(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public synchronized DealersContainer read(File f) throws ReaderExceptin {
        if (f == null || !f.exists()) {
            Logger.log(" Can't find input file");
            return new DealersContainer();
        }

        try {

            FileInputStream fin = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fin);
            DealersContainer result = (DealersContainer) ois.readObject();
            ois.close();

            Logger.log(" Reading temporary file: " + f.getAbsolutePath());
            Logger.log(" Downloaded dealers: " + result.getOffersMap().size());

            return result;
        } catch (Exception e) {
            throw new ReaderExceptin(" Can't read from file" + f.getAbsolutePath(), e);
        }
    }

}
