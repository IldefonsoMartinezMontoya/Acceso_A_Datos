package Practica11;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class PreparedStatment {
    static final String URL = "jdbc:mysql://localhost:3306/practica";
    static final String USER = "practica";
    static final String PASS = "practica";
    static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            mostrarMenu();
            try {
                int opcion = Integer.parseInt(leerTexto("Elige opción: "));
                if (opcion == 7) {
                    System.out.println("Programa finalizado.");
                    break;
                }
                if (opcion < 1 || opcion > 6) {
                    System.out.println("Opción no válida. Elige entre 1 y 7.");
                    continue;
                }

                try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
                    switch (opcion) {
                        case 1:
                            contratarEmpleado(con);
                            break;
                        case 2:
                            analizarSalariosPorDepartamento(con);
                            break;
                        case 3:
                            mostrarRanking(con);
                            break;
                        case 4:
                            incrementarSalarios(con);
                            break;
                        case 5:
                            buscarEmpleados(con);
                            break;
                        case 6:
                            eliminarEmpleado(con);
                            break;
                        default:
                            System.out.println("Opción no válida.");
                    }
                }
            } catch (Exception e) {
                System.out.println("Error en la operación: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private static void mostrarMenu() {
        System.out.println("===== MENÚ DE EMPLEADOS =====");
        System.out.println("1. Contratar nuevo empleado");
        System.out.println("2. Análisis salarial por departamento");
        System.out.println("3. Ranking de empleados mejor pagados");
        System.out.println("4. Incrementar salarios de un departamento");
        System.out.println("5. Búsqueda por criterios múltiples");
        System.out.println("6. Eliminar empleado con confirmación");
        System.out.println("7. Salir");
    }

    private static void contratarEmpleado(Connection con) throws SQLException {
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String oficio = leerTexto("Oficio: ");
        BigDecimal salario = leerImporte("Salario: ");
        int deptNo = leerEntero("Número de departamento: ");

        if (!existeDepartamento(con, deptNo)) {
            System.out.println("El departamento " + deptNo + " no existe.");
            mostrarDepartamentos(con);
            return;
        }

        String sql = "INSERT INTO empleados " +
                "(nombre, apellido, oficio, fecha_alt, salario, dept_no) " +
                "VALUES (?, ?, ?, CURDATE(), ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, oficio);
            ps.setBigDecimal(4, salario);
            ps.setInt(5, deptNo);

            int insertados = ps.executeUpdate();
            if (insertados > 0) {
                try (ResultSet claves = ps.getGeneratedKeys()) {
                    if (claves.next()) {
                        System.out.println("Empleado contratado. Número asignado: " + claves.getInt(1));
                        System.out.println("Salario: " + formatoDinero(salario));
                    }
                }
            }
        }
    }

    private static void analizarSalariosPorDepartamento(Connection con) throws SQLException {
        String sql = "SELECT d.dnombre, d.loc, COUNT(e.emp_no) AS total_empleados, " +
                "COALESCE(MAX(e.salario), 0) AS salario_maximo, " +
                "COALESCE(MIN(e.salario), 0) AS salario_minimo, " +
                "COALESCE(AVG(e.salario), 0) AS salario_medio, " +
                "COALESCE(SUM(e.salario), 0) AS masa_salarial, " +
                "COALESCE(ROUND(100.0 * SUM(e.salario) / " +
                "NULLIF((SELECT SUM(salario) FROM empleados), 0), 2), 0) AS porcentaje " +
                "FROM departamentos d LEFT JOIN empleados e ON d.dept_no = e.dept_no " +
                "GROUP BY d.dept_no, d.dnombre, d.loc " +
                "ORDER BY masa_salarial DESC";

        System.out.printf("%-20s %-15s %8s %18s %18s %18s %18s %10s%n",
                "Departamento", "Localidad", "N.º emp.", "Máximo", "Mínimo",
                "Media", "Masa salarial", "% total");

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                System.out.printf("%-20s %-15s %8d %18s %18s %18s %18s %9.2f%%%n",
                        rs.getString("dnombre"),
                        rs.getString("loc"),
                        rs.getInt("total_empleados"),
                        formatoDinero(rs.getBigDecimal("salario_maximo")),
                        formatoDinero(rs.getBigDecimal("salario_minimo")),
                        formatoDinero(rs.getBigDecimal("salario_medio")),
                        formatoDinero(rs.getBigDecimal("masa_salarial")),
                        rs.getDouble("porcentaje"));
            }
        }

        String totalesSql = "SELECT COUNT(*) AS empleados, COALESCE(MAX(salario), 0) AS maximo, " +
                "COALESCE(MIN(salario), 0) AS minimo, COALESCE(AVG(salario), 0) AS media, " +
                "COALESCE(SUM(salario), 0) AS masa FROM empleados";
        try (PreparedStatement ps = con.prepareStatement(totalesSql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                System.out.printf("%-20s %-15s %8d %18s %18s %18s %18s %9.2f%%%n",
                        "TOTALES", "-",
                        rs.getInt("empleados"),
                        formatoDinero(rs.getBigDecimal("maximo")),
                        formatoDinero(rs.getBigDecimal("minimo")),
                        formatoDinero(rs.getBigDecimal("media")),
                        formatoDinero(rs.getBigDecimal("masa")),
                        rs.getBigDecimal("masa").compareTo(BigDecimal.ZERO) == 0 ? 0.0 : 100.0);
            }
        }
    }

    private static void mostrarRanking(Connection con) throws SQLException {
        String sql = "SELECT ROW_NUMBER() OVER (ORDER BY " +
                "(e.salario + COALESCE(e.comision, 0)) DESC, e.emp_no) AS posicion, " +
                "e.apellido, e.oficio, d.dnombre, " +
                "(e.salario + COALESCE(e.comision, 0)) AS salario_total " +
                "FROM empleados e JOIN departamentos d ON e.dept_no = d.dept_no " +
                "ORDER BY salario_total DESC, e.emp_no LIMIT 10";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.printf("%-6s %-18s %-18s %-22s %18s%n",
                    "Puesto", "Apellido", "Oficio", "Departamento", "Salario total");
            while (rs.next()) {
                int posicion = rs.getInt("posicion");
                String marca = posicion <= 3 ? "*" : "";
                System.out.printf("%-6s %-18s %-18s %-22s %18s%n",
                        posicion + marca,
                        rs.getString("apellido"),
                        rs.getString("oficio"),
                        rs.getString("dnombre"),
                        formatoDinero(rs.getBigDecimal("salario_total")));
            }
        }
    }

    private static void incrementarSalarios(Connection con) throws SQLException {
        int deptNo = leerEntero("Número de departamento: ");
        BigDecimal porcentaje = leerImporte("Porcentaje de incremento: ");
        if (porcentaje.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El porcentaje no puede ser negativo.");
        }

        String resumenSql = "SELECT d.dnombre, COUNT(e.emp_no) AS empleados, " +
                "COALESCE(SUM(e.salario), 0) AS masa " +
                "FROM departamentos d LEFT JOIN empleados e ON d.dept_no = e.dept_no " +
                "WHERE d.dept_no = ? GROUP BY d.dept_no, d.dnombre";
        String nombre;
        int empleados;
        BigDecimal masaAntes;
        try (PreparedStatement ps = con.prepareStatement(resumenSql)) {
            ps.setInt(1, deptNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("El departamento " + deptNo + " no existe.");
                    mostrarDepartamentos(con);
                    return;
                }
                nombre = rs.getString("dnombre");
                empleados = rs.getInt("empleados");
                masaAntes = rs.getBigDecimal("masa");
            }
        }

        System.out.println("Departamento: " + nombre);
        System.out.println("Empleados afectados: " + empleados);
        System.out.println("Masa salarial antes: " + formatoDinero(masaAntes));
        String confirmacion = leerTexto("¿Confirmas el incremento? (S/N): ");
        if (!confirmacion.equalsIgnoreCase("S")) {
            System.out.println("Operación cancelada. No se modificaron salarios.");
            return;
        }

        String updateSql = "UPDATE empleados SET salario = salario * (1 + ? / 100) WHERE dept_no = ?";
        int actualizados;
        try (PreparedStatement ps = con.prepareStatement(updateSql)) {
            ps.setBigDecimal(1, porcentaje);
            ps.setInt(2, deptNo);
            actualizados = ps.executeUpdate();
        }

        BigDecimal masaDespues = consultarMasaDepartamento(con, deptNo);
        System.out.println("Empleados actualizados: " + actualizados);
        System.out.println("Masa salarial después: " + formatoDinero(masaDespues));
        System.out.println("Coste del incremento: " + formatoDinero(masaDespues.subtract(masaAntes)));
    }

    private static BigDecimal consultarMasaDepartamento(Connection con, int deptNo) throws SQLException {
        String sql = "SELECT COALESCE(SUM(salario), 0) FROM empleados WHERE dept_no = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, deptNo);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getBigDecimal(1);
            }
        }
    }

    private static void buscarEmpleados(Connection con) throws SQLException {
        Integer deptNo = leerEnteroOpcional("Número de departamento (Intro para omitir): ");
        String oficio = leerOpcional("Oficio o parte del oficio (Intro para omitir): ");
        Double salarioMinimo = leerDecimalOpcional("Salario mínimo (Intro para omitir): ");
        Double salarioMaximo = leerDecimalOpcional("Salario máximo (Intro para omitir): ");
        String fechaAlta = leerOpcional("Fecha de alta desde AAAA-MM-DD (Intro para omitir): ");

        if (salarioMinimo != null && salarioMaximo != null && salarioMinimo > salarioMaximo) {
            throw new IllegalArgumentException("El salario mínimo no puede superar al máximo.");
        }
        if (fechaAlta != null) {
            Date.valueOf(fechaAlta);
        }

        empleado filtro = new empleado(deptNo, oficio, salarioMaximo, salarioMinimo, fechaAlta);
        StringBuilder sql = new StringBuilder(
                "SELECT e.apellido, e.oficio, d.dnombre, e.salario, " +
                "COALESCE(e.comision, 0) AS comision, e.fecha_alt, " +
                "TIMESTAMPDIFF(YEAR, e.fecha_alt, CURDATE()) AS antiguedad " +
                "FROM empleados e JOIN departamentos d ON e.dept_no = d.dept_no WHERE 1 = 1");
        List<Object> valores = new ArrayList<>();

        if (filtro.getDept_no() != null) {
            sql.append(" AND e.dept_no = ?");
            valores.add(filtro.getDept_no());
        }
        if (filtro.getOficio() != null && !filtro.getOficio().isBlank()) {
            sql.append(" AND e.oficio LIKE ?");
            valores.add("%" + filtro.getOficio().trim() + "%");
        }
        if (filtro.getMinSalario() != null) {
            sql.append(" AND e.salario >= ?");
            valores.add(filtro.getMinSalario());
        }
        if (filtro.getMaxSalario() != null) {
            sql.append(" AND e.salario <= ?");
            valores.add(filtro.getMaxSalario());
        }
        if (filtro.getFechaAlta() != null) {
            sql.append(" AND e.fecha_alt >= ?");
            valores.add(Date.valueOf(filtro.getFechaAlta()));
        }
        sql.append(" ORDER BY e.salario DESC");

        int encontrados = 0;
        int salariosContados = 0;
        BigDecimal sumaSalarios = BigDecimal.ZERO;
        try (PreparedStatement ps = con.prepareStatement(sql.toString())) {
            asignarParametros(ps, valores);
            try (ResultSet rs = ps.executeQuery()) {
                System.out.printf("%-18s %-18s %-22s %18s %18s %-12s %10s%n",
                        "Apellido", "Oficio", "Departamento", "Salario",
                        "Comisión", "Alta", "Años");
                while (rs.next()) {
                    encontrados++;
                    BigDecimal salario = rs.getBigDecimal("salario");
                    if (salario != null) {
                        sumaSalarios = sumaSalarios.add(salario);
                        salariosContados++;
                    }
                    Date alta = rs.getDate("fecha_alt");
                    System.out.printf("%-18s %-18s %-22s %18s %18s %-12s %10d%n",
                            rs.getString("apellido"),
                            rs.getString("oficio"),
                            rs.getString("dnombre"),
                            formatoDinero(salario),
                            formatoDinero(rs.getBigDecimal("comision")),
                            alta == null ? "-" : alta.toString(),
                            rs.getInt("antiguedad"));
                }
            }
        }
        BigDecimal media = salariosContados == 0 ? BigDecimal.ZERO :
                sumaSalarios.divide(BigDecimal.valueOf(salariosContados), 2, RoundingMode.HALF_UP);
        System.out.println("Empleados encontrados: " + encontrados);
        System.out.println("Salario medio: " + formatoDinero(media));
    }

    private static void asignarParametros(PreparedStatement ps, List<Object> valores) throws SQLException {
        for (int i = 0; i < valores.size(); i++) {
            Object valor = valores.get(i);
            int indice = i + 1;
            if (valor instanceof Integer numero) {
                ps.setInt(indice, numero);
            } else if (valor instanceof String texto) {
                ps.setString(indice, texto);
            } else if (valor instanceof Double numero) {
                ps.setDouble(indice, numero);
            } else if (valor instanceof Date fecha) {
                ps.setDate(indice, fecha);
            } else {
                ps.setObject(indice, valor);
            }
        }
    }

    private static void eliminarEmpleado(Connection con) throws SQLException {
        int empNo = leerEntero("Número de empleado: ");
        String consulta = "SELECT e.emp_no, e.apellido, e.oficio, d.dnombre, e.salario " +
                "FROM empleados e LEFT JOIN departamentos d ON e.dept_no = d.dept_no " +
                "WHERE e.emp_no = ?";
        String apellido;
        try (PreparedStatement ps = con.prepareStatement(consulta)) {
            ps.setInt(1, empNo);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    System.out.println("No existe un empleado con número " + empNo + ".");
                    String listar = leerTexto("¿Quieres listar los empleados? (S/N): ");
                    if (listar.equalsIgnoreCase("S")) {
                        mostrarEmpleados(con);
                    }
                    return;
                }
                apellido = rs.getString("apellido");
                System.out.println("Empleado: " + rs.getInt("emp_no") +
                        " | Apellido: " + apellido +
                        " | Oficio: " + rs.getString("oficio") +
                        " | Departamento: " + rs.getString("dnombre") +
                        " | Salario: " + formatoDinero(rs.getBigDecimal("salario")));
            }
        }

        String primeraConfirmacion = leerTexto("¿Deseas eliminar este empleado? (S/N): ");
        if (!primeraConfirmacion.equalsIgnoreCase("S")) {
            System.out.println("Eliminación cancelada.");
            return;
        }

        String apellidoEscrito = leerTexto("Escribe el apellido del empleado para confirmar: ");
        if (!apellido.equalsIgnoreCase(apellidoEscrito)) {
            System.out.println("El apellido no coincide. Eliminación cancelada.");
            return;
        }

        String deleteSql = "DELETE FROM empleados WHERE emp_no = ?";
        try (PreparedStatement ps = con.prepareStatement(deleteSql)) {
            ps.setInt(1, empNo);
            int borrados = ps.executeUpdate();
            if (borrados > 0) {
                System.out.println("Empleado eliminado correctamente.");
            } else {
                System.out.println("No se eliminó ningún empleado.");
            }
        }
    }

    private static boolean existeDepartamento(Connection con, int deptNo) throws SQLException {
        String sql = "SELECT 1 FROM departamentos WHERE dept_no = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, deptNo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    private static void mostrarDepartamentos(Connection con) throws SQLException {
        String sql = "SELECT dept_no, dnombre, loc FROM departamentos ORDER BY dept_no";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("Departamentos disponibles:");
            while (rs.next()) {
                System.out.printf("%d - %s (%s)%n",
                        rs.getInt("dept_no"), rs.getString("dnombre"), rs.getString("loc"));
            }
        }
    }

    private static void mostrarEmpleados(Connection con) throws SQLException {
        String sql = "SELECT emp_no, apellido, oficio, dept_no, salario " +
                "FROM empleados ORDER BY emp_no";
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            System.out.printf("%-8s %-18s %-18s %-8s %18s%n",
                    "N.º", "Apellido", "Oficio", "Depto.", "Salario");
            while (rs.next()) {
                System.out.printf("%-8d %-18s %-18s %-8d %18s%n",
                        rs.getInt("emp_no"),
                        rs.getString("apellido"),
                        rs.getString("oficio"),
                        rs.getInt("dept_no"),
                        formatoDinero(rs.getBigDecimal("salario")));
            }
        }
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    private static String leerOpcional(String mensaje) {
        String valor = leerTexto(mensaje);
        return valor.isBlank() ? null : valor;
    }

    private static int leerEntero(String mensaje) {
        return Integer.parseInt(leerTexto(mensaje));
    }

    private static Integer leerEnteroOpcional(String mensaje) {
        String valor = leerOpcional(mensaje);
        return valor == null ? null : Integer.valueOf(valor);
    }

    private static BigDecimal leerImporte(String mensaje) {
        String valor = leerTexto(mensaje).replace(',', '.');
        return new BigDecimal(valor);
    }

    private static Double leerDecimalOpcional(String mensaje) {
        String valor = leerOpcional(mensaje);
        return valor == null ? null : Double.valueOf(valor.replace(',', '.'));
    }

    private static String formatoDinero(BigDecimal importe) {
        BigDecimal valor = importe == null ? BigDecimal.ZERO : importe;
        return String.format(Locale.forLanguageTag("es-ES"), "%,.2f €", valor);
    }
}
