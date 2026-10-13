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
                System.out.println("  Ingrediente: " + ingrediente.getNombre() + " — " + ingrediente.getCantidad());
            }

            for (Paso paso : elemento.getPasos()) {
                System.out.println("  Paso " + paso.getOrden() + ": " + paso.getPaso());
            }
        }
    }
}
