package data.offer;

import com.google.gson.*;
import config.Configuration;
import connection.DocumentMdt;
import connection.http.HttpConnector;
import data.db.low.CarItem;
import data.xpath.SearchResultPageXpaths;
import data.xpath.XpathEvaluator;
import org.w3c.dom.Document;


public class OfferMdt {

    public class OfferMdtException extends Exception {

        public OfferMdtException(String message, Throwable cause) {
            super(message, cause);
        }

        public OfferMdtException(Throwable cause) {
            super(cause);
        }
    }

    private DocumentMdt mdt;
    private Configuration config;
    private Gson gson;

    public String getMainStringFromPage(int page) throws OfferMdtException, Configuration.ConfigurationException {
        try {
            Document doc = getMdt().getSearchPage(page);
            return XpathEvaluator.evaluateXpathString(doc, SearchResultPageXpaths.PAGE_ITEMS);
        } catch (HttpConnector.HttpConnectorException e) {
            throw new OfferMdtException(e);
        } catch (XpathEvaluator.XpathEvaluatorException e) {
            throw new OfferMdtException(e);
        }
    }

    public CarItem[] getCarsFromMainString(String mainString) throws OfferMdtException {
        CarItem[] cars;
        try {
            int startJsVariable = mainString.indexOf("var articlesFromServer = [{");
            int endJsVariableIndex = mainString.indexOf(";");

            String tmp = mainString.substring(startJsVariable, endJsVariableIndex);
            String result = tmp.substring(tmp.indexOf("[{"), tmp.length());

            cars = getGson().fromJson(result, CarItem[].class);
        } catch (JsonSyntaxException e) {
            throw new OfferMdtException("Problem while parsing json object from content", e);
        }
        return cars;
    }

    public CarItem[] getPremiumCars() throws OfferMdtException, Configuration.ConfigurationException {
        CarItem[] cars;
        try {
            String premiumCarsString = getMdt().getPremiumCarsPage(1);
            JsonObject obj = new JsonParser().parse(premiumCarsString).getAsJsonObject();
            String jsString = obj.get("ta").toString();

            premiumCarsString = jsString.substring(1, jsString.length() - 1);
            premiumCarsString = premiumCarsString.replace("\\", "");

            cars = getGson().fromJson(premiumCarsString, CarItem[].class);

        } catch (HttpConnector.HttpConnectorException e) {
            throw new OfferMdtException(e);
        } catch (JsonSyntaxException e) {
            throw new OfferMdtException("Problem while parsing json object from content", e);
        }
        return cars;
    }

    public String getPagesTotalCountFromMainString(String scriptContent) {
        int tcIndex = scriptContent.indexOf("var totalCount = ");
        String tcContent = scriptContent.substring(tcIndex, scriptContent.length());
        int tcEndIndex = tcContent.indexOf(";");
        return tcContent.substring(tcContent.indexOf("\"") + 1, tcEndIndex - 1);
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

    private Gson getGson() {
        if (gson == null) {
            gson = new GsonBuilder().create();
        }
        return gson;
    }
}
