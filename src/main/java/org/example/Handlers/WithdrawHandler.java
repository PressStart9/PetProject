package org.example.Handlers;

import org.example.Account;
import org.example.CashMachine;
import org.example.Operations.Operation;
import org.example.Operations.WithdrawOperation;

public class WithdrawHandler extends CommandHandler {
    private static final String COMMAND_NAME = "withdraw";

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
            account.withdraw(amount);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        Operation op = new WithdrawOperation(account, amount);
        machine.addOperation(op);
    }
}
