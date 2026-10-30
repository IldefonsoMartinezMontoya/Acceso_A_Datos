package Practica7;

import java.sql.*;

public class Ejercicio_9_MarzoJunio {
    static final String URL = "jdbc:mysql://localhost:3306/practica";
    static final String USER = "practica";
    static final String PASS = "practica";
    static String sql;

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)){
            sql = "select apellido, fecha_alt, dnombre from empleados join departamentos on empleados.dept_no = departamentos.dept_no where fecha_alt between '2023-05-01' and '2023-06-30'";
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)){
                while (rs.next()) {
                    System.out.println(rs.getString("apellido") + " " + rs.getString("fecha_alt") + " " + rs.getString("dnombre"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
