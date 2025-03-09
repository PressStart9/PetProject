package org.example;

import jakarta.persistence.EntityManagerFactory;
import lombok.val;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;

import java.util.concurrent.TimeUnit;

public class DbApp {
    public static void main(String[] args) {
        final String jakartaJdbcUrl = "jdbc:postgresql://localhost:" + System.getenv("POSTGRES_PORT") + "/" + System.getenv("POSTGRES_DB");

        Configuration cfg = new Configuration();
        cfg.setProperty(AvailableSettings.PERSISTENCE_UNIT_NAME, "petproject.jpa");
        cfg.setProperty(AvailableSettings.JAKARTA_JDBC_DRIVER, "org.postgresql.Driver");
        cfg.setProperty(AvailableSettings.JAKARTA_JDBC_URL, jakartaJdbcUrl);
        cfg.setProperty(AvailableSettings.JAKARTA_JDBC_USER, System.getenv("POSTGRES_USER"));
        cfg.setProperty(AvailableSettings.JAKARTA_JDBC_PASSWORD, System.getenv("POSTGRES_PASSWORD"));

        cfg.setProperty(AvailableSettings.HBM2DDL_AUTO, Action.ACTION_UPDATE);

        cfg.setProperty(AvailableSettings.SHOW_SQL, true);
        cfg.setProperty(AvailableSettings.FORMAT_SQL, true);
        cfg.setProperty(AvailableSettings.HIGHLIGHT_SQL, true);

        cfg.addAnnotatedClass(Pet.class);
        cfg.addAnnotatedClass(Person.class);


        try(val sessionFactory = cfg.buildSessionFactory()) {
            Session session = sessionFactory.openSession();
            EntityManagerFactory entityManagerFactory = session.getEntityManagerFactory();

            for (int i = 0; i < 100; i++) {
                System.out.println("Session factory successfully created.");
                TimeUnit.SECONDS.sleep(1);
            }

            session.close();
        } catch (HibernateException | InterruptedException e) {
            for (int i = 0; i < 100; i++) {
                System.out.println(e.getMessage());
            }
        }
    }
}
