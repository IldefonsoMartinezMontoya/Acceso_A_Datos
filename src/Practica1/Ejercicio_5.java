package Practica1;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Ejercicio_5 {
    static void main() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Introduce el fichero(ruta y nombre):");
        String fichero = sc.nextLine();
        System.out.println("Introduce el contenido: ");
        String contenido = sc.nextLine();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fichero, true))){
            bw.write(contenido);
            bw.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Contenido del ficehro:");
        try (BufferedReader br = new BufferedReader(new FileReader(fichero))){
            String linea;
            while ((linea = br.readLine()) != null) {
                for (int i = 0; i < linea.length(); i++) {
                    char a = linea.charAt(i);
                    if (Character.isLowerCase(a)) {
                        System.out.print(Character.toUpperCase(a));
                    } else if (Character.isUpperCase(a)) {
                        System.out.print(Character.toLowerCase(a));
                    } else {
                        System.out.print(a);
                    }
                }
                System.out.println();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
