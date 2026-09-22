package EjemplosClaseFileStreams;

import java.io.*;

//Leemos línea a línea un fichero de texto.
public class LeerFichTextoBuf {
    public static void main(String[] args) {
        try{
            BufferedReader fichero= new BufferedReader( 
                new FileReader ("FichTexto.txt")) ;
            String linea;
            while((linea = fichero.readLine()) !=null)
                System.out.println(linea); 
            fichero.close(); 
        } 
        catch (FileNotFoundException fn) {
            System.out.println("No se encuentra el fichero");}
        catch (IOException io) { 
            System.out.println("Error de E/S");}
    }
} 