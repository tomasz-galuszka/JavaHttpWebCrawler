package data.dealer;

import data.db.Dealer;
import data.xpath.CarDetailsPageXpaths;
import data.xpath.XpathEvaluator;
import logger.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class DealerMdtTransformer {

    private final String COMMA_SEPARATOR = ", ";

    public class DealerMdtTransformerException extends Exception {

        public DealerMdtTransformerException(Throwable cause) {
            super(cause);
        }

        public DealerMdtTransformerException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public boolean isDealerPage(Document doc) {
        try {
            String s = XpathEvaluator.evaluateXpathString(doc, CarDetailsPageXpaths.OFFER_TYPE_XPATH);
            if (s.trim().contains("Annuncio di privato")) {
                Logger.log(" Private offer - skipped");
                return false;
            }
        } catch (XpathEvaluator.XpathEvaluatorException e) {
            Logger.log(" Private offer - skipped");
            return false;
        }
        return true;
    }

    public Dealer getDealerFromDocument(Document doc) throws DealerMdtTransformerException {
        String stringFromDocument = getStringFromDocument(doc);
        Dealer dealer = null;
        try {

            org.jsoup.nodes.Document scriptTagDoc = getDocumentFromContactScriptTag(stringFromDocument);

            String name = fetchDealerName(scriptTagDoc);
            String link = fetchDealerLink(scriptTagDoc);
            String phone = fetchDealerPhone(scriptTagDoc);
            String street = fetchDealerStreet(scriptTagDoc);
            String zipCode = fetchDealerZipCode(scriptTagDoc);
            String city = fetchDealerCity(scriptTagDoc);


            dealer = new Dealer();
            dealer.setName(name);
            dealer.setWww(link);
            dealer.setPhone(phone);
            dealer.setStreet(street);
            dealer.setZipCode(city);
            dealer.setZipCity(city);

        } catch (Exception e) {
            throw new DealerMdtTransformerException(e);
        }
        return dealer;
    }

    private String fetchDealerCity(org.jsoup.nodes.Document scriptTagDoc) {
        String result = "";
        try {
            Elements customerCity = scriptTagDoc.getElementsByAttributeValue("data-test", "city");
            for (Element e : customerCity) {
                result += e.text() + COMMA_SEPARATOR;
            }
            result = removeLastSeparator(result);
        } catch (Exception e) {
            Logger.log(" City field is empty");
            return result;
        }
        return result;
    }

    private String fetchDealerZipCode(org.jsoup.nodes.Document scriptTagDoc) {
        String result = "";
        try {
            Elements customerZipSign = scriptTagDoc.getElementsByAttributeValue("data-test", "zip");
            for (Element e : customerZipSign) {
                result += e.text() + COMMA_SEPARATOR;
            }
            result = removeLastSeparator(result);
            result += " ";

            Elements zipCode = scriptTagDoc.getElementsByAttributeValue("data-test", "zipCode");
            for (Element e : zipCode) {
                result += e.text() + COMMA_SEPARATOR;
            }
            result = removeLastSeparator(result);
        } catch (Exception e) {
            Logger.log(" ZipCode field is empty");
            return result;
        }

        return result;
    }

    private String fetchDealerStreet(org.jsoup.nodes.Document scriptTagDoc) {
        String result = "";
        try {
            Elements customerAddress = scriptTagDoc.getElementsByAttributeValue("data-test", "customerAddress");
            for (Element e : customerAddress) {
                result += e.text() + COMMA_SEPARATOR;
            }
            result = removeLastSeparator(result);
        } catch (Exception e) {
            Logger.log(" Street field is empty");
            return result;
        }
        return result;
    }

    private String fetchDealerPhone(org.jsoup.nodes.Document scriptTagDoc) {
        String result = "";
        try {
            Elements phoneElements = scriptTagDoc.getElementsByAttributeValue("data-test", "phoneNumbers");
            for (Element e : phoneElements) {
                result += e.text() + COMMA_SEPARATOR;
            }
            result = removeLastSeparator(result);
        } catch (Exception e) {
            Logger.log(" Phone field is empty");
            return result;
        }
        return result;
    }

    private String fetchDealerLink(org.jsoup.nodes.Document scriptTagDoc) {
        String result = "";
        try {
            Elements sellerCompanyLink = scriptTagDoc.getElementsByAttributeValue("data-test", "sellerCompanyLink");
            Elements links = sellerCompanyLink.select("a[href]");
            result = links.get(0).attr("href");
        } catch (Exception e) {
            Logger.log(" Link field is empty");
            return result;
        }
        return result;
    }

    private String fetchDealerName(org.jsoup.nodes.Document scriptTagDoc) {
        String result = "";
        try {
            Elements sellerCompanyLink = scriptTagDoc.getElementsByAttributeValue("data-test", "sellerCompanyLink");
            Elements links = sellerCompanyLink.select("a[href]");
            result = links.get(0).text();
        } catch (Exception e) {
            Logger.log(" Name field is empty");
            return result;
        }
        return result;
    }

    private org.jsoup.nodes.Document getDocumentFromContactScriptTag(String stringFromDocument) {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(stringFromDocument, "UTF-8");
        Elements select1 = jsoupDoc.select("script[id=contactTabTemplate]");
        Element select2 = null;
        for (Element e : select1) {
            select2 = e;
        }
        DataNode dataNode = select2.dataNodes().get(0);
        String data1 = dataNode.attr("data");

        org.jsoup.nodes.Document parse = Jsoup.parse(data1);
        String text = parse.body().text();
        return Jsoup.parse(text);
    }

    private String removeLastSeparator(String result) {
        if (result.isEmpty()) {
            return result;
        }
        String lastTwoChars = result.substring(result.length() - COMMA_SEPARATOR.length(), result.length());
        if (lastTwoChars.equals(COMMA_SEPARATOR)) {
            result = result.substring(0, result.length() - COMMA_SEPARATOR.length());
        }
        return result;
    }

    public String getStringFromDocument(Document doc) {
        try {
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        } catch (TransformerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
