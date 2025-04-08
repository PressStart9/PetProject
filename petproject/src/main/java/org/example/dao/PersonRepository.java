package org.example.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import org.example.dto.PersonDto;
import org.example.entities.Person;
import org.example.entities.Pet;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.stream.Collectors;

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
            var entity = entityManager.find(Person.class, id);
            if (entity == null) {
                return null;
            }
            Hibernate.initialize(entity.getPets());
            return entity;
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
