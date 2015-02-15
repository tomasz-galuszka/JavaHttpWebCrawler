package connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.Configuration;
import connection.http.HttpConnector;
import connection.http.HttpConnector.HttpConnectorException;
import org.w3c.dom.Document;

public class DocumentMdt {

    private final Configuration config;
    private HttpConnector connector;

    public DocumentMdt(Configuration config) {
        this.config = config;
    }

    public Document getSearchPage(int pageNumber) throws HttpConnectorException {
        String requestUrl = getConfig().getBaseUrl() + String.format(getConfig().getBaseQuery(), pageNumber);
        return getConnector().getDocument(requestUrl);
    }

    public String getPremiumCarsPage(int pageNumber) throws HttpConnectorException {
        String requestAddress = getConfig().getBaseUrl() + String.format(getConfig().getTopArticlesQuery(), pageNumber);
         return getConnector().getResponseAsString(requestAddress);
    }

    public Document getPremiumCarDetailsPage(String id) throws HttpConnectorException {
        return getCarDetailsPage(id, true);
    }

    public Document getNormalCarDetailsPage(String id) throws HttpConnectorException {
        return getCarDetailsPage(id, false);
    }

    private Document getCarDetailsPage(String id, boolean isPremium) throws HttpConnectorException {
        String requestAddress = getConfig().getCarDetailsBaseUrl();
        requestAddress += String.format((isPremium ? getConfig().getCarDetailsQueryPremium() : getConfig().getCarDetailsQueryStandard()), id);

        return getConnector().getDocument(requestAddress);
    }

    private HttpConnector getConnector() {
        if (connector == null) {
            connector = new HttpConnector(getConfig());
        }
        return connector;
    }

    private Configuration getConfig() {
        return config;
    }
}
