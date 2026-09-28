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
