package Practica12;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Scanner;

public class CalcularSalario {
    static final String URL = "jdbc:mysql://localhost:3306/practica";
    static final String USER = "practica";
    static final String PASS = "practica";
    static Scanner sc = new Scanner(System.in);

    private static String crear_funcion = """
            create function calcular_salario_maximo(pdept_no int) returns decimal(10, 2)
            reads sql data
            begin
                declare existe int default 0;
                declare maximo decimal(10, 2) default null;
                
                select count(*) into existe from departamentos where dept_no = pdept_no;
                
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
    private static String crear_procedimiento = """
        CREATE PROCEDURE calcular_estadisticas_departamento(
            IN pdept_no INT,
            OUT pnum_empleados INT,
            OUT psalario_medio DECIMAL(10,2)
        )
        READS SQL DATA
        BEGIN
            DECLARE existe INT DEFAULT 0;

            SELECT COUNT(*) INTO existe
            FROM departamentos
            WHERE dept_no = pdept_no;

            IF existe = 0 THEN
                SET pnum_empleados = -1;
                SET psalario_medio = -1.00;
            ELSE
                SELECT COUNT(*), COALESCE(AVG(salario), 0.00)
                INTO pnum_empleados, psalario_medio
                FROM empleados
                WHERE dept_no = pdept_no;
            END IF;
        END
        """;

    private static void crearProcedimiento(Connection con) throws SQLException {
        try (Statement st = con.createStatement()) {
            st.execute("DROP PROCEDURE IF EXISTS calcular_estadisticas_departamento");
            st.execute(crear_procedimiento);
        }
    }
    public static void calcular_Salario(int dept_no) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            CallableStatement cs = con.prepareCall("{? = call calcular_salario_maximo(?)}");
            cs.registerOutParameter(1, Types.DECIMAL);
            CallableStatement cs2 = con.prepareCall("{call calcular_salario_departamento(?, ?, ?)}");
            cs2.registerOutParameter(2, Types.INTEGER);
            cs2.registerOutParameter(3, Types.DECIMAL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL,USER, PASS)){
            crearFuncion(con);
            crearProcedimiento(con);
            System.out.print("Introduce un numerín: ");
            int dept_no = sc.nextInt();
            calcular_Salario(dept_no);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
