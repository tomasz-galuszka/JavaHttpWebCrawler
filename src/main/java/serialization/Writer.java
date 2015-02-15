package serialization;

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

    public void write(DealersContainer data, File f) throws Exception {
        try {
            System.out.println("-- Writting to temporary file: " + f.getAbsolutePath());

            FileOutputStream fout = new FileOutputStream(f);
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(data);
            oos.close();

            System.out.println("");
            System.out.println("-- Writting data to temporary file: SUCCESS !");
            System.out.println("-- Downloaded dealers: " + data.getOffersmap().size());
            System.out.println("-- Current page: " + data.getPage());
            System.out.println("-- Total pages: " + data.getTotalCount());
            System.out.println("");

        } catch (Exception e) {
            throw new Exception("-- Can't write dealers to file" + f.getAbsolutePath(), e);
        }
    }
}
