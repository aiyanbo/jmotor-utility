package org.jmotor.util;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.jmotor.util.converter.SimpleValueConverter;
import org.jmotor.util.exception.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import javax.xml.parsers.ParserConfigurationException;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.io.InputStream;

/**
 * Component:Utility
 * Description:Xml utilities
 * Date: 11-8-20
 *
 * @author Andy.Ai
 */
public class XmlUtilities {
    private static SAXReader reader;

    private XmlUtilities() {
    }

    public static String getAttribute(Node node, String xpathExpression) {
        Node attributeNode = node.selectSingleNode(transformAttributePattern(xpathExpression));
        return attributeNode == null ? null : attributeNode.getText();
    }

    public static String transformAttributePattern(String xpathExpression) {
        String _xpathExpression = xpathExpression;
        if (_xpathExpression.indexOf('@') == -1) {
            _xpathExpression = '@' + _xpathExpression;
        }
        return _xpathExpression;
    }

    public static Document loadDocument(InputStream inputStream) throws DocumentException {
        try {
            return getReader().read(inputStream);
        } finally {
            CloseableUtilities.closeQuietly(inputStream);
        }
    }

    public static Document loadDocument(String name) {
        InputStream is = null;
        try {
            is = StreamUtilities.getStream4ClassPath(name);
            if (null == is) {
                throw new NullPointerException("Xml stream can not be null.");
            }
            return getReader().read(is);
        } catch (DocumentException e) {
            throw new XMLParserException(e.getMessage(), e);
        } finally {
            CloseableUtilities.closeQuietly(is);
        }
    }

    public static void fillProperties(Object object, Node node) {
        PropertyDescriptor[] propertyDescriptors = ObjectUtilities.getPropertyDescriptors(object);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String attributeValue = XmlUtilities.getAttribute(node, propertyDescriptor.getName());
            if (StringUtilities.isNotBlank(attributeValue)) {
                Object value = SimpleValueConverter.convert(propertyDescriptor.getPropertyType(), attributeValue);
                if (value != null) {
                    ObjectUtilities.setPropertyValue(object, propertyDescriptor.getName(), value);
                }
            }
        }
    }

    public static void fillProperties(Object object, Attributes attributes) {
        PropertyDescriptor[] propertyDescriptors = ObjectUtilities.getPropertyDescriptors(object);
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            String attributeValue = attributes.getValue(propertyDescriptor.getName());
            if (StringUtilities.isNotBlank(attributeValue)) {
                Object value = SimpleValueConverter.convert(propertyDescriptor.getPropertyType(), attributeValue);
                if (value != null) {
                    ObjectUtilities.setPropertyValue(object, propertyDescriptor.getName(), value);
                }
            }
        }
    }

    public static void handleSAX(String xmlContent, String charset, DefaultHandler saxHandler)
            throws IOException, SAXException, ParserConfigurationException {
        handleSAX(StreamUtilities.transform(xmlContent, charset), saxHandler);
    }

    public static void handleSAX(String xmlContent, DefaultHandler saxHandler)
            throws IOException, SAXException, ParserConfigurationException {
        handleSAX(StreamUtilities.transform(xmlContent), saxHandler);
    }

    public static void handleSAX(InputStream inputStream, DefaultHandler saxHandler)
            throws ParserConfigurationException, SAXException, IOException {
//        SAXParserFactory factory = SAXParserFactory.newInstance();
//        factory.setValidating(false);
//        SAXParser saxParser = factory.newSAXParser();
//        saxParser.parse(inputStream, saxHandler);
        try {
            InputSource inputSource = new InputSource(inputStream);
            handleSAX(inputSource, saxHandler);
        } finally {
            CloseableUtilities.closeQuietly(inputStream);
        }
    }

    public static void handleSAX(InputSource inputSource, DefaultHandler saxHandler)
            throws SAXException, IOException {
        XMLReader reader = XMLReaderFactory.createXMLReader();
        reader.setContentHandler(saxHandler);
        reader.parse(inputSource);
    }

    private static SAXReader getReader() {
        if (reader == null) {
            reader = new SAXReader();
        }
        return reader;
    }
}
