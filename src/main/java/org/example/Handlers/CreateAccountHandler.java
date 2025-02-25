package org.example.Handlers;

import org.example.Account;
import org.example.AccountBuilder;
import org.example.CashMachine;

public class CreateAccountHandler extends CommandHandler {
    private static final String COMMAND_NAME = "create";

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
        System.out.println("Enter owner name:");
        String ownerName = scanner.nextLine();

        Account newAccount = new AccountBuilder()
                .setAccountNumber(accountNumber)
                .setOwnerName(ownerName)
                .build();

        machine.setCurrentAccount(machine.addAccount(newAccount));
        System.out.println("Success.");
    }
}
