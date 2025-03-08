package org.example;

import org.example.Operations.Operation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a cash machine that manages accounts and operations.
 */
public class CashMachine {
    private final Map<String, Account> accounts;
    private final List<Operation> operationHistory;
    private Account currentAccount;

    /**
     * Constructs a new CashMachine instance.
     */
    public CashMachine() {
        this.accounts = new HashMap<>();
        this.operationHistory = new ArrayList<>();
    }

    /**
     * Adds an account to the cash machine.
     *
     * @param account the account to add
     * @return the previous account associated with the same account number, or null if there was no mapping
     */
    public Account addAccount(Account account) {
        return accounts.put(account.getAccountNumber(), account);
    }

    /**
     * Adds an operation to the operation history.
     *
     * @param operation the operation to add
     */
    public void addOperation(Operation operation) {
        operationHistory.add(operation);
    }

    /**
     * Retrieves an account by its account number.
     *
     * @param accountNumber the account number to search for
     * @return the account associated with the account number, or null if no account is found
     */
    public Account getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }

    /**
     * Sets the current account for the cash machine.
     *
     * @param account the account to set as current
     */
    public void setCurrentAccount(Account account) {
        currentAccount = account;
    }

    /**
     * Returns the current account.
     *
     * @return the current account
     */
    public Account getCurrentAccount() {
        return currentAccount;
    }

    /**
     * Returns the operation history.
     *
     * @return the list of operations performed
     */
    public final List<Operation> getOperationHistory() {
        return operationHistory;
    }
}
