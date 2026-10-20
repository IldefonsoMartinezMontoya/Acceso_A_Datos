package Practica6;

import java.sql.*;

public class ApacheDerby {
    static String sql;
    static String url = "jdbc:derby:C:/Users/estudiante/Desktop/BBDD/ApacheDerby";
    public static void main(String[] args) {
        try {
            Connection conexion = DriverManager.getConnection(url);

            Statement sentenDepartamentos = conexion.createStatement();
            sql = "select * from departamento";
            ResultSet resulDepartamentos = sentenDepartamentos.executeQuery(sql);
            while (resulDepartamentos.next ()) {
                System.out.printf("%d, %s, %s %n",
                        resulDepartamentos.getInt("dept"),
                        resulDepartamentos.getString("dNombre"),
                        resulDepartamentos.getString("localizacion")
                );
            }
            resulDepartamentos.close();
            sentenDepartamentos.close();
            Statement sentenciaEmpleados = conexion.createStatement();
            sql = "select * from empleados";
            ResultSet resulEmpleados = sentenciaEmpleados.executeQuery(sql);
            while (resulEmpleados.next()) {
                System.out.printf("%d, %s, %s, %d, %s, %.2f, %.2f, %d%n",
                        resulEmpleados.getInt("emp"),
                        resulEmpleados.getString("apellido"),
                        resulEmpleados.getString("oficio"),
                        resulEmpleados.getInt("dir"),
                        resulEmpleados.getString("fehcaAlta"),
                        resulEmpleados.getDouble("salario"),
                        resulEmpleados.getDouble("comision"),
                        resulEmpleados.getInt("DEPT"));
            }
            resulEmpleados.close();
            sentenciaEmpleados.close();
            conexion.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
