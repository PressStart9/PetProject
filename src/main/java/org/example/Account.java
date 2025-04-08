package org.example;

import org.example.Exceptions.InvalidAmountException;
import org.example.Exceptions.NotEnoughMoneyException;

public class Account {
    private final String accountNumber;
    private final String ownerName;
    private double balance;

    /**
     * Deposits the specified amount into the account.
     *
     * @param amount the amount to be deposited
     * @return the new balance after the deposit
     * @throws InvalidAmountException if the amount is less than zero
     */
    public double deposit(double amount) throws InvalidAmountException {
        if (amount < 0) {
            throw new InvalidAmountException("Amount must be greater than zero.");
        }

        balance += amount;

        return balance;
    }

    /**
     * Withdraws the specified amount from the account.
     *
     * @param amount the amount to be withdrawn
     * @return the new balance after the withdrawal
     * @throws InvalidAmountException if the amount is less than zero
     * @throws NotEnoughMoneyException if the amount exceeds the current balance
     */
    public double withdraw(double amount) throws InvalidAmountException, NotEnoughMoneyException {
        if (amount < 0) {
            throw new InvalidAmountException("Amount must be greater than zero.");
        }

        if (amount > balance) {
            throw new NotEnoughMoneyException("Amount must be greater than zero.");
        }

        balance -= amount;

        return balance;
    }

    /**
     * Constructs a new Account with the specified account number, owner name, and initial balance.
     *
     * @param accountNumber the account number
     * @param ownerName the name of the account owner
     * @param balance the initial balance of the account
     */
    public Account(String accountNumber, String ownerName, double balance) {
        this.accountNumber = accountNumber;
        this.ownerName = ownerName;
        this.balance = balance;
    }

    /**
     * Returns the current balance of the account.
     *
     * @return the current balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Returns the account number.
     *
     * @return the account number
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Returns the name of the account owner.
     *
     * @return the owner name
     */
    public String getOwnerName() {
        return ownerName;
    }
}