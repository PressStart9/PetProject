package ru.pressstart9.petproject.common_kafka.exceptions;

public class InvalidServiceResponse extends RuntimeException {
    public InvalidServiceResponse(String message) {
        super(message);
    }
}
