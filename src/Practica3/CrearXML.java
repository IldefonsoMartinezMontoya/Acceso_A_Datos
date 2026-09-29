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
