package serialization;

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

    public DealersContainer read(File f) throws ReaderExceptin {
        if (f == null || f.exists() == false) {
            System.out.println("-- Can't find input file");
            return new DealersContainer();
        }

        try {
            System.out.println("-- Reading temporary file: " + f.getAbsolutePath());

            FileInputStream fin = new FileInputStream(f);
            ObjectInputStream ois = new ObjectInputStream(fin);
            DealersContainer result = (DealersContainer) ois.readObject();
            ois.close();

            System.out.println("-- Reading input data from tmp file: SUCCESS !");
            System.out.println("-- Last exploerd page: " + (result.getPage()) + "/" + result.getTotalCount());
            System.out.println("-- Downloaded dealers: " + result.getOffersmap().size());

            return result;
        } catch (Exception e) {
            throw new ReaderExceptin("-- Can't read from file" + f.getAbsolutePath(), e);
        }
    }

}
