package Practica1;

import java.io.File;

public class Ejercicio_1 {
    static void main(String[] args) throws IllegalAccessException {
        String ruta = args.length > 0 ? args[0] : "src/EjemplosClaseFileStreams";
        File directorio = new File(ruta);

        if (!directorio.exists()) {
            throw new IllegalAccessException("No hay acceso al directorio o no existe");
        } else {
            File[] ficheros = directorio.listFiles();
            if (ficheros != null) {
                for (File fichero : ficheros) {
                    System.out.println(fichero.getName());
                }
            }
        }
    }
}
