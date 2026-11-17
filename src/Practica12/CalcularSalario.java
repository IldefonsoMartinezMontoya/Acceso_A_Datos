package Practica12;

import java.sql.*;
import java.util.Scanner;

public class CalcularSalario {
    static final String URL = "jdbc:mysql://localhost:3306/practica";
    static final String USER = "practica";
    static final String PASS = "practica";
    static Scanner sc = new Scanner(System.in);

    public static void calcular_Salario(int dept_no) {
        String sql = "select max(salario) from empleados where dept_no = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
        PreparedStatement ps = con.prepareStatement(sql)){
            ps.setInt(1, dept_no);
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()) {
                    System.out.println("Salario máximo del del departamento " + dept_no + ": " + rs.getDouble("max(salario)"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("Introduce un numero de departamento");
        int dept_no = sc.nextInt();
        calcular_Salario(dept_no);
    }
}
