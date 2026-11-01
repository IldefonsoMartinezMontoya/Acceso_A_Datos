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