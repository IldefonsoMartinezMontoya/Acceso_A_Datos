package Practica11;

import java.sql.*;
import java.util.Scanner;

public class PreparedStatment {
    static final String URL = "jdbc:mysql://localhost:3306/practica";
    static final String USER = "practica";
    static final String PASS = "practica";
    static String sql;
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) throws SQLException {
        while (true) {
            System.out.println("1. Contratar nuevo empleado(insert)");
            System.out.println("2. Análisis salarial por departamento (GROUP BY y funciones agregadas)");
            System.out.println("3. Ranking de empleados mejor pagados (ORDER BY y LIMIT)");
            System.out.println("4. Incrementar salarios de un departamento (UPDATE)");
            System.out.println("5. Búsqueda de empleados por criterios múltiples (consulta dinámica)");
            System.out.println("6.  Eliminar empleado con confirmación (DELETE)");
            System.out.println("7. Salir del programa");
            System.out.print("Elige opción: ");
            int opcion = sc.nextInt();
            try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
                switch (opcion) {
                    case 1:
                        System.out.println("Ingresa el nombre del empleado: ");
                        String nombre = sc.nextLine();
                        System.out.println("Ingresa el apellido del empleado: ");
                        String apellido = sc.nextLine();
                        System.out.println("Ingresa el oficio del empleado: ");
                        String oficio = sc.nextLine();
                        System.out.println("Ingresa el salario del empleado: ");
                        double salario = sc.nextDouble();
                        System.out.println("Ingresa el número del departamento del empleado: ");
                        int departamento = sc.nextInt();
                        sql = "select 1 from departamentos where dept_no = ?";
                        try (PreparedStatement st = con.prepareStatement(sql)){
                            st.setInt(1, departamento);
                            try (ResultSet rs = st.executeQuery()){
                                boolean existe = rs.next();
                                if (existe) {
                                    sql = "insert into empleados (emp_no, nombre, apellido, oficio, fecha_alt, salario, dept_no) values (?, ?, ?, curdate(), ?, ?)";
                                    try (PreparedStatement st2 = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                                        st2.setString(1, nombre);
                                        st2.setString(2, apellido);
                                        st2.setString(3, oficio);
                                        st2.setDouble(4, salario);
                                        st2.setInt(5, departamento);
                                    }
                                } else {
                                    System.out.println("El departamento " + departamento + " no existe.");
                                    mostrarDepartamentos(con);
                                }
                            }
                        }
                        break;
                    case 2:
                        sql = "SELECT departamentos.dnombre, departamentos.loc, " +
                                "count(empleados.emp_no), " +
                                "coalesce(max(empleados.salario), 0), " +
                                "coalesce(min(empleados.salario), 0), " +
                                "coalesce(round(avg(empleados.salario), 2), 0), " +
                                "coalesce(sum(empleados.salario), 0), " +
                                "coalesce(round(100.0 * sum(empleados.salario) / " +
                                "nullif((select sum(empleados.salario) from empleados), 0), 2), 0) " +
                                "from departamentos " +
                                "left join empleados on departamentos.dept_no = empleados.dept_no " +
                                "group by departamentos.dept_no, departamentos.dnombre, departamentos.loc " +
                                "order by departamentos.dept_no";
                        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()){
                            System.out.printf("%-20s %-15s %10s %12s %12s %12s %15s %12s%n",
                                    "Departamento", "Localidad", "Empleados", "Máximo", "Mínimo",
                                    "Media", "Masa salarial", "% del total");
                            while (rs.next()) {
                                System.out.printf("%-20s %-15s %10d %12.2f %12.2f %12.2f %15.2f %11.2f%%%n",
                                        rs.getString(1),
                                        rs.getString(2),
                                        rs.getInt(3),
                                        rs.getDouble(4),
                                        rs.getDouble(5),
                                        rs.getDouble(6),
                                        rs.getDouble(7),
                                        rs.getDouble(8));
                            }
                        }
                        break;
                    case 3:
                        sql = "select row_number() over (order by " +
                                "(empleados.salario + coalesce(empleados.comision, 0)) desc, empleados.emp_no), " +
                                "empleados.apellido, empleados.oficio, departamentos.dnombre, " +
                                "(empleados.salario + coalesce(empleados.comision, 0)) " +
                                "from empleados join departamentos " +
                                "on empleados.dept_no = departamentos.dept_no " +
                                "order by (empleados.salario + coalesce(empleados.comision, 0)) desc, " +
                                "empleados.emp_no limit 10";
                        try (PreparedStatement st = con.prepareStatement(sql);
                             ResultSet rs = st.executeQuery()) {
                            System.out.println("pos. | apellido | oficio | departamento | salario total");
                            while (rs.next()) {
                                int posicion = rs.getInt(1);
                                String marca = posicion <= 3 ? "*" : "";

                                System.out.printf("%2d%s | %-15s | %-15s | %-15s | %.2f%n",
                                        posicion,
                                        marca,
                                        rs.getString(2),
                                        rs.getString(3),
                                        rs.getString(4),
                                        rs.getDouble(5));
                            }
                        }
                    case 4:
                    System.out.println("Ingrese el numero de un departamento: ");
                    int deptno = sc.nextInt();
                        sql = "select 1 from departamentos where dept_no = ?";
                        try (PreparedStatement st = con.prepareStatement(sql)) {
                            st.setInt(1, deptno);
                            try (ResultSet rs = st.executeQuery()) {
                                boolean existe = rs.next();
                                if (existe) {
                                    System.out.println("Ingrese un porcentaje de subida: ");
                                    int subida = sc.nextInt();
                                    sql = "select dnombre, count(empleados.emp_no) " +
                                            "from departamentos left join empleados " +
                                            "on departamentos.dept_no = empleados.dept_no " +
                                            "where departamentos.dept_no = ? " +
                                            "group by departamentos.dept_no, departamentos.dnombre";
                                    try (PreparedStatement st2 = con.prepareStatement(sql)) {
                                        st2.setInt(1, deptno);
                                        try (ResultSet rs2 = st2.executeQuery()) {
                                            if (rs2.next()) {
                                                String nombreDepartamento = rs2.getString(1);
                                                int afectados = rs2.getInt(2);

                                                System.out.println("Departamento: " + nombreDepartamento);
                                                System.out.println("Empleados afectados: " + afectados);
                                            } else {
                                                System.out.println("Ese departamento no existe.");
                                            }
                                        }
                                        System.out.println("Quiere actualizar(S/N): ");
                                        char sino = sc.next().charAt(0);
                                        switch (Character.toUpperCase(sino)) {
                                            case 's':
                                                sql = "update empleados on salario = salario * (1 + ? / 100) where dept_no = ?";
                                                try (PreparedStatement st3 = con.prepareStatement(sql)) {
                                                    st3.setInt(1, subida);
                                                    st3.setInt(2, deptno);
                                                    int resultado = st3.executeUpdate();
                                                    System.out.println("Empleados afectados: " + resultado);
                                                }
                                        }
                                    }
                                }
                            }
                        }
                }
            }
        }
    }
    private static void mostrarDepartamentos(Connection con) throws SQLException {
        String consulta = "SELECT dept_no, dnombre FROM departamentos ORDER BY dept_no";
        boolean hayDepartamentos = false;

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(consulta)) {

            System.out.println("Departamentos disponibles:");

            while (rs.next()) {
                hayDepartamentos = true;
                System.out.printf("%d - %s%n",
                        rs.getInt("dept_no"),
                        rs.getString("dnombre"));
            }
        }

        if (!hayDepartamentos) {
            System.out.println("No hay departamentos disponibles.");
        }
    }
}
