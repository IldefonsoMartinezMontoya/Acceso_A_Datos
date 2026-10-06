# Práctica 4: XML con XStream, XSLT y DOM

## Objetivo y recorrido

La práctica lee registros de un fichero binario, los convierte a objetos Java y guarda esos objetos como XML con XStream. Después aplica una plantilla XSLT para generar una página HTML. También incluye un lector DOM que muestra el contenido del XML en la consola.

El recorrido es: `datosbeca.bin` -> `Becario_Xstream.java` -> `Becario.xml` -> `HTML.xsl` -> `Becario.html`. `LeerXML.java` lee `Becario.xml` y muestra los datos por consola.

## Archivos de entrada y salida

- Entrada binaria: `src/Practica2/datosbeca.bin`.
- XML generado: `src/Practica4/Becario.xml`.
- Plantilla de transformación: `src/Practica4/HTML.xsl`.
- HTML generado: `src/Practica4/Becario.html`.
- Lector DOM: `src/Practica4/LeerXML.java`.

Las rutas son relativas al directorio de trabajo configurado en IntelliJ. El proceso termina con código 0 cuando finaliza sin error. Los avisos sobre `sun.misc.Unsafe` mostrados por XStream en Java 27 son advertencias de la biblioteca; no impiden por sí solos que se escriban los archivos.

## 1. Leer el binario y generar XML y HTML

`Becario_Xstream.java` lee cada registro de `datosbeca.bin` en un orden fijo: nombre y sexo como UTF, edad y suspensos como enteros, residencia como UTF e ingresos como double. Los tipos y el orden deben coincidir con los usados al escribir el fichero binario.

Cada registro se convierte en un objeto `Becario` y se añade a la lista de la clase contenedora `Becarios`. XStream serializa la lista a `Becario.xml`. Luego `TransformerFactory` aplica `HTML.xsl` a ese XML y guarda el resultado como `Becario.html`.

Los setters validan los valores: los textos no pueden estar vacíos, la edad y los ingresos no pueden ser negativos, y los suspensos deben estar entre 0 y 4.

### Código actual: `Becario_Xstream.java`

```java
package Practica4;

import Practica2.Becario;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class Becario_Xstream {
    //PRIMER PASO: CREAR LAS RUTAS DE LOS ARCHIVOS
    private static final Path Archivo = Path.of("src/Practica2/datosbeca.bin");
    private static final Path Objetivo = Path.of("src/Practica4/Becario.xml");

    public static void main(String[] args) {
        //SEGUNDO PASO: CREAR LA RAÍZ
        Becarios datos = new Becarios();
        try (DataInputStream dis = new DataInputStream(new FileInputStream(Archivo.toFile()))){
            while (dis.available() > 0) {
                String nombre = dis.readUTF();
                String sexo = dis.readUTF();
                int edad = dis.readInt();
                int sus = dis.readInt();
                String res = dis.readUTF();
                double sal = dis.readDouble();
                datos.getLista().add(
                        new Becario(nombre, sexo, edad, sus, res, sal)
                );
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //TERCER PASO: CREAR XSTREAM
        XStream xstream = new XStream();
        xstream.processAnnotations(new Class<?>[] {
                Becarios.class,
                Becario.class
        });
        //CUARTO PASO: TRANSFORMAR LOS DATOS RECOGIDOS CON WRITER EN XML
        try (Writer wr = Files.newBufferedWriter(Objetivo, StandardCharsets.UTF_8)){
            xstream.toXML(datos, wr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Path plantilla = Path.of("src/Practica4/HTML.xsl");
        Path pagina = Path.of("src/Practica4/Becario.html");

        try {
            Transformer transformador = TransformerFactory.newInstance()
                    .newTransformer(new StreamSource(plantilla.toFile()));

            transformador.transform(
                    new StreamSource(Objetivo.toFile()),
                    new StreamResult(pagina.toFile())
            );
        } catch (TransformerException e) {
            throw new RuntimeException("No se pudo transformar el XML a HTML", e);
        }
    }
    //QUINTO PASO: INICIALIZAR LA RAÍZ
    static class Becarios {
        @XStreamImplicit(itemFieldName = "Becario")
        private final List<Becario> lista = new ArrayList<>();

        public List<Becario> getLista() {
            return lista;
        }
    }
    //SEXTO PASO: CREAR LOS OBJETOS DE LA RAÍZ CON CONSTRUCTOR, GETTER Y SETTER
    @XStreamAlias("Becario")
    static class Becario {
        @XStreamAlias("nombre")
        private String nombre;
        @XStreamAlias("sexo")
        private String sexo;
        @XStreamAlias("edad")
        private int edad;
        @XStreamAlias("suspensosCursoAnterior")
        private int sus;
        @XStreamAlias("residenciaParental")
        private String res;
        @XStreamAlias("ingresosAnualesFamiliares")
        private double sal;

        public Becario (String nombre, String sexo, int edad, int sus, String res, double sal) {
            setNombre(nombre);
            setSexo(sexo);
            setEdad(edad);
            setSus(sus);
            setRes(res);
            setSal(sal);
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            if (nombre.isEmpty()) {
                throw new IllegalArgumentException("No puede estar vacío");
            } else {
                this.nombre = nombre;
            }
        }

        public String getSexo() {
            return sexo;
        }

        public void setSexo(String sexo) {
            if (sexo.isEmpty()) {
                throw new IllegalArgumentException("No puede estar vacío");
            } else {
                this.sexo = sexo;
            }
        }

        public int getEdad() {
            return edad;
        }

        public void setEdad(int edad) {
            if (edad < 0) {
                throw new IllegalArgumentException("No puede ser negativa");
            } else {
                this.edad = edad;
            }
        }

        public int getSus() {
            return sus;
        }

        public void setSus(int sus) {
            if (sus < 0 || sus > 4) {
                throw new IllegalArgumentException("No puede ser negativo o mayor de 4");
            } else {
                this.sus = sus;
            }
        }

        public String getRes() {
            return res;
        }

        public void setRes(String res) {
            if (res.isEmpty()) {
                throw new IllegalArgumentException("No puede estar vacio");
            } else {
                this.res = res;
            }
        }

        public double getSal() {
            return sal;
        }

        public void setSal(double sal) {
            if (sal < 0) {
                throw new IllegalArgumentException("No puede ser negativo");
            } else {
                this.sal = sal;
            }
        }
    }
}
```

## 2. Plantilla XSLT para crear la tabla HTML

`HTML.xsl` indica que la salida debe ser HTML. Obtiene los encabezados de los campos del primer `Becario`, recorre cada `Becario` de la raíz y genera una fila con una celda por campo. El selector `name(/*)` pone el nombre de la raíz XML en el título y encabezado HTML.

La plantilla incluye un bloque de comentario con una indicación para añadir `xsl:sort` si el ejercicio pide ordenar los registros. Como la instrucción está comentada y `CAMPO` es solo un marcador, ahora mismo la tabla no ordena los datos. También hay un comentario `TODO` para añadir estilos CSS; por sí solo no aplica ningún estilo.

La clase contenedora `Becarios` no tiene alias XStream, así que el XML actual usa como raíz el nombre completo derivado de la clase. La plantilla sigue encontrando los registros, pero ese nombre largo también aparece como título HTML. Añadir `@XStreamAlias` para asignar una raíz como `Becarios` sobre la clase contenedora permitiría usar un nombre más sencillo.

### Código actual: `HTML.xsl`

```xml
<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="UTF-8" indent="yes"/>
    <xsl:template match="/">
        <html>
            <head>
                <meta charset="UTF-8"/>
                <title>
                    <xsl:value-of select="name(/*)"/>
                </title>
                <!-- TODO: puedes añadir aquí estilos CSS -->
            </head>
            <body>
                <h1>
                    <xsl:value-of select="name(/*)"/>
                </h1>

                <table border="1">
                    <thead>
                        <tr>
                            <!-- Crea una columna por cada campo del primer registro -->
                            <xsl:for-each select="/*/*[1]/*">
                                <th>
                                    <xsl:value-of select="name()"/>
                                </th>
                            </xsl:for-each>
                        </tr>
                    </thead>

                    <tbody>
                        <!-- Recorre los registros que cuelgan de la raíz -->
                        <xsl:for-each select="/*/*">
                            <tr>
                                <!-- Escribe una celda por cada campo del registro -->
                                <xsl:for-each select="*">
                                    <td>
                                        <xsl:value-of select="."/>
                                    </td>
                                </xsl:for-each>
                            </tr>
                        </xsl:for-each>
                    </tbody>
                </table>
            </body>
        </html>
    </xsl:template>

</xsl:stylesheet>
```

## 3. Leer el XML con DOM

`LeerXML.java` crea un `DocumentBuilder`, carga `Becario.xml` y normaliza el árbol DOM. Después busca todos los elementos `Becario`, recorre cada registro y obtiene el texto de sus campos. El método `obtenertexto` devuelve una cadena vacía si falta una etiqueta.

Este programa solo lee el XML e imprime sus datos; no genera ni modifica el HTML.

### Código actual: `LeerXML.java`

```java
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
        //PRIMER PASO: CREAR EL DOCUMENTO
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document documento = builder.parse(Archivo);

        documento.getDocumentElement().normalize();

        NodeList becarios = documento.getElementsByTagName("Becario");

        //SEGUNDO PASO: RECORRER EL XML
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
    //TERCER PASO: TRANSFORMAR A TEXTO
    private static String obtenertexto(Element elemento, String etiqueta) {
        NodeList nodos = elemento.getElementsByTagName(etiqueta);
        if (nodos.getLength() == 0) {
            return "";
        }
        return nodos.item(0).getTextContent();
    }
}
```

