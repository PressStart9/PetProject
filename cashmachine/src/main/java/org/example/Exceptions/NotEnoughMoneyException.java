package org.example.Exceptions;

/**
 * Exception which must be thrown if money amount would be less than zero after operation.
 */
public class NotEnoughMoneyException extends BusinessExceptions {
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}

