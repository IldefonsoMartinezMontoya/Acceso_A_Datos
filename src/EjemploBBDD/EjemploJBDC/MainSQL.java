package EjemploBBDD.EjemploJBDC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainSQL {
    public static void main(String[] args) {
        // Ruta al archivo de la base de datos local SQLite
        String url = "jdbc:sqlite:prueba.sqlite";

        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                System.out.println("¡Conexión establecida con éxito a SQLite!");

                // Ejemplo de consulta rápida
                String sql = "SELECT * FROM Artist"; // Cambia 'Artist' por una tabla de tu BD
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)) {

                    while (rs.next()) {
                        // Cambia "Name" por el nombre de la columna que quieras ver
                        System.out.println(rs.getInt("ArtistId") + " - " + rs.getString("Name"));
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al conectar o consultar la base de datos: " + e.getMessage());
        }
    }
}