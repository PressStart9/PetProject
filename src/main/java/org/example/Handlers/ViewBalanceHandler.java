package org.example.Handlers;

import org.example.Account;
import org.example.CashMachine;

public class ViewBalanceHandler extends CommandHandler {
    private static final String COMMAND_NAME = "balance";

    @Override
    public void handle (CashMachine machine, String request) {
        if (!request.equalsIgnoreCase(COMMAND_NAME)) {
            if (nextHandler != null) {
                nextHandler.handle(machine, request);
            }
            return;
        }

        Account account = machine.getCurrentAccount();
        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        System.out.println("Balance: " + account.getBalance());
    }
}
