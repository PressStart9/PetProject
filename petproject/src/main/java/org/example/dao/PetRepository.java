package org.example.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import org.example.dto.AvailableColor;
import org.example.entities.Pet;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Set;

public class PetRepository implements IRepository<Pet> {
    private final DaoFactory daoFactory;

    public PetRepository(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public List<Pet> getPetsByColor(AvailableColor color) {
        return daoFactory.inTransaction(entityManager -> {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Pet> cq = cb.createQuery(Pet.class);
            var root = cq.from(Pet.class);
            cq.where(cb.equal(root.get("color"), color));
            return entityManager.createQuery(cq).getResultList();
        });
    }

    @Override
    public Pet save(Pet entity) {
        return daoFactory.inTransaction(entityManager -> {
            entityManager.persist(entity);
            return entity;
        });
    }

    @Override
    public void deleteById(long id) {
        Pet entity = getById(id);
        if (entity != null) {
            deleteByEntity(entity);
        }
    }

    @Override
    public void deleteByEntity(Pet entity) {
        daoFactory.inTransaction(entityManager -> {
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
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
        return daoFactory.inTransaction(entityManager -> {
            var entity = entityManager.find(Pet.class, id);
            if (entity == null) {
                return null;
            }
            Hibernate.initialize(entity.getOwner());
            Hibernate.initialize(entity.getFriends());
            return entity;
        });
    }

    @Override
    public List<Pet> getAll() {
        return daoFactory.inTransaction(entityManager -> {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Pet> cq = cb.createQuery(Pet.class);
            cq.from(Pet.class);
            return entityManager.createQuery(cq).getResultList();
        });
    }

    public List<Pet> getPetsByIds(Set<Long> friendsIds) {
        return daoFactory.inTransaction(entityManager -> {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Pet> cq = cb.createQuery(Pet.class);
            var root = cq.from(Pet.class);
            cq.where(root.get("id").in(friendsIds));
            return entityManager.createQuery(cq).getResultList();
        });
    }
}
