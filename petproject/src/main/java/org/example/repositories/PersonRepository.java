package org.example.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import lombok.RequiredArgsConstructor;
import org.example.DaoFactory;
import org.example.entities.Person;

import java.util.List;

@RequiredArgsConstructor
public class PersonRepository implements IRepository<Person> {
    @PersistenceContext(unitName = DaoFactory.PERSISTENCE_UNIT_NAME, type = PersistenceContextType.TRANSACTION)
    private final EntityManager entityManager;

    @Override
    public Person save(Person entity) {
        DaoFactory.get().inTransaction(entityManager -> {
            entityManager.persist(entity);
        });

        return entity;
    }

    @Override
    public void deleteById(long id) {
        deleteByEntity(getById(id));
    }

    @Override
    public void deleteByEntity(Person entity) {
        DaoFactory.get().inTransaction(entityManager -> {
            entityManager.remove(entity);
        });
    }

    @Override
    public void deleteAll() {
        DaoFactory.get().inTransaction(entityManager -> {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaDelete<Person> cq = cb.createCriteriaDelete(Person.class);

            entityManager.createQuery(cq);
        });
    }

    @Override
    public Person update(Person entity) {
        return DaoFactory.get().inTransaction(entityManager -> {
            return entityManager.merge(entity);
        });
    }

    @Override
    public Person getById(long id) {
        return entityManager.find(Person.class, id);
    }

    @Override
    public List<Person> getAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Person> cq = cb.createQuery(Person.class);
        cq.from(Person.class);

        return entityManager.createQuery(cq).getResultList();
    }
}
