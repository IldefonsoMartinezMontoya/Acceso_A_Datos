package Practica7;

import java.sql.*;

public class Ejercicio_3_NotaMedia {
    static final String URL = "jdbc:postgresql://localhost:5432/escuela";
    static final String USER = "postgres";
    static final String PASS = "oretania";
    static String sql;

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)){
            sql = "select apenom, count(notas) as numAsig, round(avg(nota), 2) as media from alumnos left join notas on " +
                    "alumnos.dni = notas.dni group by alumnos.dni, alumnos.apenom order by media desc nulls last";
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)) {
                System.out.println("Nombre y Media:");
                while (rs.next()) {
                    System.out.printf("%-30s | %2d asignaturas | %.2f%n",
                            rs.getString("apenom"),
                            rs.getInt("numAsig"),
                            rs.getDouble("media"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
