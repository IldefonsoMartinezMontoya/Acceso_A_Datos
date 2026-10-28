package Practica7;

import java.sql.*;

public class Ejercicio_6_Practica {
    static final String URL = "jdbc:mysql://localhost:3306/practica";
    static final String USER = "practica";
    static final String PASS = "practica";
    static String sql;

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)){
            sql = "select nombre, apellido, oficio, salario from empleados where dept_no = 1 order by apellido";
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)){
                System.out.println("Empleados:");
                while (rs.next()) {
                    System.out.println(rs.getString("nombre") + " " + rs.getString("apellido") +
                            " " + rs.getString("oficio") + " " + rs.getDouble("salario"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
