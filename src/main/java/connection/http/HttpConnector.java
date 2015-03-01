package connection.http;

import config.Configuration;
import connection.http.cookie.Cookie;
import connection.http.cookie.CookieManager;
import org.apache.commons.lang3.StringEscapeUtils;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.Set;

/**
 * Created by tomasz on 31.12.14.
 */
public class HttpConnector {

    public class HttpConnectorException extends Exception {

        public HttpConnectorException() {
        }

        public HttpConnectorException(String message) {
            super(message);
        }

        public HttpConnectorException(String message, Throwable cause) {
            super(message, cause);
        }

    }
    private final String CHARSET_NAME = "utf-8";
    private final Configuration config;

    private Set<Cookie> cookies;
    private boolean afterFirstConnection = false;

    public HttpConnector(Configuration config) {
        this.config = config;
    }

    public Document getDocument(String www) throws HttpConnectorException {
        Document document;
        try {
            HttpURLConnection connection = connect(www);
            cookies = new CookieManager(connection).readCookies();
            String response = getResponseAsString(connection);
            byte[] bytes = response.getBytes(CHARSET_NAME);
            String htmlContent = new String(bytes, CHARSET_NAME);

            htmlContent = StringEscapeUtils.unescapeHtml4(htmlContent);

            TagNode cleanedHtml = new HtmlCleaner().clean(htmlContent);
            CleanerProperties props = new CleanerProperties();
            props.setCharset(CHARSET_NAME);
            document = new DomSerializer(props).createDOM(cleanedHtml);

        } catch (IOException e) {
            throw new HttpConnectorException("Error while downloading web page", e);
        } catch (ParserConfigurationException e) {
            throw new HttpConnectorException("Error while downloading web page", e);
        }
        return document;
    }

    public String getResponseAsString(String www) throws HttpConnectorException {
        String response = "";
        try {
            HttpURLConnection connection = connect(www);
            cookies = new CookieManager(connection).readCookies();
            response = StringEscapeUtils.unescapeHtml4(getResponseAsString(connection));
        } catch (IOException e) {
            throw new HttpConnectorException("Error while downloading web page", e);
        }
        return response;
    }

    private String getResponseAsString(HttpURLConnection connection) throws IOException, HttpConnectorException {
        BufferedReader bufferedIn = new BufferedReader(new InputStreamReader(connection.getInputStream(), CHARSET_NAME));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = bufferedIn.readLine()) != null) {
            response.append(line);
        }
        bufferedIn.close();
        return response.toString();
    }

    private HttpURLConnection connect(String www) throws IOException, HttpConnectorException {
        try {
            Thread.sleep(config.getSleepTime());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        HttpURLConnection connection;
        try {
            URL url = new URL(www);
            if (config.getUseProxy().equals(true)) {
                if (config.getType().equalsIgnoreCase("SOCKS")) {
                    Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress(config.getIp(), Integer.parseInt(config.getPort())));
                    connection = (HttpURLConnection) url.openConnection(proxy);
                } else {
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(config.getIp(), Integer.parseInt(config.getPort())));
                    connection = (HttpURLConnection) url.openConnection(proxy);
                }

            } else {
                connection = (HttpURLConnection) url.openConnection();
            }
            if (afterFirstConnection) {
                connection.setRequestProperty("Cookie", getCookiesString(cookies));
            }
            afterFirstConnection = true;
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", config.getBrowserName());
            connection.setRequestProperty("Content-Type", "text/html; charset=" + CHARSET_NAME);

            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                throw new IOException("Error while connection, http response code: " + responseCode);
            }
        } catch (MalformedURLException e) {
            throw new HttpConnectorException("Incorrect url address, check if address : " + www + " exists", e);
        }
        return connection;
    }

    private String getCookiesString(Set<Cookie> cookies) throws HttpConnectorException {
        if (cookies == null || cookies.isEmpty()) {
            return "";
        }

        StringBuilder cookieBuilder = new StringBuilder();
        for (Cookie c : cookies) {
            cookieBuilder.append(c.convertToStringHeader()).append(";");
        }
        cookieBuilder.deleteCharAt(cookieBuilder.length() - 1);
        return cookieBuilder.toString();
    }
}