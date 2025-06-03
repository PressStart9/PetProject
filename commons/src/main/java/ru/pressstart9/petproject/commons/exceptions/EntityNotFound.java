package ru.pressstart9.petproject.commons.exceptions;

public class EntityNotFound extends RuntimeException {
    public EntityNotFound(String id) {
        super("There is no entity with id = " + id);
    }
}
