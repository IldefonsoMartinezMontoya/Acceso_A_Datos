# Memoria explicativa de Práctica 9

## Select_Scanner.java

El programa pide una consulta por teclado, se conecta a MySQL, la ejecuta y usa `ResultSetMetaData` para obtener las columnas y sus nombres. Después imprime cada fila y muestra `NULL` cuando una celda no tiene valor.

**Nota:** JDBC numera las columnas desde 1, pero los bucles del código comienzan en 0. Para que funcione, deben recorrer `i = 1` hasta `i <= columns`.

### Código

```java
package Practica9;
import java.sql.*;
import java.util.Scanner;

public class Select_Scanner {
    static final String URL = "jdbc:mysql://localhost:3306/practica";
    static final String USER = "practica";
    static final String PASS = "practica";
    static String sql;
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        System.out.print("Introduce la consulta: ");
        sql = sc.nextLine();
        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
        Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            ResultSetMetaData metaData = rs.getMetaData();
            int columns = metaData.getColumnCount();
            for (int i = 0; i < columns; i++) {
                System.out.printf("| %-20s ", metaData.getColumnLabel(i));
            }
            System.out.println("|");
            while (rs.next()) {
                for (int i = 0; i < columns; i++) {
                    String valor = rs.getString(i);
                    System.out.printf("| %-20s ", valor == null ? "NULL" : valor);
                }
                System.out.println("|");
            }
        } catch (SQLException e) {
            System.err.println("Error de base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
```
