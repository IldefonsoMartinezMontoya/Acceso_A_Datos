package Practica12;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

public class CalcularSalario {
    static final String URL = "jdbc:mysql://localhost:3306/practica";
    static final String USER = "practica";
    static final String PASS = "practica";
    static Scanner sc = new Scanner(System.in);

    private static String crear_funcion = """
            create function calcular_salario maximo(pdept_no int)
            returns decimal(10, 2)
            reads sql data
            begin
                declare existe int default 0;
                declare maximo decimal(10, 2) default null;
                
                select count(*) int existe from departamentos where dept_no = pdept_no;
                
                if existe = 0 then return -1.00;
                end if;
                
                select max(salario) into maximo from empleados where dept_no = pdept_no;
                
                return coalesce(maximo, 0.00);
            end
            """;
    private static void crearFuncion(Connection con) throws SQLException {
        try (Statement st = con.createStatement()){
            st.execute("DROP FUNCTION IF EXISTS calcular_salario_maximo");
            st.execute(crear_funcion);
        }
    }
    public static void calcular_Salario(int dept_no) {
        String sql = "SELECT calcular_salario_maximo(?)";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            crearFuncion(con);
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, dept_no);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        BigDecimal salario = rs.getBigDecimal(1);

                        if (salario.compareTo(BigDecimal.valueOf(-1)) == 0) {
                            System.out.println("El departamento no existe.");
                        } else if (salario.compareTo(BigDecimal.ZERO) == 0) {
                            System.out.println(
                                    "Salario máximo: 0.00 (departamento sin empleados)"
                            );
                        } else {
                            System.out.println("Salario máximo: " + salario + " €");
                        }
                    }
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
