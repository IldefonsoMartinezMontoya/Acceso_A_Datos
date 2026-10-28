package Practica7;

import java.sql.*;

public class Ejercicio_4_NotaMasAlta {
    static final String URL = "jdbc:postgresql://localhost:5432/escuela";
    static final String USER = "postgres";
    static final String PASS = "oretania";
    static String sql;

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)){
            sql = "select apenom, nombre, nota from alumnos join notas on alumnos.dni = notas.dni join asignaturas " +
                    "on notas.cod = asignaturas.cod  where nota = (select max(nota) from notas) order by apenom, alumnos.dni";
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)){
                System.out.println("Alumnos con nota más alta:");
                while (rs.next()) {
                    System.out.printf("%-30s | %-30s | %d%n",
                            rs.getString("apenom"),
                            rs.getString("nombre"),
                            rs.getInt("nota"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
