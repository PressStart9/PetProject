package org.example.Exceptions;

/**
 * Exception which must be thrown if money amount is less than zero.
 */
public class InvalidAmountException extends BusinessExceptions {
    public InvalidAmountException(String message) {
        super(message);
    }
}
