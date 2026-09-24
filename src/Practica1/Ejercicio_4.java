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
