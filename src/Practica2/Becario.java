package Practica2;

import java.io.*;
import java.util.Scanner;

public class Becario {
    static Scanner sc = new Scanner(System.in);
    private static final String ARCHIVO = "src/Practica2/datosbeca.bin";
    private String nombre;
    private String sexo;
    private int edad;
    private int sus;
    private String res;
    private double sal;

    public Becario() {

    }
    public Becario(String nombre, String sexo, int edad, int sus, String res, double sal) {
        setNombre(nombre);
        setSexo(sexo);
        setEdad(edad);
        setSus(sus);
        setRes(res);
        setSal(sal);
    }
    public static void main(String[] args) {
        System.out.println("Introduce los datos del becario:");

        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();

        String sexo;
        do {
            System.out.print("Sexo (H/M): ");
            sexo = sc.nextLine().trim().toUpperCase();
        } while (!sexo.equals("H") && !sexo.equals("M"));

        int edad;
        do {
            edad = leerEntero("Edad: ");
            if (edad <= 20 || edad >= 60) {
                System.out.println("La edad debe ser mayor de 20 y menor de 60.");
            }
        } while (edad <= 20 || edad >= 60);

        int sus;
        do {
            sus = leerEntero("Número de suspensos: ");
            if (sus < 0) {
                System.out.println("El número no puede ser negativo.");
            }
        } while (sus < 0);

        String res;
        do {
            System.out.print("¿Residencia familiar? (Si/No): ");
            res = sc.nextLine().trim();
            if (!res.equalsIgnoreCase("Si") && !res.equalsIgnoreCase("No")) {
                System.out.println("Escribe Si o No.");
            }
        } while (!res.equalsIgnoreCase("Si") && !res.equalsIgnoreCase("No"));

        double sal;
        do {
            sal = leerDouble("Ingresos anuales de la familia: ");
            if (sal < 0) {
                System.out.println("Los ingresos no pueden ser negativos.");
            }
        } while (sal < 0);

        Becario becario = new Becario(nombre, sexo, edad, sus, res, sal);
        guardarBecario(becario);
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número entero.");
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String valor = sc.nextLine().trim().replace(',', '.');
                return Double.parseDouble(valor);
            } catch (NumberFormatException e) {
                System.out.println("Introduce un número válido.");
            }
        }
    }

    private static void guardarBecario(Becario becario) {
        try (DataOutputStream dos =
                     new DataOutputStream(new FileOutputStream(ARCHIVO, true))) {

            dos.writeUTF(becario.nombre);
            dos.writeUTF(becario.sexo);
            dos.writeInt(becario.edad);
            dos.writeInt(becario.sus);
            dos.writeUTF(becario.res);
            dos.writeDouble(becario.sal);

            System.out.println("Becario guardado.");

        } catch (IOException e) {
            System.out.println("No se pudo guardar el fichero: " + e.getMessage());
        }
    }

    public String getNombre() {
        return nombre;
    }

    private void setNombre(String nombre) {
        if (nombre.isEmpty()) {
            throw new IllegalArgumentException("No me vale");
        } else {
            this.nombre = nombre;
        }
    }

    public String getSexo() {
        return sexo;
    }

    private void setSexo(String sexo) {
        if (sexo.isEmpty()) {
            throw new IllegalArgumentException("No me vale");
        } else {
            this.sexo = sexo;
        }
    }

    public int getEdad() {
        return edad;
    }

    private void setEdad(int edad) {
        if (edad < 0) {
            throw new IllegalArgumentException("No me vale");
        } else {
            this.edad = edad;
        }
    }

    public int getSus() {
        return sus;
    }

    private void setSus(int sus) {
        if (sus < 0) {
            throw new IllegalArgumentException("No me vale");
        } else {
            this.sus = sus;
        }
    }

    public String getRes() {
        return res;
    }

    private void setRes(String res) {
        if (res.isEmpty()) {
            throw new IllegalArgumentException("No me vale");
        } else {
            this.res = res;
        }
    }

    public double getSal() {
        return sal;
    }

    private void setSal(double sal) {
        if (sal < 0) {
            throw new IllegalArgumentException("No me vale");
        } else {
            this.sal = sal;
        }
    }
}
