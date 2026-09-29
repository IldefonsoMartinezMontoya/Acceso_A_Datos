package Practica3;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class LeerXML {
        static File Archivo = new File("src/Practica3/Becario.xml");
    static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document documento = builder.parse(Archivo);

        documento.getDocumentElement().normalize();

        NodeList becarios = documento.getElementsByTagName("Becario");

        for (int i = 0; i < becarios.getLength(); i++) {
            Element becario = (Element) becarios.item(i);

            String nombre = obtenerTexto(becario, "nombre");
            String sexo = obtenerTexto(becario, "sexo");
            String edad = obtenerTexto(becario, "edad");
            String sus = obtenerTexto(becario, "suspensosCursoAnterior");
            String res = obtenerTexto(becario, "residenciaParental");
            String sal = obtenerTexto(becario, "ingresosAnualesFamiliares");

            System.out.println("Becario " + (i + 1));
            System.out.println("------------------------------");
            System.out.println("Nombre: " + nombre);
            System.out.println("Sexo: " + sexo);
            System.out.println("Edad: " + edad);
            System.out.println("Suspensos en el curso anterior: " + sus);
            System.out.println("Residencia parental: " + res);
            System.out.println("Ingresos anuales de la familia: " + sal);
            System.out.println();
        }
    }
    private static String obtenerTexto(Element elemento, String etiqueta) {
        NodeList nodos = elemento.getElementsByTagName(etiqueta);
        if (nodos.getLength() == 0) {
            return "";
        }
        return nodos.item(0).getTextContent();
    }
}
