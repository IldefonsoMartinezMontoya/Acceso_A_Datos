package Practica7;

import java.sql.*;

public class Ejercicio_8_VisualizarDept {
    static final String URL = "jdbc:mysql://localhost:3306/practica";
    static final String USER = "practica";
    static final String PASS = "practica";
    static String sql;

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)){
            sql = "select dnombre, count(emp_no) as numero_empleados, coalesce(round(avg(salario), 2), 0.00) as salario_medio from departamentos left join empleados on departamentos.dept_no = empleados.dept_no group by departamentos.dept_no, departamentos.dnombre order by dnombre";
            try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)){
                while (rs.next()) {
                    System.out.println(rs.getString("dnombre") + " " + rs.getInt("numero_empleados") + " " + rs.getDouble("salario_medio"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
