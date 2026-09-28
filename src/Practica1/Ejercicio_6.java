package Practica1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Ejercicio_6 {
    private static final String ARCHIVO = "C:\\\\Users\\\\estudiante\\\\Desktop\\\\Numeros.txt";
    static void main() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))){
            String linea;
            int total = 0;
            while ((linea = br.readLine()) != null) {
                total += Integer.parseInt(linea);
            }
            System.out.println(total);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
