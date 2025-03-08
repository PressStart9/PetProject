import org.example.*;
import org.example.Exceptions.*;
import org.example.Operations.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BankMachineTests {
    @Test
    public void testAccountCreation() {
        Account account = new Account("123", "John Doe", 1000.0);
        assertEquals("123", account.getAccountNumber());
        assertEquals("John Doe", account.getOwnerName());
        assertEquals(1000.0, account.getBalance());
    }

    @Test
    public void testDeposit() throws InvalidAmountException {
        Account account = new Account("123", "John Doe", 1000.0);
        double newBalance = account.deposit(500.0);
        assertEquals(1500.0, newBalance);
    }

    @Test
    public void testDepositInvalidAmount() {
        Account account = new Account("123", "John Doe", 1000.0);
        assertThrows(InvalidAmountException.class, () -> account.deposit(-500.0));
    }

    @Test
    public void testWithdraw() throws InvalidAmountException, NotEnoughMoneyException {
        Account account = new Account("123", "John Doe", 1000.0);
        double newBalance = account.withdraw(300.0);
        assertEquals(700.0, newBalance);
    }

    @Test
    public void testWithdrawInvalidAmount() {
        Account account = new Account("123", "John Doe", 1000.0);
        assertThrows(InvalidAmountException.class, () -> account.withdraw(-300.0));
    }

    @Test
    public void testWithdrawNotEnoughMoney() {
        Account account = new Account("123", "John Doe", 1000.0);
        assertThrows(NotEnoughMoneyException.class, () -> account.withdraw(1500.0));
    }

    @Test
    public void testAccountBuilder() {
        Account account = new AccountBuilder()
                .setAccountNumber("456")
                .setOwnerName("Jane Doe")
                .setBalance(2000.0)
                .build();
        assertEquals("456", account.getAccountNumber());
        assertEquals("Jane Doe", account.getOwnerName());
        assertEquals(2000.0, account.getBalance());
    }

    @Test
    public void testCashMachineAddAccount() {
        CashMachine cashMachine = new CashMachine();
        Account account = new Account("789", "Alice", 500.0);
        cashMachine.addAccount(account);
        assertEquals(account, cashMachine.getAccount("789"));
    }

    @Test
    public void testCashMachineOperationHistory() {
        CashMachine cashMachine = new CashMachine();
        Account account = new Account("789", "Alice", 500.0);
        cashMachine.addAccount(account);
        cashMachine.setCurrentAccount(account);

        Operation depositOp = new DepositOperation(account, 200.0);
        cashMachine.addOperation(depositOp);

        assertEquals(1, cashMachine.getOperationHistory().size());
        assertEquals(depositOp, cashMachine.getOperationHistory().getFirst());
    }

    @Test
    public void testDepositOperation() {
        Account account = new Account("123", "John Doe", 1000.0);
        Operation operation = new DepositOperation(account, 500.0);
        assertTrue(operation.getString().contains("Deposit to John Doe amount of 500.0"));
    }

    @Test
    public void testWithdrawOperation() {
        Account account = new Account("123", "John Doe", 1000.0);
        Operation operation = new WithdrawOperation(account, 300.0);
        assertTrue(operation.getString().contains("Withdraw from John Doe amount of 300.0"));
    }

    @Test
    public void testInvalidAmountException() {
        InvalidAmountException exception = new InvalidAmountException("Amount must be greater than zero.");
        assertEquals("Amount must be greater than zero.", exception.getMessage());
    }
}