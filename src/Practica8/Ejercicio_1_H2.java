package Practica8;

import java.sql.*;

public class Ejercicio_1_H2 {
    private static final String URL = "jdbc:h2:file:C:/Users/estudiante/Desktop/BBDD/miBaseH2";
    private static final String USUARIO = "sa";
    private static final String PASSWORD = "";
    private static final String TABLA = "departamento";

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL, USUARIO, PASSWORD)){
            DatabaseMetaData dbmd = con.getMetaData();
            String catalogo = con.getCatalog();
            mostrarInfoGeneral(dbmd);
            mostrarTablas(dbmd, catalogo);
            mostrarColumnas(dbmd, catalogo, TABLA);
            mostrarClavePrimaria(dbmd, catalogo, TABLA);
            mostrarClavesAjenas(dbmd, catalogo, TABLA);
            mostrarProcedimientos(dbmd, catalogo);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static void mostrarInfoGeneral(DatabaseMetaData dbmd) throws SQLException {
        titulo("Informacion sobre la Base de Datos");
        System.out.printf("Nombre : %s %n", dbmd.getDatabaseProductName());
        System.out.printf("Versión: %s %n", dbmd.getDatabaseProductVersion());
        System.out.printf("Driver : %s %n", dbmd.getDriverName());
        System.out.printf("URL    : %s %n", dbmd.getURL());
        System.out.printf("Usuario: %s %n", dbmd.getUserName());
    }
    private static void mostrarTablas(DatabaseMetaData dbmd, String catalogo) {
        titulo("Tablas y Vistas de '" + catalogo + "'");
        try (ResultSet rs = dbmd.getTables(catalogo, null, null, null)){
            while (rs.next()) {
                String tipo = rs.getString("TABLE_TYPE");
                String nombre = rs.getString("TABLE_NAME");
                System.out.printf("%-6s -> %s %n", tipo, nombre);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static void mostrarColumnas(DatabaseMetaData dbmd, String catalogo, String tabla) {
        titulo("Columnas de la tabla '" + tabla + "'");
        try (ResultSet rs = dbmd.getColumns(catalogo, null, tabla, null)){
            while (rs.next()) {
                String nombre = rs.getString("COLUMN_NAME");
                String tipo = rs.getString("TYPE_NAME");
                int tamanio = rs.getInt("COLUMN_SIZE");
                String nula = rs.getString("IS_NULLABLE");
                System.out.printf("Columna: %-12s Tipo: %-10s Tamaño: %-5d ¿Nula?: %s %n", nombre, tipo, tamanio, nula);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static void mostrarClavePrimaria(DatabaseMetaData dbmd, String catalogo, String tabla) {
        titulo("Clave primaria de '" + tabla + "'");
        StringBuilder pk = new StringBuilder();
        try (ResultSet rs = dbmd.getPrimaryKeys(catalogo, null, tabla)){
            while (rs.next()) {
                if (pk.length() > 0) {
                    pk.append(" + ");
                }
                pk.append(rs.getString("COLUMN_NAME"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Clave primaria: " + pk);
    }
    private static void mostrarClavesAjenas(DatabaseMetaData dbmd, String catalogo, String tabla) {
        titulo("Claves ajenas que referencian a '" + tabla + "'");
        try (ResultSet rs = dbmd.getExportedKeys(catalogo, null, tabla)){
            while (rs.next()) {
                String tablaPK = rs.getString("PKTABLE_NAME");
                String columnaPK = rs.getString("PKCOLUMN_NAME");
                String tablaFK = rs.getString("FKTABLE_NAME");
                String columnaFK = rs.getString("FKCOLIMN_NAME");
                System.out.printf("%s.%s <-- %s.%s %n", tablaPK, columnaPK, tablaFK, columnaFK);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static void mostrarProcedimientos(DatabaseMetaData dbmd, String catalogo) throws SQLException {
        titulo("Procedimientos almacenados");
        boolean hayProcedimientos = false;
        try (ResultSet rs = dbmd.getProcedures(catalogo, null, null)){
            while (rs.next()) {
                hayProcedimientos = true;
                String nombre = rs.getString("PROCEDURE_NAME");
                int tipo = rs.getInt("PROCEDURE_TYPE");
                String descripcionTipo;
                switch (tipo) {
                    case DatabaseMetaData.procedureNoResult:
                        descripcionTipo = "no devuelve resultado";
                        break;
                    case DatabaseMetaData.procedureReturnsResult:
                        descripcionTipo = "devuelve resultado";
                        break;
                    default:
                        descripcionTipo = "resultado desconocido";
                }
                System.out.printf("Procedimiento: %s (%s) %n", nombre, descripcionTipo);
            }
        }
        if (!hayProcedimientos) {
            System.out.println("(No hay procedimientos almacenados)");
        }
    }
    private static void titulo(String texto) {
        System.out.println("\n" + texto);
        System.out.println("-".repeat(texto.length()));
    }
}
