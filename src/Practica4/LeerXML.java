package Practica4;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class LeerXML {
    static File Archivo = new File("src/Practica4/Becario.xml");

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document documento = builder.parse(Archivo);

        documento.getDocumentElement().normalize();

        NodeList becarios = documento.getElementsByTagName("Becario");

        for (int i = 0; i < becarios.getLength(); i++) {
            Element becario = (Element) becarios.item(i);

            String nombre = obtenertexto(becario, "nombre");
            String sexo = obtenertexto(becario, "sexo");
            String edad = obtenertexto(becario, "edad");
            String sus = obtenertexto(becario, "suspensosCursoAnterior");
            String res = obtenertexto(becario, "residenciaParental");
            String sal = obtenertexto(becario, "ingresosAnualesFamiliares");

            System.out.println("Becario" + (i + 1));
            System.out.println("------------------------------");
            System.out.println("Nombre: " + nombre);
            System.out.println("Sexo: " + sexo);
            System.out.println("Edad: " + edad);
            System.out.println("Suspensos del curso anterior: " + sus);
            System.out.println("Residencia Parental: " + res);
            System.out.println("Ingrersos anuales de la familia: " + sal);
        }
    }
    private static String obtenertexto(Element elemento, String etiqueta) {
        NodeList nodos = elemento.getElementsByTagName(etiqueta);
        if (nodos.getLength() == 0) {
            return "";
        }
        return nodos.item(0).getTextContent();
    }
}
