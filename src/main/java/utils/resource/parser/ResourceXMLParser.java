package utils.resource.parser;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Andrey
 * 24.04.14.
 */
public class ResourceXMLParser {
    public static Object readXML(String xmlFile) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            SaxHandler handler = new SaxHandler();
            saxParser.parse(xmlFile, handler);

            return handler.getObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


}