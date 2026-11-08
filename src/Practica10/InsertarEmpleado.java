package Practica10;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InsertarEmpleado {
    static final String URL = "jdbc:mysql://localhost:3306/practica";
    static final String USER = "practica";
    static final String PASS = "practica";

    public static void main(String[] args) {
        if (args.length != 7) {
            System.out.println("Uso: java Practica10.InsertarEmpleado EMP_NO APELLIDO OFICIO DIR SALARIO COMISION DEPT_NO");
            System.out.println("Use NULL para DIR o COMISION cuando no tengan valor.");
            return;
        }

        try {
            int empNo = Integer.parseInt(args[0]);
            String apellido = args[1].trim();
            String oficio = args[2].trim();
            Integer director = valorOpcional(args[3]);
            BigDecimal salario = new BigDecimal(args[4]);
            BigDecimal comision = decimalOpcional(args[5]);
            int deptNo = Integer.parseInt(args[6]);

            if (apellido.isEmpty()) { System.out.println("No insertado: el apellido no puede estar vacío."); return; }
            if (oficio.isEmpty()) { System.out.println("No insertado: el oficio no puede estar vacío."); return; }
            if (salario.compareTo(BigDecimal.ZERO) <= 0) { System.out.println("No insertado: el salario debe ser mayor que 0."); return; }

            try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
                if (!existe(con, "SELECT 1 FROM departamentos WHERE dept_no = ?", deptNo)) {
                    System.out.println("No insertado: el departamento " + deptNo + " no existe."); return;
                }
                if (existe(con, "SELECT 1 FROM empleados WHERE emp_no = ?", empNo)) {
                    System.out.println("No insertado: el número de empleado " + empNo + " ya existe."); return;
                }
                if (director != null && !existe(con, "SELECT 1 FROM empleados WHERE emp_no = ?", director)) {
                    System.out.println("No insertado: el director " + director + " no existe."); return;
                }

                String sql = "INSERT INTO empleados (emp_no, apellido, oficio, dir, salario, comision, dept_no, fecha_alt) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_DATE)";
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setInt(1, empNo);
                    ps.setString(2, apellido);
                    ps.setString(3, oficio);
                    if (director == null) ps.setNull(4, java.sql.Types.INTEGER); else ps.setInt(4, director);
                    ps.setBigDecimal(5, salario);
                    if (comision == null) ps.setNull(6, java.sql.Types.DECIMAL); else ps.setBigDecimal(6, comision);
                    ps.setInt(7, deptNo);
                    if (ps.executeUpdate() == 1) System.out.println("Empleado " + empNo + " insertado correctamente.");
                    else System.out.println("No se insertó ningún empleado.");
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("No insertado: EMP_NO, DIR, SALARIO, COMISION y DEPT_NO deben tener formato numérico (DIR/COMISION admiten NULL).");
        } catch (SQLException e) {
            System.out.println("No insertado: error de base de datos: " + e.getMessage());
        }
    }

    private static boolean existe(Connection con, String sql, int valor) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, valor);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    private static Integer valorOpcional(String valor) {
        return valor.equalsIgnoreCase("NULL") || valor.equals("-") ? null : Integer.valueOf(valor);
    }

    private static BigDecimal decimalOpcional(String valor) {
        return valor.equalsIgnoreCase("NULL") || valor.equals("-") ? null : new BigDecimal(valor);
    }
}
