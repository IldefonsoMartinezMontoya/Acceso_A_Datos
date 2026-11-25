package PruebaConexion;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class PruebaConexion {
    public static void probarConexion(String ficheroCfg) {
        String baseDatos = ficheroCfg.contains("mysql")
                ? "MySQL (practica)"
                : "PostgreSQL (escuela)";

        try (SessionFactory factory = new Configuration()
                .configure(ficheroCfg)
                .buildSessionFactory();
             Session session = factory.openSession()) {

            String version = session
                    .createNativeQuery("SELECT version()", String.class)
                    .getSingleResult();
            System.out.println(baseDatos + " - " + version);
        } catch (RuntimeException e) {
            System.err.println("Error conectando a " + baseDatos + ": " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        probarConexion("hibernate-mysql.cfg.xml");
        probarConexion("hibernate-postgres.cfg.xml");
    }
}
