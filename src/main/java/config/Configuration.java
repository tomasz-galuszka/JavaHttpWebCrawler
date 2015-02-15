package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Configuration {

    public class ConfigurationException extends Exception {

        private static final long serialVersionUID = 1L;

        public ConfigurationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private final static String CONFIG_FILE = "config.properties";

    private String baseUrl;
    private String baseQuery;
    private String topArticlesQuery;
    private String carDetailsBaseUrl;
    private String carDetailsQueryStandard;
    private String carDetailsQueryPremium;

    private String browserName;
    private Boolean useProxy;
    private String ip;
    private String port;
    private String type;
    private String serializationFile;

    public Configuration() throws ConfigurationException {
        FileInputStream inStream = null;
        try {
            Properties properties = new Properties();
            inStream = new FileInputStream(CONFIG_FILE);
            properties.load(inStream);

            baseUrl = properties.getProperty("baseUrl");
            baseQuery = properties.getProperty("baseQuery");
            topArticlesQuery = properties.getProperty("topArticlesQuery");
            carDetailsBaseUrl = properties.getProperty("carDetailsBaseUrl");
            carDetailsQueryStandard = properties.getProperty("carDetailsQueryStandard");
            carDetailsQueryPremium = properties.getProperty("carDetailsQueryPremium");

            browserName = properties.getProperty("browserName");
            useProxy = Boolean.valueOf(properties.getProperty("useProxy"));
            ip = properties.getProperty("ip");
            port = properties.getProperty("port");
            type = properties.getProperty("type");
            serializationFile = properties.getProperty("serializationFile");

        } catch (IOException e) {
            throw new ConfigurationException("Problem while reading configuration from " + CONFIG_FILE + " file.", e);
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }
            } catch (IOException e) {
                throw new ConfigurationException("Error when trying to close configuration.properties file.\n.", e);
            }
        }
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getBaseQuery() {
        return baseQuery;
    }

    public String getTopArticlesQuery() {
        return topArticlesQuery;
    }

    public String getCarDetailsBaseUrl() {
        return carDetailsBaseUrl;
    }

    public String getCarDetailsQueryStandard() {
        return carDetailsQueryStandard;
    }

    public String getCarDetailsQueryPremium() {
        return carDetailsQueryPremium;
    }

    public String getBrowserName() {
        return browserName;
    }

    public Boolean getUseProxy() {
        return useProxy;
    }

    public String getIp() {
        return ip;
    }

    public String getPort() {
        return port;
    }

    public String getType() {
        return type;
    }

    public String getSerializationFile() {
        return serializationFile;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "baseUrl='" + baseUrl + '\'' +
                ", baseQuery='" + baseQuery + '\'' +
                ", topArticlesQuery='" + topArticlesQuery + '\'' +
                ", carDetailsBaseUrl='" + carDetailsBaseUrl + '\'' +
                ", carDetailsQueryStandard='" + carDetailsQueryStandard + '\'' +
                ", carDetailsQueryPremium='" + carDetailsQueryPremium + '\'' +
                ", browserName='" + browserName + '\'' +
                ", useProxy=" + useProxy +
                ", ip='" + ip + '\'' +
                ", port='" + port + '\'' +
                ", type='" + type + '\'' +
                ", serializationFile='" + serializationFile + '\'' +
                '}';
    }
}
