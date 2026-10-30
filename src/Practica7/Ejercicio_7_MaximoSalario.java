package Practica7;

import java.sql.*;

public class Ejercicio_7_MaximoSalario {
    static final String URL = "jdbc:mysql://localhost:3306/practica";
    static final String USER = "practica";
    static final String PASS = "practica";
    static String sql;

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)){
            sql = "select apellido, salario, dnombre from empleados join departamentos on empleados.dept_no = departamentos.dept_no where salario = (select max(salario) from empleados) order by apellido";
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql)){
                while (rs.next()) {
                    System.out.println(rs.getString("apellido") + " " + rs.getDouble("salario") + " " + rs.getString("dnombre"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
