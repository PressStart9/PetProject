package org.example.Handlers;

import org.example.CashMachine;

public class SelectAccountHandler extends CommandHandler {
    private static final String COMMAND_NAME = "select";

    @Override
    public void handle(CashMachine machine, String request) {
        if (!request.equalsIgnoreCase(COMMAND_NAME)) {
            if (nextHandler != null) {
                nextHandler.handle(machine, request);
            }
            return;
        }

        System.out.println("Enter account number:");
        String accountNumber = scanner.nextLine();
        machine.setCurrentAccount(machine.getAccount(accountNumber));
    }
}
