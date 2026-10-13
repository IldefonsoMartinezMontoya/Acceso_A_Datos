# Práctica 5: explicación de programas JAXB

Este documento describe los tres programas de la carpeta Practica5 y reproduce sus fuentes actuales. JAXB enlaza objetos Java con XML: Unmarshaller lee XML y crea objetos, mientras que Marshaller escribe objetos como XML.

## TransformarPizzasConJAXB

Este programa lee src/Practica5/pizzas.xml y convierte el documento en un objeto ListaPizzas. Luego recorre la lista de pizzas y muestra el nombre, el precio y todos los ingredientes de cada una. No modifica el XML.

### Código

```java
package Practica5;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

import java.io.File;
import java.util.Arrays;

public class TransformarPizzasConJAXB {
    public static void main(String[] args) throws JAXBException {
        JAXBContext contexto = JAXBContext.newInstance(ListaPizzas.class);

        ListaPizzas datos = (ListaPizzas) contexto.createUnmarshaller().unmarshal(new File("src/Practica5/pizzas.xml"));

        for (Pizza elemento : datos.getPizza()) {
            System.out.println(elemento.getNombre() + ", " + elemento.getPrecio() + ", " + Arrays.toString(elemento.getIngrediente()));
        }
    }
}
```

### Código de ListaPizzas.java

```java
package Practica5;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "pizzas")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListaPizzas {
    protected List<Pizza> pizza;
    public ListaPizzas() {}

    public List<Pizza> getPizza() {
        return pizza;
    }
}
```

## TransformarRecetasConJAXB

Este programa lee src/Practica5/recetas.xml y crea un objeto ListaRecetas. Para cada receta muestra sus datos generales; después recorre sus ingredientes para imprimir el nombre y la cantidad, y sus pasos para imprimir el orden y el texto. La clase Ingrediente representa los atributos nombre y cantidad, y Paso representa el atributo orden y el texto del paso.

En el bloque de código, \u2014 es el escape Java del separador largo que aparece en el literal de salida del fuente; produce el mismo carácter.

### Código

```java
package Practica5;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

import java.io.File;

public class TransformarRecetasConJAXB {
    public static void main(String[] args) throws JAXBException {
        JAXBContext contexto = JAXBContext.newInstance(ListaRecetas.class);
        ListaRecetas datos = (ListaRecetas) contexto.createUnmarshaller().unmarshal(new File("src/Practica5/recetas.xml"));
        for (Receta elemento : datos.getReceta()) {
            System.out.println(elemento.getTipo().getDefinicion() + ", " + elemento.getDificultad() + ", " + elemento.getNombre() + ", " + elemento.getCalorias() +
                    ", " + elemento.getTiempo() + ", " + elemento.getElaboracion());

            for (Ingrediente ingrediente : elemento.getIngredientes()) {
                System.out.println("  Ingrediente: " + ingrediente.getNombre() + " \u2014 " + ingrediente.getCantidad());
            }

            for (Paso paso : elemento.getPasos()) {
                System.out.println("  Paso " + paso.getOrden() + ": " + paso.getPaso());
            }
        }
    }
}
```

### Código de ListaRecetas.java

```java
package Practica5;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "recetas")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListaRecetas {
    protected List<Receta> receta;
    public List<Receta> getReceta() {
        return receta;
    }
}
```

## CrearXMLJAXB

Este programa pide por consola el diámetro, la altura y el identificador de un depósito. Después crea un Deposito, lo agrega a una ListaDeposito y usa un Marshaller para intentar escribir la lista como XML con formato legible.

**Avisos sobre el código actual:**

- CrearXMLJAXB declara un campo llamado depositos, pero no lo usa. El programa agrega el depósito a la lista de ListaDeposito.
- ListaDeposito no inicializa su lista depositos; por eso, tal como está ahora, lista.getDepositos().add(...) produce un NullPointerException antes de escribir el archivo.
- Tras nextFloat(), nextLine() suele consumir el salto de línea pendiente, no el identificador escrito. El valor de id puede quedar vacío.
- La ruta pasada a marshal termina en CrearXMLJAXB.java. Si se alcanza esa instrucción, el XML se escribe encima del propio archivo fuente Java. La ruta debe revisarse antes de ejecutar el programa; en el estado actual, el error de lista nula ocurre antes de llegar ahí.

### Código

```java
package Practica5;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.SchemaOutputResolver;
import org.glassfish.jaxb.runtime.v2.runtime.output.StAXExStreamWriterOutput;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CrearXMLJAXB {
    private List<Deposito> depositos = new ArrayList<>();
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws JAXBException {
        ListaDeposito lista = new ListaDeposito();
        System.out.println("Dime el diametro");
        float diametro = sc.nextFloat();
        System.out.println("Dime la altura");
        float altura = sc.nextFloat();
        System.out.println("Dime el id");
        String id = sc.nextLine();
        lista.getDepositos().add(new Deposito(diametro, altura, id));

        JAXBContext contexto = JAXBContext.newInstance(ListaDeposito.class);
        Marshaller marshaller = contexto.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(lista, new File("src/Practica5/CrearXMLJAXB.java"));
    }
}
```

### Código de ListaDeposito.java

```java
package Practica5;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "depositos")
@XmlAccessorType(XmlAccessType.FIELD)
public class ListaDeposito {
    @XmlElement(name = "deposito")
    private List<Deposito> depositos;

    public ListaDeposito() {
    }

    public List<Deposito> getDepositos() {
        return depositos;
    }
}
```
