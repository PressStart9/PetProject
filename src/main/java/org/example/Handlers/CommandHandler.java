package org.example.Handlers;

import org.example.CashMachine;

import java.util.Scanner;

public abstract class CommandHandler {
    protected final Scanner scanner = new Scanner(System.in);
    protected CommandHandler nextHandler;

    /**
     * Sets the next handler in the chain.
     *
     * @param nextHandler the next handler to set
     * @return the current CommandHandler instance for method chaining
     */
    public CommandHandler setNextHandler(CommandHandler nextHandler) {
        if (this.nextHandler == null) {
            this.nextHandler = nextHandler;
            return this;
        }

        this.nextHandler.setNextHandler(nextHandler);
        return this;
    }

    /**
     * Handles the command for the given cash machine and request.
     *
     * @param machine the cash machine to handle the command for
     * @param request the request to handle
     */
    public abstract void handle(CashMachine machine, String request);
}
