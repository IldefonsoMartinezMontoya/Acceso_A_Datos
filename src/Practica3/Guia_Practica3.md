# Guía de los programas de Práctica 3

## CrearXML.java

CrearXML lee con DataInputStream el fichero binario src/Practica2/datosbeca.bin. Para cada becario espera, en este orden, nombre y sexo como UTF, edad y suspensos como enteros, residencia parental como UTF e ingresos familiares como double. El orden y los tipos deben coincidir con los del fichero binario.

Construye un documento DOM con la raíz Becarios y añade un elemento Becario por registro. Lo guarda con sangría en src/Practica3/Becario.xml. La escritura está dentro del bucle: cada guardado incluye los registros leídos hasta ese momento. Si el fichero binario está vacío, no se genera un XML nuevo.

```java
package Practica3;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stax.StAXResult;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.annotation.Documented;

public class CrearXML {
    static void main() throws ParserConfigurationException, IOException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document documento = builder.newDocument();

        try (DataInputStream entrada = new DataInputStream(new FileInputStream("src/Practica2/datosbeca.bin"))) {

            Element raiz = documento.createElement("Becarios");
            documento.appendChild(raiz);
            while (entrada.available() > 0) {
                Element becario = documento.createElement("Becario");
                raiz.appendChild(becario);

                String nombreLeido = entrada.readUTF();
                Element nombre = documento.createElement("nombre");
                nombre.setTextContent(nombreLeido);
                becario.appendChild(nombre);

                String sexoLeido = entrada.readUTF();
                Element sexo = documento.createElement("sexo");
                sexo.setTextContent(sexoLeido);
                becario.appendChild(sexo);

                int edadLeida = entrada.readInt();
                Element edad = documento.createElement("edad");
                edad.setTextContent(String.valueOf(edadLeida));
                becario.appendChild(edad);

                int susLeido = entrada.readInt();
                Element sus = documento.createElement("suspensosCursoAnterior");
                sus.setTextContent(String.valueOf(susLeido));
                becario.appendChild(sus);

                String resLeida = entrada.readUTF();
                Element res = documento.createElement("residenciaParental");
                res.setTextContent(resLeida);
                becario.appendChild(res);

                double salLeido = entrada.readDouble();
                Element sal = documento.createElement("ingresosAnualesFamiliares");
                sal.setTextContent(String.valueOf(salLeido));
                becario.appendChild(sal);

                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.transform(
                        new DOMSource(documento),
                        new StreamResult(new File("src/Practica3/Becario.xml"))
                );
            }
        }
    }
}
```

## LeerXML.java

LeerXML carga src/Practica3/Becario.xml como un documento DOM, normaliza su estructura y busca todos los elementos Becario. Para cada uno obtiene sus seis campos y muestra los valores en la consola con etiquetas descriptivas.

El método obtenerTexto busca una etiqueta dentro de un becario y devuelve su contenido. Si no encuentra esa etiqueta, devuelve una cadena vacía.

```java
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
```

## LeerXML_SAX.java

LeerXML_SAX procesa src/Practica3/Becario.xml con SAX. Su manejador registra la etiqueta actual, anuncia cada nuevo becario y muestra el texto de sus campos; al cerrar un becario imprime una línea en blanco.

SAX puede entregar el texto de una etiqueta en varias llamadas a characters. El programa imprime cada fragmento no vacío por separado, así que un valor podría aparecer dividido en casos donde el analizador entregue varios fragmentos.

```java
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
```
