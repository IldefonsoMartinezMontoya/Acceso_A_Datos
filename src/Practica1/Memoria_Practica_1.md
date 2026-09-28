# Memoria explicativa: Práctica 1

## Prácticas Clase File y Streams

## Ejercicio 1: contenido de un directorio
Ejercicio_1 toma una ruta desde los argumentos de ejecución. Comprueba la existencia de la ruta y muestra por consola los nombres de sus elementos.

```java
package Practica1;

import java.io.File;

public class Ejercicio_1 {
    static void main(String[] args) throws IllegalAccessException {
        String ruta = args.length > 0 ? args[0] : "src/EjemplosClaseFileStreams";
        File directorio = new File(ruta);

        if (!directorio.exists()) {
            throw new IllegalAccessException("No hay acceso al directorio o no existe");
        } else {
            File[] ficheros = directorio.listFiles();
            if (ficheros != null) {
                for (File fichero : ficheros) {
                    System.out.println(fichero.getName());
                }
            }
        }
    }
}
```

## Ejercicio 2: lectura de texto
Ejercicio_2 abre la ruta recibida como argumento o, si no se proporciona, utiliza FichTexto.txt. Lee el fichero línea a línea con FileReader y BufferedReader e imprime su contenido en consola.
```java
package Practica1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Ejercicio_2 {
    static void main(String[] args) {
        String fichero = args.length > 0 ? args[0] : "FichTexto.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(fichero))){
            String linea;
            while ((linea = br.readLine()) != null) {
                System.out.println(linea);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

```
## Ejercicio 3: formulario y almacenamiento
Ejercicio_3 presenta una ventana Swing con un menú vertical. «Crear Fichero» abre un formulario con campos para nombre, apellidos y ciudad natal. «Terminar» agrega los datos a src/Practica1/Ejercicio_3_texto.txt, vacía los campos e informa del resultado. «Volver» retorna al menú; «Mostrar» abre una ventana con el texto guardado; «Cerrar programa» termina la aplicación. Se utilizan JFrame, JPanel, GridLayout, botones, campos de texto y listeners de acción.
```java
package Practica1;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Ejercicio_3 extends JFrame {
    private static final String ARCHIVO = "src/Practica1/Ejercicio_3_texto.txt";

    public Ejercicio_3() {
        setTitle("Ficheros");
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
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
                bw.write("Nombre: " + escNombre.getText());
                bw.newLine();
                bw.write("Apellidos: " + escApellido.getText());
                bw.newLine();
                bw.write("Ciudad natal: " + escCiudad.getText());
                bw.newLine();
                bw.newLine();
                escNombre.setText("");
                escApellido.setText("");
                escCiudad.setText("");
                JOptionPane.showMessageDialog(this, "Datos guardados.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "No se pudieron guardar los datos: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        mostrar.addActionListener(e -> mostrarFichero());
        setVisible(true);
    }

    private void mostrarFichero() {
        JTextArea texto = new JTextArea();
        texto.setEditable(false);
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) texto.append(linea + "\n");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo leer el fichero: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        JFrame ventana = new JFrame("Contenido del fichero de texto");
        ventana.setSize(400, 300);
        ventana.setLocationRelativeTo(this);
        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        ventana.add(new JScrollPane(texto));
        ventana.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Ejercicio_3::new);
    }
}

```
## Ejercicio 4: escritura y lectura de números
Ejercicio_4 ofrece botones para generar, mostrar y cerrar. Al generar, añade al fichero src/Practica1/Ejercicio_4_texto.txt los números pares desde 0 hasta 100, separados por espacios y en una nueva línea. El botón «Mostrar» presenta el contenido en una ventana. Se utilizan BufferedWriter, FileWriter, BufferedReader y FileReader.
```java
package Practica1;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Random;

public class Ejercicio_4 extends JFrame {
    private static final String ARCHIVO = "src/Practica1/Ejercicio_4_texto.txt";
    public Ejercicio_4() {
        setTitle("Array");
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1));

        JButton crearFichero = createCrearFichero();
        JButton mostrarFichero = new JButton("Mostrar");
        mostrarFichero.addActionListener(e -> {
            JTextArea texto = new JTextArea();
            texto.setEditable(false);
            try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))){
                String Linea;
                while ((Linea = br.readLine()) != null) {
                    texto.append(Linea + "\n");
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            JFrame ventana = new JFrame("Contenido del Fichero");
            ventana.setResizable(false);
            ventana.setSize(400, 300);
            ventana.setLocationRelativeTo(this);
            ventana.add(new JScrollPane(texto));
            ventana.setVisible(true);

        });
        JButton cerrar = new JButton("Cerrar");
        cerrar.addActionListener(e -> System.exit(0));
        add(crearFichero);
        add(mostrarFichero);
        add(cerrar);
        setVisible(true);

    }

    private static JButton createCrearFichero() {
        JButton crearFichero = new JButton("Crear Ficehro");
        crearFichero.addActionListener(e -> {
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARCHIVO, true))){
                    for (int i = 0; i < 102; i+= 2) {
                        bw.write(Integer.toString(i));
                        bw.write(" ");
                    }
                    bw.newLine();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
        });
        return crearFichero;
    }

    static void main() {
        SwingUtilities.invokeLater(Ejercicio_4::new);
    }
}

```
## Ejercicio 5: escritura y transformación de texto
Ejercicio_5 solicita por consola la ruta del fichero y una línea de contenido. Añade esa línea al final del fichero y después lee el fichero completo. Muestra el texto por consola cambiando las letras minúsculas a mayúsculas y las mayúsculas a minúsculas. Conserva espacios, números y signos, y mantiene los saltos entre líneas.
```java
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

```
## Ejercicio 6: suma de números de un fichero
Ejercicio_6 lee el fichero indicado por la constante ARCHIVO. Interpreta cada línea como un número entero, suma los valores y muestra el total por consola. El fichero debe contener un entero por línea.
```java
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

```
## Ficheros de datos
- Ejercicio_3_texto.txt: registros introducidos mediante el formulario del ejercicio 3.
- Ejercicio_4_texto.txt: secuencias de números pares generadas por el ejercicio 4.
- Ejercicio 5: el fichero que se solicita por consola; se crea si no existe y se amplía en cada ejecución.
- Ejercicio 6: el fichero indicado en ARCHIVO, con un número entero en cada línea.

