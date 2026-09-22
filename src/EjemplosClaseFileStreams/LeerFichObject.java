package EjemplosClaseFileStreams;

import java.io.*;

public class LeerFichObject{
    public static void main(String[] args) throws IOException, ClassNotFoundException{ 
        Persona persona; 
        //defino la variable persona 
        File fichero= new File("FichPersona.dat"); 
        //crea el flujo de entrada 
        FileInputStream filein = new FileInputStream(fichero); 
        //conecta el flujo de bytes al flujo de datos
        ObjectInputStream dataIS = new ObjectInputStream(filein); 
        
        try {
            while (true) { //lectura del éó.ci:ero
                persona= (Persona) dataIS.readObject(); //leer una Persona
                System.out.printf("Nombre: %s, edad: %d %n", persona.getNombre() ,persona.getEdad()); 
            }
        }catch (EOFException eo){
            System.out.println("FIN DE LECTURA");
        }
        dataIS.close(); //cerrar stream de entrada
    }
}