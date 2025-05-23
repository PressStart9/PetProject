package ru.pressstart9.petproject.common_kafka.exceptions;

public class EntityNotFound extends RuntimeException {
    public EntityNotFound(Long id) {
        super("There is no entity with id = " + id);
    }
}
