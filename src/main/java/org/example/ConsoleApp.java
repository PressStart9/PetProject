package org.example;

import org.example.Handlers.*;

import java.util.Scanner;

public class ConsoleApp {
    public static void main(String[] args) {
        CommandHandler commandHandler = new CreateAccountHandler()
                .setNextHandler(new SelectAccountHandler())
                .setNextHandler(new ViewBalanceHandler())
                .setNextHandler(new WithdrawHandler())
                .setNextHandler(new DepositHandler())
                .setNextHandler(new ViewHistoryHandler());

        CashMachine cashMachine = new CashMachine();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Enter command (create, select, balance, withdraw, deposit, history, exit):");
            String command = scanner.nextLine();

            if ("exit".equalsIgnoreCase(command)) {
                break;
            }

            commandHandler.handle(cashMachine, command);
        }

        scanner.close();
    }
}