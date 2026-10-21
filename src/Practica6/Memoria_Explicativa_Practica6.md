# Memoria explicativa - Práctica 6

## Objetivo

La práctica reúne cuatro programas Java que acceden mediante JDBC a bases de datos diferentes: Apache Derby, H2, HSQLDB y SQLite. En todos los casos se recupera información de departamentos y empleados y se muestra en la consola.

## Funcionamiento común

1. Se define una URL JDBC que indica el motor y la ubicación de la base de datos.
2. Se abre la conexión mediante `DriverManager.getConnection(...)`.
3. Se crea un `Statement` y se ejecuta una consulta `SELECT`.
4. El `ResultSet` devuelve las filas encontradas. El bucle `while (resultado.next())` avanza por cada fila.
5. Los valores se imprimen con `System.out.printf`. Los formatos `%d` muestran enteros, `%s` muestran texto y `%.2f` muestran números decimales con dos cifras.
6. Se cierran los resultados, las sentencias y la conexión. Las excepciones SQL se gestionan dentro de un bloque `try-catch`.

Las tres primeras versiones consultan la tabla `departamento` (singular); SQLite consulta `departamentos` (plural). Por ello, cada consulta debe coincidir con el nombre real de la tabla en su base de datos.

## Programas

---

## 1. Apache Derby

### Explicación

Este programa se conecta a una base de datos Apache Derby almacenada en la ruta indicada en la variable `url`. No proporciona usuario ni contraseña en la llamada a `getConnection`, por lo que utiliza la configuración local de Derby.

A continuación consulta la tabla `departamento` y muestra el identificador, el nombre y la localización de cada departamento. Después consulta `empleados` e imprime el identificador, apellido, oficio, director, fecha de alta, salario, comisión y departamento asociado.

Los datos se obtienen por el nombre de cada columna. Las consultas son de solo lectura (`SELECT`); el programa no crea, modifica ni elimina registros. Al terminar cierra cada `ResultSet`, cada `Statement` y la conexión. Si se produce un error SQL, lo convierte en una excepción `RuntimeException`.

### Código fuente completo

```java
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
```

---

## 2. H2

### Explicación

Este programa realiza las mismas consultas y muestra los mismos campos que la versión de Apache Derby, pero se conecta a una base de datos H2 en modo archivo. La ruta del archivo se especifica en la URL JDBC. La conexión se abre con el usuario `sa` y una contraseña vacía.

Primero recorre los departamentos de `departamento` y luego los empleados de `empleados`. En ambos casos accede a las columnas por su nombre y emplea `printf` para presentar números y texto en una línea por registro. Cierra los recursos al finalizar y, ante un error SQL, lanza una `RuntimeException`.

### Código fuente completo

```java
package Practica6;

import java.sql.*;

public class H2 {
    static String url = "jdbc:h2:file:C:/Users/estudiante/Desktop/BBDD/miBaseH2";
    static String sql;
    public static void main(String[] args) {
        try {
            Connection conexion = DriverManager.getConnection(url, "sa", "");
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
```

---

## 3. HSQLDB

### Explicación

Este programa consulta las tablas de departamentos y empleados en una base de datos HSQLDB persistida en archivos. La URL JDBC identifica la ubicación y el nombre de la base de datos. La conexión se solicita sin indicar usuario ni contraseña.

El flujo es equivalente al de Derby y H2: ejecuta un `SELECT *` sobre `departamento`, imprime sus columnas `dept`, `dNombre` y `localizacion`; después ejecuta otro sobre `empleados` e imprime sus campos principales. Recorre cada resultado con `ResultSet.next()` y cierra consultas y conexión. Los errores SQL se propagan como `RuntimeException`.

### Código fuente completo

```java
package Practica6;

import java.sql.*;

public class HSQLDB {
    static String sql;
    static String url = "jdbc:hsqldb:file:C:/Users/estudiante/Desktop/BBDD/HSQLDB/miBase";
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
```

---

## 4. SQLite

### Explicación

Este programa se conecta a un archivo SQLite cuya ubicación aparece en `dataBase`. Consulta `departamentos` (en plural) y `empleados`. Para los departamentos obtiene las tres columnas por posición (`1`, `2` y `3`); para los empleados usa los nombres de columna, igual que las otras versiones.

El programa incluye comentarios que describen la conexión, la preparación de la consulta y el recorrido de resultados. Al terminar cierra los resultados, las sentencias y la conexión. A diferencia de los otros tres programas, cuando ocurre un error SQL imprime la traza con `printStackTrace()` en vez de lanzar una `RuntimeException`.

### Código fuente completo

```java
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
```

## Observaciones

- En las cuatro versiones aparece el nombre de columna `fehcaAlta` en la consulta de empleados. Se ha conservado tal como está escrito en los programas. Si la columna real se llama, por ejemplo, `fechaAlta`, habría que corregir el identificador en el código.
- Las URL apuntan a rutas concretas del equipo. Para ejecutar los programas en otro ordenador, deben ajustarse a la ubicación real de los archivos de base de datos.
- Para que las conexiones funcionen, el proyecto debe tener disponible el controlador JDBC del motor correspondiente y las tablas/columnas consultadas deben existir.

