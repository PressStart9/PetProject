package org.example.Handlers;

import org.example.Account;
import org.example.CashMachine;
import org.example.Operations.DepositOperation;
import org.example.Operations.Operation;

public class DepositHandler extends CommandHandler {
    private static final String COMMAND_NAME = "deposit";

    @Override
    public void handle(CashMachine machine, String request) {
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

        System.out.println("Enter amount:");
        double amount = scanner.nextDouble();
        try {
            account.deposit(amount);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        Operation op = new DepositOperation(account, amount);
        machine.addOperation(op);
    }
}
