package org.example.Exceptions;

/**
 * Base class for all other exceptions for business logic.
 */
public abstract class BusinessExceptions extends RuntimeException {
    public BusinessExceptions(String message) {
        super(message);
    }
}
