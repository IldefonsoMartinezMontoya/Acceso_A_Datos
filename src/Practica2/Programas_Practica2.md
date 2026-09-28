# Programas de Practica2

Resumen de los programas Java incluidos en este directorio. El código se reproduce tal como está actualmente en los archivos.

## Ejercicio_1: formulario Swing y fichero binario de personas

Abre una ventana con un menú para introducir personas y mostrar las guardadas. Al pulsar “Terminar”, añade nombre, apellidos y ciudad al fichero persona.dat mediante DataOutputStream.writeUTF. La opción “Mostrar” lee esos tres textos por registro hasta llegar al final del fichero y los presenta en otra ventana.

```java
package Practica2;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Ejercicio_1 extends JFrame {
    private static final String ARCHIVO = "src/Practica2/persona.dat";

    public Ejercicio_1() {
        setTitle("DAT");
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        CardLayout tarjetas = new CardLayout();
        JPanel principal = new JPanel(tarjetas);
        JButton crearFichero = new JButton("Crear Fichero");
        JButton mostrar = new JButton("Mostrar");
        JButton cerrarPrograma = new JButton("Cerrar programa");

        JPanel menu = new JPanel(new GridLayout(3, 1, 0, 10));
        menu.setBorder(BorderFactory.createEmptyBorder(40, 180, 40, 180));
        menu.add(crearFichero);
        menu.add(mostrar);
        menu.add(cerrarPrograma);

        JPanel datosFichero = new JPanel(new GridLayout(4, 2, 10, 10));
        datosFichero.setBorder(BorderFactory.createEmptyBorder(100, 120, 100, 120));
        JTextField escNombre = new JTextField();
        JTextField escApellido = new JTextField();
        JTextField escCiudad = new JTextField();
        datosFichero.add(new JLabel("Nombre:", JLabel.CENTER));
        datosFichero.add(escNombre);
        datosFichero.add(new JLabel("Apellidos:", JLabel.CENTER));
        datosFichero.add(escApellido);
        datosFichero.add(new JLabel("Ciudad natal:", JLabel.CENTER));
        datosFichero.add(escCiudad);
        JButton terminar = new JButton("Terminar");
        JButton volver = new JButton("Volver");
        datosFichero.add(terminar);
        datosFichero.add(volver);

        principal.add(menu, "menu");
        principal.add(datosFichero, "datos");
        add(principal);
        crearFichero.addActionListener(e -> tarjetas.show(principal, "datos"));
        volver.addActionListener(e -> tarjetas.show(principal, "menu"));
        cerrarPrograma.addActionListener(e -> System.exit(0));

        terminar.addActionListener(e -> {
            try (DataOutputStream salida = new DataOutputStream(new FileOutputStream(ARCHIVO, true))) {

                salida.writeUTF(escNombre.getText());
                salida.writeUTF(escApellido.getText());
                salida.writeUTF(escCiudad.getText());

                escNombre.setText("");
                escApellido.setText("");
                escCiudad.setText("");

                JOptionPane.showMessageDialog(this, "Datos guardados.");

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "No se pudieron guardar los datos: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        mostrar.addActionListener(e -> mostrarFichero());
        setVisible(true);
    }

    private void mostrarFichero() {
        JTextArea texto = new JTextArea();
        texto.setEditable(false);
        try (DataInputStream entrada = new DataInputStream(new FileInputStream(ARCHIVO))) {
            while (true) {
                String nombre = entrada.readUTF();
                String apellido = entrada.readUTF();
                String ciudad = entrada.readUTF();
                texto.append("Nombre: " + nombre + "\n");
                texto.append("Apellidos: " + apellido + "\n");
                texto.append("Ciudad natal: " + ciudad + "\n\n");
            }
        } catch (EOFException _) {
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "No se pudo leer el fichero: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFrame ventana = new JFrame("Contenido del fichero binario");
        ventana.setSize(400, 300);
        ventana.setLocationRelativeTo(this);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.add(new JScrollPane(texto));
        ventana.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Ejercicio_1::new);
    }
}
```

## Becario: captura por consola y guardado de un objeto

Solicita por consola los datos de un becario y valida sexo, edad, suspensos, residencia e ingresos. Con las respuestas construye un objeto Becario y lo añade a datosbeca.bin. Escribe nombre, sexo, residencia e ingresos como UTF, y edad y suspensos como enteros. El salario se guarda como double.

```java
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
```
