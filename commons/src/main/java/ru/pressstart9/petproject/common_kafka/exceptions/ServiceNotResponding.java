package ru.pressstart9.petproject.common_kafka.exceptions;

public class ServiceNotResponding extends RuntimeException {
    public ServiceNotResponding(String message) {
        super(message);
    }
}
