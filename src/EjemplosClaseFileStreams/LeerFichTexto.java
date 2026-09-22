package EjemplosClaseFileStreams;

import java.io.*;

public class LeerFichTexto {
    static void main(String[] args) throws IOException{
        //Declarar fichero
        File fichero = new File ("NUEVODIR/FICHERO3.TXT");
        
        //crear el flujo de entrada hacia el fichero
        FileReader fic = new FileReader(fichero);
        int i;
        while((i= fic.read()) != -1) //se va leyendo un caracter
            System.out.print((char)i); //convierte el entero en caracter
        fic.close(); //cerramos el fichero

        //para leer de 20 en 20:
        //char b[] = new char[20];
        //while ((i=fic.read(b))!= -1) System.out.println(b);
    } 
}