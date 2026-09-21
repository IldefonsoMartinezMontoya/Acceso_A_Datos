package PruebaInicial;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Conjunto {
    private Object[] elementos;

    /**
     * Constructor que recibe un array de Object.
     * Si el array es homogéneo (todos los elementos son de la misma clase), se guardan sus elementos.
     * En caso contrario, o si es nulo, el conjunto se crea vacío.
     */
    public Conjunto(Object[] datos) {
        if (datos != null && datos.length > 0 && esHomogeneo(datos)) {
            // Eliminamos duplicados para mantener la propiedad de un conjunto
            this.elementos = Arrays.stream(datos).distinct().toArray();
        } else {
            this.elementos = new Object[0];
        }
    }

    /**
     * Método público y estático que determina si todos los elementos
     * de un array de Object son exactamente del mismo tipo.
     */
    public static boolean esHomogeneo(Object[] datos) {
        if (datos == null || datos.length <= 1) {
            return true;
        }

        // Buscamos la clase del primer elemento que no sea nulo
        Class<?> tipoReferencia = null;
        for (Object elem : datos) {
            if (elem != null) {
                tipoReferencia = elem.getClass();
                break;
            }
        }

        // Si todos los elementos son null, lo consideramos homogéneo
        if (tipoReferencia == null) {
            return true;
        }

        // Comprobamos que todos los demás elementos sean del mismo tipo
        for (Object elem : datos) {
            if (elem != null && !elem.getClass().equals(tipoReferencia)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Comprueba si un elemento pertenece al conjunto.
     */
    public boolean pertenece(Object elemento) {
        if (elemento == null) {
            for (Object elem : this.elementos) {
                if (elem == null) return true;
            }
            return false;
        }

        for (Object elem : this.elementos) {
            if (elemento.equals(elem)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Realiza la unión de este conjunto con otro recibido como parámetro.
     * Retorna un nuevo Conjunto con la unión de ambos elementos.
     */
    public Conjunto union(Conjunto otro) {
        if (otro == null || otro.elementos.length == 0) {
            return new Conjunto(this.elementos);
        }
        if (this.elementos.length == 0) {
            return new Conjunto(otro.elementos);
        }

        // Combinar ambos arrays
        Object[] combinado = new Object[this.elementos.length + otro.elementos.length];
        System.arraycopy(this.elementos, 0, combinado, 0, this.elementos.length);
        System.arraycopy(otro.elementos, 0, combinado, this.elementos.length, otro.elementos.length);

        // El constructor validará la homogeneidad del resultado combinado
        return new Conjunto(combinado);
    }

    // Método de utilidad para ver el contenido del conjunto
    @Override
    public String toString() {
        return Arrays.toString(elementos);
    }
}
