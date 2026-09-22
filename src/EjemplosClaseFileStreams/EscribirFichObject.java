package EjemplosClaseFileStreams;

import java.io.*;

public class EscribirFichObject { 
    public static void main(String[] args) throws IOException {
        Persona persona;//defino variable persona 
        
        //declara el fichero 
        File fichero = new File ("FichPersona.dat");
        //crea el flujo de salida 
        FileOutputStream fileout = new FileOutputStream(fichero); 
        //conecta el flujo de bytes al flujo de datos
        ObjectOutputStream dataOS = new ObjectOutputStream(fileout); 

        String nombres[] = {"Ana", "Luis Miguel", "Alicia", "Pedro", 
            "Manuel", "Andrés", "Julio", "Antonio", "Maria Jesús"}; 
        int edades[] = {14,15,13,15,16,12,16,14,13}; 
        
        for (int i=0;i<edades.length; i++) { //recorro los arrays
            persona = new Persona(nombres[i],edades[i]);
            dataOS.writeObject(persona); //escribo la persona en el fichero 
        }
        dataOS.close(); //cerrar stream de salida 
    }
}