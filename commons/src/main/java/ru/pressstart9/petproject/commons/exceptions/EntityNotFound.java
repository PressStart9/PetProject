package ru.pressstart9.petproject.commons.exceptions;

public class EntityNotFound extends RuntimeException {
    public EntityNotFound(Long id) {
        super("There is no entity with id = " + id);
    }
}
