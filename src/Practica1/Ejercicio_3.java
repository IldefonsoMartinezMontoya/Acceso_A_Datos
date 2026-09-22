package Practica1;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Ejercicio_3 extends JFrame {
    static Scanner sc = new Scanner(System.in);
    public Ejercicio_3() {
        setTitle("Ficheros");
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 1));

        JPanel botonCrear = new JPanel(new BorderLayout());
        JButton crearFichero = new JButton("Crear Fichero");

        JPanel datosFichero = new JPanel(new GridLayout(4, 3));
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
            datosFichero.setVisible(false);
            botonCrear.setVisible(true);
            revalidate();
            repaint();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/Practica1/Ejercicio_3_texto.txt"))){
                bw.write("Nombre: " + escNombre.getText());
                bw.newLine();
                bw.write("Apellidos: " + escApellido.getText());
                bw.newLine();
                bw.write("Ciudad Natal: " + escCiudad.getText());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        crearFichero.addActionListener(e -> {
            datosFichero.setVisible(true);
            botonCrear.setVisible(false);
            revalidate();
            repaint();
        });
        botonCrear.add(crearFichero);

        add(botonCrear);
        botonCrear.setVisible(true);
        add(datosFichero);
        datosFichero.setVisible(false);
        setVisible(true);
    }

    static void main() {
        SwingUtilities.invokeLater(Ejercicio_3::new);
    }
}
