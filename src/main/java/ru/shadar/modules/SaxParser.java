package ru.shadar.modules;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.shadar.objects.Xml;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Реализация SaxParser-а для XML файлов
 */
public class SaxParser {
    private static boolean isFound;
    private static boolean isStarted = true;
    private static List<Map<String, String>> elementsList;

    public List<Map<String, String>> getListOfElementsMap() {
        return elementsList;
    }

    public SaxParser() {
        elementsList = new ArrayList<>();
        isFound = false;
    }

    public List<Map<String, String>> parse(Xml xml) throws IllegalArgumentException {
        try {
            run(xml.getPath());
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException il) {
            throw new IllegalArgumentException("Файл поврежден или не содержит требуемых данных");
        }
        return getListOfElementsMap();
    }

    public static void run(String path) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        XMLHandler handler = new XMLHandler("ROWS");
        parser.parse(Path.of(path).toFile(), handler);

        if (!isFound) {
            throw new IllegalArgumentException("File XML don't contains ROWS or invalid");
        }
    }

    private static class XMLHandler extends DefaultHandler {
        private String element;
        private boolean isEntered;

        public XMLHandler(String element) {
            this.element = element;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (isEntered) {
                Map<String, String> map = new HashMap<>();
                int length = attributes.getLength();
                List<String> list = new ArrayList<>();
                for (int i = 0; i < length; i++) {
                    map.put(attributes.getQName(i).toLowerCase(), attributes.getValue(i));
                }
                elementsList.add(map);
                isStarted = false;
            }
            if (qName.equals(element)) {
                isEntered = true;
                isFound = true;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals(element)) {
                isEntered = false;
            }
        }
    }
}
