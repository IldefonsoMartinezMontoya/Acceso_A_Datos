package EjemplosClaseFileStreams;

import java.io.*;
public class VerInf { 
    static void main(String[] args) {
        System.out.println("INFORMACIÓN SOBRE EL FICHERO:");
        String directorio = ".";
        File f = new File(directorio,"Verinf.java");
        if (f.exists ()){
            System.out.println("Nombre del fichero: " + f.getName());
            System.out.println("Ruta: " + f.getPath());
            System.out.println("Ruta absoluta: " + f.getAbsolutePath());
            System.out.println("Se puede leer: " + f.canRead());
            System.out.println("Se puede escribir: " + f.canWrite ());
            System.out.println("Tamaño: " + f.length());
            System.out.println("Es un directorio: " + f.isDirectory());
            System.out.println("Es un fichero: " + f.isFile());
            System.out.println ("Nombre del directorio padre: " + f.getParent());
        }
    }
}