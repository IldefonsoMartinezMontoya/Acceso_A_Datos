package Practica7;

import java.sql.*;

public class Ejercicio_5_MayorMedia {
    static final String URL = "jdbc:postgresql://localhost:5432/escuela";
    static final String USER = "postgres";
    static final String PASS = "oretania";
    static String sql;

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)){
            sql = "select nombre, round(avg(nota), 2) as nota from asignaturas join notas on asignaturas.cod = notas.cod group by nombre " +
                    "having avg(nota) > (select avg(nota) from notas) order by nota desc";
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)){
                System.out.println("Asginatura y nota");
                while (rs.next()) {
                    System.out.printf("%-30s",
                            rs.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
