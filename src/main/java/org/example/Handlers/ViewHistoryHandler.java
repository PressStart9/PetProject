package org.example.Handlers;

import org.example.CashMachine;
import org.example.Operations.Operation;

public class ViewHistoryHandler extends CommandHandler {
    private static final String COMMAND_NAME = "history";

    @Override
    public void handle(CashMachine machine, String request) {
        if (!request.equalsIgnoreCase(COMMAND_NAME)) {
            if (nextHandler != null) {
                nextHandler.handle(machine, request);
            }
            return;
        }

        for (Operation op : machine.getOperationHistory()) {
            System.out.println(op.getString());
        }
    }
}
