package org.example;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import lombok.Getter;
import lombok.val;
import org.example.entities.Person;
import org.example.entities.Pet;
import org.example.repositories.PersonRepository;
import org.example.repositories.PetRepository;
import org.hibernate.Session;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.schema.Action;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DaoFactory {
    private static DaoFactory instance;
    public static final String PERSISTENCE_UNIT_NAME = "petproject.jpa";

    private final EntityManagerFactory entityManagerFactory;

    @Getter
    private final PetRepository petRepository;
    @Getter
    private final PersonRepository personRepository;

    private Configuration makeConfig() {
        final String jakartaJdbcUrl = "jdbc:postgresql://localhost:" + System.getenv("POSTGRES_PORT") + "/" + System.getenv("POSTGRES_DB");

        Configuration cfg = new Configuration();
        cfg.setProperty(AvailableSettings.PERSISTENCE_UNIT_NAME, PERSISTENCE_UNIT_NAME);
        cfg.setProperty(AvailableSettings.JTA_PLATFORM, "org.hibernate.service.jta.platform.internal.JBossAppServerJtaPlatform");
        cfg.setProperty(AvailableSettings.JAKARTA_JDBC_DRIVER, "org.postgresql.Driver");
        cfg.setProperty(AvailableSettings.JAKARTA_JDBC_URL, jakartaJdbcUrl);
        cfg.setProperty(AvailableSettings.JAKARTA_JDBC_USER, System.getenv("POSTGRES_USER"));
        cfg.setProperty(AvailableSettings.JAKARTA_JDBC_PASSWORD, System.getenv("POSTGRES_PASSWORD"));

        cfg.setProperty(AvailableSettings.HBM2DDL_AUTO, Action.ACTION_UPDATE);

        cfg.setProperty(AvailableSettings.SHOW_SQL, false);
        cfg.setProperty(AvailableSettings.FORMAT_SQL, false);
        cfg.setProperty(AvailableSettings.HIGHLIGHT_SQL, false);

        cfg.addAnnotatedClass(Pet.class);
        cfg.addAnnotatedClass(Person.class);

        return cfg;
    }

    public DaoFactory() {
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        val sessionFactory = makeConfig().buildSessionFactory();
        Session session = sessionFactory.openSession();
        entityManagerFactory = session.getEntityManagerFactory();
        session.close();

        petRepository = new PetRepository(entityManagerFactory.createEntityManager());
        personRepository = new PersonRepository(entityManagerFactory.createEntityManager());
    }

    public static DaoFactory get() {
        if (instance == null) {
            instance = new DaoFactory();
        }
        return instance;
    }

    public void inTransaction(Consumer<EntityManager> work) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            work.accept(entityManager);
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
        finally {
            entityManager.close();
        }
    }

    public <R> R inTransaction(Function<EntityManager, R> work) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            R res = work.apply(entityManager);
            transaction.commit();
            return res;
        }
        catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
        finally {
            entityManager.close();
        }
    }
}
