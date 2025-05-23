package ru.pressstart9.petproject.commons.exceptions;

public class ServiceNotResponding extends RuntimeException {
    public ServiceNotResponding(String message) {
        super(message);
    }
}
