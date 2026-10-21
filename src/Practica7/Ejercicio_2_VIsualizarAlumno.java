package Practica7;

import java.sql.*;

public class Ejercicio_2_VIsualizarAlumno {
    static final String URL  = "jdbc:postgresql://localhost:5432/escuela";
    static final String USER = "postgres";
    static final String PASS = "oretania";
    static String sql;
    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            sql = "SELECT apenom, asignatura, nota FROM alumnos JOIN notas ON notas.dni = alumnos.dni JOIN asignaturas " +
                    "ON asignaturas.cod = notas.cod ORDER BY alumnos.apenom ASC, alumnos.dni ASC, notas.nota DESC";
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                System.out.println("ALUMNOS:");
                while (rs.next()) {
                    System.out.printf("  %-10s %-25s %s%n",
                            rs.getString("apenom"),
                            rs.getString("asignatura"),
                            rs.getString("nota"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error SQL [" + e.getSQLState() + "]: " + e.getMessage());
        }
    }
}
