package org.example;

public class AccountBuilder {
    private String accountNumber;
    private String ownerName;
    private double balance = 0.0;

    /**
     * Sets the account number for the account being built.
     *
     * @param accountNumber the account number to set
     * @return the AccountBuilder instance for method chaining
     */
    public AccountBuilder setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
        return this;
    }

    /**
     * Sets the owner name for the account being built.
     *
     * @param ownerName the owner name to set
     * @return the AccountBuilder instance for method chaining
     */
    public AccountBuilder setOwnerName(String ownerName) {
        this.ownerName = ownerName;
        return this;
    }

    /**
     * Sets the initial balance for the account being built.
     *
     * @param balance the initial balance to set
     * @return the AccountBuilder instance for method chaining
     */
    public AccountBuilder setBalance(double balance) {
        this.balance = balance;
        return this;
    }

    /**
     * Builds and returns an Account instance with the specified properties.
     *
     * @return a new Account instance
     */
    public Account build() {
        return new Account(accountNumber, ownerName, balance);
    }
}
