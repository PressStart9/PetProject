package org.example.repositories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import org.example.DaoFactory;
import org.example.entities.Pet;

import java.util.List;

public class PetRepository implements IRepository<Pet> {
    @PersistenceContext(unitName = DaoFactory.PERSISTENCE_UNIT_NAME, type = PersistenceContextType.TRANSACTION)
    private final EntityManager entityManager;

    private final DaoFactory daoFactory;

    public PetRepository(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
        this.entityManager = daoFactory.getEntityManagerFactory().createEntityManager();
    }

    @Override
    public Pet save(Pet entity) {
        daoFactory.inTransaction(entityManager -> {
           entityManager.persist(entity);
        });

        return entity;
    }

    @Override
    public void deleteById(long id) {
        deleteByEntity(getById(id));
    }

    @Override
    public void deleteByEntity(Pet entity) {
        daoFactory.inTransaction(entityManager -> {
            entityManager.remove(entity);
        });
    }

    @Override
    public void deleteAll() {
        daoFactory.inTransaction(entityManager -> {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaDelete<Pet> cq = cb.createCriteriaDelete(Pet.class);

            entityManager.createQuery(cq).executeUpdate();
        });
    }

    @Override
    public Pet update(Pet entity) {
        return daoFactory.inTransaction(entityManager -> {
            return entityManager.merge(entity);
        });
    }

    @Override
    public Pet getById(long id) {
        return entityManager.find(Pet.class, id);
    }

    @Override
    public List<Pet> getAll() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Pet> cq = cb.createQuery(Pet.class);
        cq.from(Pet.class);

        return entityManager.createQuery(cq).getResultList();
    }
}
