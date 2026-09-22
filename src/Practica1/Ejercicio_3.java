package Practica1;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;

public class Ejercicio_3 extends JFrame {
    public Ejercicio_3() {
        setTitle("Ficheros");
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        JPanel botonCrear = new JPanel(new BorderLayout());
        JButton crearFichero = new JButton("Crear Fichero");

        JPanel datosFichero = new JPanel(new GridLayout(4, 4));
        JLabel nombre = new JLabel("Nombre: ");
        nombre.setHorizontalAlignment(JLabel.CENTER);
        JTextField escNombre = new JTextField();
        datosFichero.add(nombre);
        datosFichero.add(escNombre);
        JLabel apellido = new JLabel("Apellidos: ");
        apellido.setHorizontalAlignment(JLabel.CENTER);
        JTextField escApellido = new JTextField();
        datosFichero.add(apellido);
        datosFichero.add(escApellido);
        JLabel ciudad = new JLabel("Ciudad Natal");
        ciudad.setHorizontalAlignment(JLabel.CENTER);
        JTextField escCiudad = new JTextField();
        datosFichero.add(ciudad);
        datosFichero.add(escCiudad);
        JButton terminar = new JButton("Terminar");
        datosFichero.add(terminar);
        terminar.addActionListener(e -> {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/Practica1/Ejercicio_3_texto.txt", true))){
                bw.write("Nombre: " + escNombre.getText());
                bw.newLine();
                bw.write("Apellidos: " + escApellido.getText());
                bw.newLine();
                bw.write("Ciudad Natal: " + escCiudad.getText());
                bw.newLine();
                escNombre.setText("");
                escApellido.setText("");
                escCiudad.setText("");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        JButton cerrar = new JButton("Cerrar");
        datosFichero.add(cerrar);
        cerrar.addActionListener(e -> {
            datosFichero.setVisible(false);
            botonCrear.setVisible(true);
            revalidate();
            repaint();
        });
        botonCrear.add(crearFichero);

        JPanel mostrarFichero = new JPanel(new BorderLayout());
        JButton mostrar = getJButton();

        crearFichero.addActionListener(e -> {
            datosFichero.setVisible(true);
            botonCrear.setVisible(false);
            mostrarFichero.setVisible(false);
            revalidate();
            repaint();
        });
        mostrarFichero.add(mostrar);
        add(botonCrear);
        botonCrear.setVisible(true);
        add(mostrarFichero);
        mostrarFichero.setVisible(true);
        datosFichero.setVisible(false);
        add(datosFichero);
        setVisible(true);
    }

    private static JButton getJButton() {
        JButton mostrar = new JButton("Mostrar");
        mostrar.addActionListener(e -> {
            JFrame nuevaVentana = new JFrame("Contenido Fichero de Texto");
            nuevaVentana.setSize(400, 300);
            nuevaVentana.setLocationRelativeTo(null);
            nuevaVentana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JTextArea mostrarTexto = new JTextArea();
            try (BufferedReader br = new BufferedReader(new FileReader("src/Practica1/Ejercicio_3_texto.txt"))){
                String linea;
                while ((linea = br.readLine()) != null) {
                    mostrarTexto.append(linea + "\n");
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            nuevaVentana.add(mostrarTexto);
            nuevaVentana.setVisible(true);
        });
        return mostrar;
    }

    static void main() {
        SwingUtilities.invokeLater(Ejercicio_3::new);
    }
}
