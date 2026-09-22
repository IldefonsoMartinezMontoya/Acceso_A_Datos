package EjemplosClaseFileStreams;

import java.io.*;

public class OperacionesFichAleatorio {
    public static void main(String[] args) throws IOException {
        File fichero= new File("AleatorioEmple.dat"); 
        //declara el fichero de acceso aleatorio 
        RandomAccessFile file= new RandomAccessFile(fichero, "rw"); 
        
        //Insertamos un nuevo registro------------------------------------------------
        StringBuffer buffer= null; //buffer para almacenar apellid
        String apellido = "GONZALEZ"; //apellido a insertar
        Double salario= 1230.87; //salario 
        int id = 8; //id del empleado
        int dep = 10; //dep del empleado 

        long posicion = (id-1) * 36; //calculamos la posición 

        file.seek(posicion); //nos posicionamos
        file.writeInt(id); //se escribe id 
        buffer = new StringBuffer(apellido);
        buffer.setLength(10); //10 caracteres para el apellido 
        file.writeChars(buffer.toString());//insertar apellido
        file.writeInt(dep); //insertar departamento
        file.writeDouble(salario); //insertar salario

        //Modificamos un registro------------------------------------------------
        int registro= 4; //id a modificar 
        posicion = (registro -1 ) * 36; //calculo la posición 
        posicion= posicion + 4 + 20; //sumo el tamaño de ID+ apellido
        file.seek(posicion); //nos posicionamos
        file.writeInt(40); //modifico departamento 
        file.writeDouble(4000.87); //modifico salario 

        file.close(); //cerrar fichero 
    }
}