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
