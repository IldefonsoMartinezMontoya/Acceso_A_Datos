package Practica8;// Importamos las clases de la API JDBC (Connection, DriverManager, DatabaseMetaData, ResultSet...)
import java.sql.*;

/**
 * Ejemplo de uso de DatabaseMetaData: permite "preguntarle" a la base de datos
 * cómo es por dentro (tablas, columnas, claves, procedimientos...) sin conocer
 * su estructura de antemano.
 */
public class EjemploDatabaseMetadata {

    // Datos de conexión como constantes: así están en un único sitio y son fáciles de cambiar.
    // Formato de la URL: jdbc:subprotocolo://host:puerto/base_de_datos
    private static final String URL = "jdbc:mysql://localhost:3306/practica";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "practica";

    // Nombre de la tabla que vamos a inspeccionar en los apartados 3, 4 y 5
    private static final String TABLA = "departamentos";

    public static void main(String[] args) {

        // try-with-resources: la conexión se declara entre paréntesis y Java la cierra
        // AUTOMÁTICAMENTE al salir del bloque, aunque haya una excepción.
        // Así nos ahorramos el conexion.close() manual (y el riesgo de olvidarlo).
        //
        // Nota: con JDBC 4.0+ NO hace falta Class.forName("com.mysql.cj.jdbc.Driver"),
        // el driver se registra solo si el jar de MySQL Connector/J está en el classpath.
        try (Connection conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD)) {

            // DatabaseMetaData es el objeto que nos da acceso a toda la información
            // sobre la estructura y las capacidades de la base de datos.
            DatabaseMetaData dbmd = conexion.getMetaData();

            // IMPORTANTE: en MySQL, "base de datos" y "catálogo" son lo mismo.
            // El driver (Connector/J 8+) ignora el parámetro "schema" y usa "catalog".
            // Por eso le pedimos a la conexión el nombre de la BD actual ("practica")
            // y se lo pasamos como catálogo en las consultas de metadatos.
            // Si pasáramos null, podríamos obtener tablas de TODAS las bases de datos del servidor.
            String catalogo = conexion.getCatalog();

            mostrarInfoGeneral(dbmd);
            mostrarTablas(dbmd, catalogo);
            mostrarColumnas(dbmd, catalogo, TABLA);
            mostrarClavePrimaria(dbmd, catalogo, TABLA);
            mostrarClavesAjenas(dbmd, catalogo, TABLA);
            mostrarProcedimientos(dbmd, catalogo);

        } catch (SQLException e) {
            // Aquí caen los errores de conexión (URL mala, usuario/contraseña incorrectos,
            // servidor apagado...) y los errores al consultar los metadatos.
            System.err.println("Error de base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ------------------------------------------------------------------
    // 1. Información general de la base de datos y del driver
    // ------------------------------------------------------------------
    private static void mostrarInfoGeneral(DatabaseMetaData dbmd) throws SQLException {
        titulo("INFORMACIÓN SOBRE LA BASE DE DATOS");

        System.out.printf("Nombre : %s %n", dbmd.getDatabaseProductName()); // Ej: MySQL
        System.out.printf("Versión: %s %n", dbmd.getDatabaseProductVersion()); // Ej: 8.4.0
        System.out.printf("Driver : %s %n", dbmd.getDriverName());          // Ej: MySQL Connector/J
        System.out.printf("URL    : %s %n", dbmd.getURL());                 // URL con la que nos conectamos
        System.out.printf("Usuario: %s %n", dbmd.getUserName());            // Usuario conectado
    }

    // ------------------------------------------------------------------
    // 2. Tablas y vistas de la base de datos
    // ------------------------------------------------------------------
    private static void mostrarTablas(DatabaseMetaData dbmd, String catalogo) throws SQLException {
        titulo("TABLAS Y VISTAS DE '" + catalogo + "'");

        // getTables(catalog, schemaPattern, tableNamePattern, types)
        //  - catalog: la base de datos (en MySQL)
        //  - schemaPattern: null, porque MySQL no usa esquemas
        //  - tableNamePattern: null = cualquier nombre (admite patrones SQL, como "emp%")
        //  - types: null = todos los tipos; también podría ser new String[]{"TABLE"}
        //
        // El ResultSet también va en el try-with-resources para que se cierre solo.
        try (ResultSet tablas = dbmd.getTables(catalogo, null, null, null)) {
            while (tablas.next()) {
                // Usamos el NOMBRE de la columna en lugar del índice: el código se entiende mejor.
                String tipo = tablas.getString("TABLE_TYPE"); // "TABLE", "VIEW"...
                String nombre = tablas.getString("TABLE_NAME");
                System.out.printf("%-6s -> %s %n", tipo, nombre);
            }
        }
    }

    // ------------------------------------------------------------------
    // 3. Columnas de una tabla concreta
    // ------------------------------------------------------------------
    private static void mostrarColumnas(DatabaseMetaData dbmd, String catalogo, String tabla) throws SQLException {
        titulo("COLUMNAS DE LA TABLA '" + tabla + "'");

        // getColumns(catalog, schemaPattern, tableNamePattern, columnNamePattern)
        // columnNamePattern = null -> queremos todas las columnas
        try (ResultSet columnas = dbmd.getColumns(catalogo, null, tabla, null)) {
            while (columnas.next()) {
                String nombre = columnas.getString("COLUMN_NAME"); // Nombre de la columna
                String tipo = columnas.getString("TYPE_NAME");     // Tipo SQL (INT, VARCHAR...)
                int tamanio = columnas.getInt("COLUMN_SIZE");      // Tamaño / precisión
                String nula = columnas.getString("IS_NULLABLE");   // "YES" o "NO"
                System.out.printf("Columna: %-12s Tipo: %-10s Tamaño: %-5d ¿Nula?: %s %n",
                        nombre, tipo, tamanio, nula);
            }
        }
    }

    // ------------------------------------------------------------------
    // 4. Clave primaria de una tabla
    // ------------------------------------------------------------------
    private static void mostrarClavePrimaria(DatabaseMetaData dbmd, String catalogo, String tabla) throws SQLException {
        titulo("CLAVE PRIMARIA DE '" + tabla + "'");

        // StringBuilder para ir concatenando los nombres de columna.
        // Una clave primaria compuesta tiene varias columnas, por eso hay un bucle.
        StringBuilder pk = new StringBuilder();

        try (ResultSet claves = dbmd.getPrimaryKeys(catalogo, null, tabla)) {
            while (claves.next()) {
                if (pk.length() > 0) {
                    pk.append(" + "); // Separador entre las columnas de una PK compuesta
                }
                pk.append(claves.getString("COLUMN_NAME"));
            }
        }
        System.out.println("Clave primaria: " + pk);
    }

    // ------------------------------------------------------------------
    // 5. Claves ajenas que apuntan a la tabla (ExportedKeys)
    // ------------------------------------------------------------------
    private static void mostrarClavesAjenas(DatabaseMetaData dbmd, String catalogo, String tabla) throws SQLException {
        titulo("CLAVES AJENAS QUE REFERENCIAN A '" + tabla + "'");

        // getExportedKeys: claves ajenas de OTRAS tablas que referencian la PK de ESTA tabla.
        // (La operación contraria es getImportedKeys: las FK que tiene esta tabla hacia otras.)
        try (ResultSet claves = dbmd.getExportedKeys(catalogo, null, tabla)) {
            while (claves.next()) {
                String tablaPk = claves.getString("PKTABLE_NAME");   // Tabla referenciada (la nuestra)
                String columnaPk = claves.getString("PKCOLUMN_NAME"); // Su columna PK
                String tablaFk = claves.getString("FKTABLE_NAME");   // Tabla que tiene la clave ajena
                String columnaFk = claves.getString("FKCOLUMN_NAME"); // Su columna FK

                System.out.printf("%s.%s  <--  %s.%s %n", tablaPk, columnaPk, tablaFk, columnaFk);
            }
        }
    }

    // ------------------------------------------------------------------
    // 6. Procedimientos almacenados
    // ------------------------------------------------------------------
    private static void mostrarProcedimientos(DatabaseMetaData dbmd, String catalogo) throws SQLException {
        titulo("PROCEDIMIENTOS ALMACENADOS");

        boolean hayProcedimientos = false;

        try (ResultSet procs = dbmd.getProcedures(catalogo, null, null)) {
            while (procs.next()) {
                hayProcedimientos = true;
                String nombre = procs.getString("PROCEDURE_NAME");
                int tipo = procs.getInt("PROCEDURE_TYPE");

                // PROCEDURE_TYPE devuelve un número definido por la API JDBC.
                // Lo traducimos a texto para que sea legible.
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

        // Si el bucle no se ejecutó ni una vez, avisamos al usuario
        if (!hayProcedimientos) {
            System.out.println("(No hay procedimientos almacenados)");
        }
    }

    // ------------------------------------------------------------------
    // Método auxiliar para imprimir los títulos con un formato uniforme
    // ------------------------------------------------------------------
    private static void titulo(String texto) {
        System.out.println("\n" + texto);
        System.out.println("-".repeat(texto.length())); // Subrayado del mismo largo que el título
    }
}