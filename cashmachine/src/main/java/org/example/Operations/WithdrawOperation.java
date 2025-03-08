package org.example.Operations;

import org.example.Account;

import java.time.LocalDateTime;

public class WithdrawOperation implements Operation {
    private final LocalDateTime date;
    private final Account account;
    private final double amount;

    public WithdrawOperation(Account account, double amount) {
        this.date = LocalDateTime.now();
        this.account = account;
        this.amount = amount;
    }

    @Override
    public LocalDateTime getDate() {
        return this.date;
    }

    @Override
    public String getString() {
        return "Withdraw from " + account.getOwnerName() + " amount of " + amount + " at " + date.toString();
    }
}
