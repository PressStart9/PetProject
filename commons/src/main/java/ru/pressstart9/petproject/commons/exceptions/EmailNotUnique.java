package ru.pressstart9.petproject.commons.exceptions;

public class EmailNotUnique extends RuntimeException {
    public EmailNotUnique(String message) {
        super(message);
    }
}
