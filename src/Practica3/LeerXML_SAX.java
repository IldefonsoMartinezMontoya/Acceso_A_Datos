package Practica3;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class LeerXML_SAX {
    static void main() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();

        DefaultHandler manejador = new DefaultHandler() {
            private String etiquetaActual = "";

            public void startElement(
                    String uri, String localName, String qName, Attributes atributos) {
                etiquetaActual = qName;
                if (qName.equals("Becario")) {
                    System.out.println("Nuevo Becario:");
                }
            }
            public void characters(char[] caracteres, int inicio, int longitud) {
                String texto = new String(caracteres, inicio, longitud).trim();
                if (!texto.isEmpty()) {
                    System.out.println(etiquetaActual + ": " + texto);
                }
            }

            public void endElement(String uri, String localName, String qName) {
                if (qName.equals("Becario")) {
                    System.out.println();
                }
                etiquetaActual = "";
            }
        };
        parser.parse(new File("src/Practica3/Becario.xml"), manejador);
    }
}
