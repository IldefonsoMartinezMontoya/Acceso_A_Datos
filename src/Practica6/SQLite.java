package Practica6;

import java.sql.*;

public class SQLite {
    static String sql;
    static String dataBase = "jdbc:sqlite:C:/Users/estudiante/Desktop/BBDD/SQLIte/miDatabase.db";
    public static void main (String [] args){
        try {
            //Establecemos la conexion con la BD
            Connection conexion = DriverManager.getConnection(dataBase);

            // Preparamos la consulta
            Statement sentenciaDepartamentos = conexion.createStatement();
            sql = "SELECT * FROM departamentos";
            ResultSet resulDepartamentos = sentenciaDepartamentos.executeQuery(sql);

            //Recorremos el resultado para visualizar cada fila 
            //Se hace un bucle mientras haya registros y se van mostrando 
            while (resulDepartamentos.next ()) {
                System.out.printf("%d, %s, %s %n", 
                    resulDepartamentos.getInt(1),
                    resulDepartamentos.getString(2),
                    resulDepartamentos.getString(3));
            }
            
            resulDepartamentos.close(); //Cerrar ResultSet
            sentenciaDepartamentos.close(); //Cerrar Statement
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
            conexion.close(); //Cerrar conexión 
        } catch (SQLException e){
            e.printStackTrace(); 
        }
    }// fin de main 
}// fin de la clase 