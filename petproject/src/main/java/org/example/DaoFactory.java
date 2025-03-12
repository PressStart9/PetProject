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
    public static final String DEFAULT_POSTGRES_USER = "postgres";
    public static final String DEFAULT_POSTGRES_PASSWORD = "postgres";
    public static final String DEFAULT_POSTGRES_PORT = "5432";
    public static final String DEFAULT_POSTGRES_DB = "PetProjectDB";
    public static final String DEFAULT_PGADMIN_DEFAULT_EMAIL = "postgres@postgres.com";
    public static final String DEFAULT_PGADMIN_DEFAULT_PASSWORD = "postgres";
    public static final String PERSISTENCE_UNIT_NAME = "petproject.jpa";

    @Getter
    private final EntityManagerFactory entityManagerFactory;

    @Getter
    private final PetRepository petRepository;
    @Getter
    private final PersonRepository personRepository;

    private static Configuration makeConfig(String user, String password, String port, String db, boolean silent) {
        final String jakartaJdbcUrl = "jdbc:postgresql://localhost:" + port + "/" + db;

        Configuration cfg = new Configuration();
        cfg.setProperty(AvailableSettings.PERSISTENCE_UNIT_NAME, PERSISTENCE_UNIT_NAME);
        cfg.setProperty(AvailableSettings.JTA_PLATFORM, "org.hibernate.service.jta.platform.internal.JBossAppServerJtaPlatform");
        cfg.setProperty(AvailableSettings.JAKARTA_JDBC_DRIVER, "org.postgresql.Driver");
        cfg.setProperty(AvailableSettings.JAKARTA_JDBC_URL, jakartaJdbcUrl);
        cfg.setProperty(AvailableSettings.JAKARTA_JDBC_USER, user);
        cfg.setProperty(AvailableSettings.JAKARTA_JDBC_PASSWORD, password);

        cfg.setProperty(AvailableSettings.HBM2DDL_AUTO, Action.ACTION_UPDATE);

        cfg.setProperty(AvailableSettings.SHOW_SQL, !silent);
        cfg.setProperty(AvailableSettings.FORMAT_SQL, !silent);
        cfg.setProperty(AvailableSettings.HIGHLIGHT_SQL, !silent);

        cfg.addAnnotatedClass(Pet.class);
        cfg.addAnnotatedClass(Person.class);

        return cfg;
    }

    public DaoFactory() {
        this(
            true
        );
    }

    public DaoFactory(boolean silent) {
        this(
            System.getenv("POSTGRES_USER") != null ? System.getenv("POSTGRES_USER") : DEFAULT_POSTGRES_USER,
            System.getenv("POSTGRES_PASSWORD") != null ? System.getenv("POSTGRES_PASSWORD") : DEFAULT_POSTGRES_PASSWORD,
            System.getenv("POSTGRES_PORT") != null ? System.getenv("POSTGRES_PORT") : DEFAULT_POSTGRES_PORT,
            System.getenv("POSTGRES_DB") != null ? System.getenv("POSTGRES_DB") : DEFAULT_POSTGRES_DB,
            silent
        );
    }

    public DaoFactory(String postgresUser, String postgresPassword, String postgresPort, String postgresDb) {
        this(
            postgresUser,
            postgresPassword,
            postgresPort,
            postgresDb,
            true
        );
    }

    public DaoFactory(String postgresUser, String postgresPassword, String postgresPort, String postgresDb, boolean silent) {
        Logger.getLogger("org.hibernate").setLevel(silent ? Level.SEVERE : Level.ALL);

        val sessionFactory = makeConfig(postgresUser, postgresPassword, postgresPort, postgresDb, silent).buildSessionFactory();
        Session session = sessionFactory.openSession();
        entityManagerFactory = session.getEntityManagerFactory();
        session.close();

        petRepository = new PetRepository(this);
        personRepository = new PersonRepository(this);
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
