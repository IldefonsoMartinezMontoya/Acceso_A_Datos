package Practica5;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

import java.io.File;
import java.util.Arrays;

public class TransformarConJAXB {
    public static void main(String[] args) throws JAXBException {
        JAXBContext contexto = JAXBContext.newInstance(Contenedor.class);

        Contenedor datos = (Contenedor) contexto.createUnmarshaller().unmarshal(new File("src/Practica5/pizzas.xml"));

        for (Pizza elemento : datos.getPizza()) {
            System.out.println(elemento.getNombre() + ", " + elemento.getPrecio() + ", " + Arrays.toString(elemento.getIngrediente()));
        }
    }
}
