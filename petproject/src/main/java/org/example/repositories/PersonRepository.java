package org.example.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import org.example.DaoFactory;
import org.example.entities.Person;
import org.example.entities.Pet;

import java.util.List;

public class PersonRepository implements IRepository<Person> {
    private final DaoFactory daoFactory;

    public PersonRepository(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    @Override
    public Person save(Person entity) {
        return daoFactory.inTransaction(entityManager -> {
            entityManager.persist(entity);
            return entity;
        });
    }

    @Override
    public void deleteById(long id) {
        Person entity = getById(id);
        if (entity != null) {
            deleteByEntity(entity);
        }
    }

    @Override
    public void deleteByEntity(Person entity) {
        daoFactory.inTransaction(entityManager -> {
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
        });
    }

    @Override
    public void deleteAll() {
        daoFactory.inTransaction(entityManager -> {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaDelete<Person> cq = cb.createCriteriaDelete(Person.class);

            entityManager.createQuery(cq).executeUpdate();
        });
    }

    @Override
    public Person update(Person entity) {
        return daoFactory.inTransaction(entityManager -> {
            return entityManager.merge(entity);
        });
    }

    @Override
    public Person getById(long id) {
        return daoFactory.inTransaction(entityManager -> {
            return entityManager.find(Person.class, id);
        });
    }

    @Override
    public List<Person> getAll() {
        return daoFactory.inTransaction(entityManager -> {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Person> cq = cb.createQuery(Person.class);
            cq.from(Person.class);
            return entityManager.createQuery(cq).getResultList();
        });
    }
}
