package Practica7;

import java.sql.*;

public class Ejercicio_1_BusquedaMadrid {
    // Datos de conexión de la base de referencia del curso (docker-compose)
    static final String URL  = "jdbc:postgresql://localhost:5432/escuela";
    static final String USER = "postgres";
    static final String PASS = "oretania";
    static String sql;
    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {

            sql = "SELECT dni, apenom, pobla FROM alumnos where poblacion = 'Madrid' ORDER BY apenom";
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                System.out.println("ALUMNOS:");
                while (rs.next()) {
                    System.out.printf("  %-10s %-25s %s%n",
                            rs.getString("dni"),
                            rs.getString("apenom"),
                            rs.getString("telef"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error SQL [" + e.getSQLState() + "]: " + e.getMessage());
        }
    }
}
