package ru.pressstart9.petproject.common_kafka.exceptions;

public class EmailNotUnique extends RuntimeException {
    public EmailNotUnique(String message) {
        super(message);
    }
}
