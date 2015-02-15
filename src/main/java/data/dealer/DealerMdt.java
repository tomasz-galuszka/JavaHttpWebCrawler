package data.dealer;

import config.Configuration;
import connection.DocumentMdt;
import data.db.Dealer;
import org.w3c.dom.Document;

public class DealerMdt {

    public class DealerMdtException extends Exception {

        public DealerMdtException(Throwable cause) {
            super(cause);
        }

        public DealerMdtException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private DocumentMdt mdt;
    private Configuration config;
    private DealerMdtTransformer transformer = new DealerMdtTransformer();

    public Dealer getPremiumCarDealer(String id) throws DealerMdtException {
        try {
            Document doc = getMdt().getPremiumCarDetailsPage(id);
            return transformer.getDealerFromDocument(doc);
        } catch (Exception e) {
            throw new DealerMdtException(e);
        }
    }

    public Dealer getCarDealer(String id) throws DealerMdtException {
        try {
            Document doc = getMdt().getNormalCarDetailsPage(id);
            return transformer.getDealerFromDocument(doc);
        } catch (Exception e) {
            throw new DealerMdtException(e);
        }
    }

    private DocumentMdt getMdt() throws Configuration.ConfigurationException {
        if (mdt == null) {
            mdt = new DocumentMdt(getConfig());
        }
        return mdt;
    }

    private Configuration getConfig() throws Configuration.ConfigurationException {
        if (config == null) {
            config = new Configuration();
        }
        return config;
    }
}
