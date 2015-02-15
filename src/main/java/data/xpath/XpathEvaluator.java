package data.xpath;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.UnsupportedEncodingException;

/**
 * Created by tomasz on 31.12.14.
 */
public class XpathEvaluator {

    private static final String CHARSET_CODE = "utf-8";

    public static class XpathEvaluatorException extends Exception {
        public XpathEvaluatorException() {
        }

        public XpathEvaluatorException(String message) {
            super(message);
        }

        public XpathEvaluatorException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static String evaluateXpathString(Document doc, String xpath) throws XpathEvaluatorException {
        try {
            String evaluate = (String) XPathFactory.newInstance().newXPath().evaluate(xpath, doc, XPathConstants.STRING);

            return new String(evaluate.getBytes(CHARSET_CODE), CHARSET_CODE);
        } catch (XPathExpressionException e) {
            handleException(e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String evaluateXpathString(Node n, String xpath) throws XpathEvaluatorException {
        try {
            String evaluate = (String) XPathFactory.newInstance().newXPath().evaluate(xpath, n, XPathConstants.STRING);
            return new String(evaluate.getBytes(CHARSET_CODE), CHARSET_CODE);
        } catch (XPathExpressionException e) {
            handleException(e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object evaluateXpathNode(Document doc, String xpath) throws XpathEvaluatorException {
        try {
            return XPathFactory.newInstance().newXPath().evaluate(xpath, doc, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            handleException(e);
        }
        return null;
    }

    public static Object evaluateXpath(Document doc, String xpath, QName xpathConstant) throws XpathEvaluatorException {
        try {
            return XPathFactory.newInstance().newXPath().evaluate(xpath, doc, xpathConstant);
        } catch (XPathExpressionException e) {
            handleException(e);
        }
        return null;
    }

    public static Object evaluateXpath(Node n, String xpath, QName xpathConstant) throws XpathEvaluatorException {
        try {
            return XPathFactory.newInstance().newXPath().evaluate(xpath, n, xpathConstant);
        } catch (XPathExpressionException e) {
            handleException(e);
        }
        return null;
    }

    private static void handleException(XPathExpressionException e) throws XpathEvaluatorException {
        throw new XpathEvaluatorException("Incorrect data.xpath.", e);
    }
}