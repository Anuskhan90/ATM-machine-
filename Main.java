import java.util.*;

class Account {
    String accountNumber;
    String pin;
    double balance;
    LinkedList<String> transactionHistory;
    
    public Account(String accNum, String pin, double initialBalance) {
        this.accountNumber = accNum;
        this.pin = pin;
        this.balance = initialBalance;
        this.transactionHistory = new LinkedList<>();
    }
}

class ATM {
    private HashMap<String, Account> accounts;
    private Queue<String> transactionQueue;
    private int[] cashInventory; 
    
    public ATM() {
        accounts = new HashMap<>();
        transactionQueue = new LinkedList<>();
        cashInventory = new int[]{10, 20, 50};
    }
    
    
    public void initializeSampleAccounts() {
        accounts.put("123456", new Account("123456", "1234", 10000.0));
        accounts.put("654321", new Account("654321", "4321", 5000.0));
    }
    
    public boolean authenticate(String accNum, String pin) {
        Account acc = accounts.get(accNum);
        return acc != null && acc.pin.equals(pin);
    }
    
    public double checkBalance(String accNum) {
        return accounts.get(accNum).balance;
    }
    
    public boolean withdraw(String accNum, int amount) {
        Account acc = accounts.get(accNum);
        if (acc.balance >= amount && dispenseCash(amount)) {
            acc.balance -= amount;
            acc.transactionHistory.addFirst("Withdrawal: -" + amount);
            return true;
        }
        return false;
    }
    
    private boolean dispenseCash(int amount) {
        int remaining = amount;
        int[] tempInventory = cashInventory.clone();
        int[] denominations = {1000, 500, 100};
        
        for (int i = 0; i < denominations.length; i++) {
            int notesNeeded = remaining / denominations[i];
            int notesAvailable = Math.min(notesNeeded, tempInventory[i]);
            remaining -= notesAvailable * denominations[i];
            tempInventory[i] -= notesAvailable;
            
            if (remaining == 0) break;
        }
        
        if (remaining == 0) {
            cashInventory = tempInventory;
            return true;
        }
        return false;
    }
    
    public void deposit(String accNum, int amount) {
        Account acc = accounts.get(accNum);
        acc.balance += amount;
        acc.transactionHistory.addFirst("Deposit: +" + amount);
        updateCashInventory(amount);
    }
    
    public List<String> getTransactionHistory(String accNum) {
        return accounts.get(accNum).transactionHistory;
    }
    
    private void updateCashInventory(int amount) {
        
        cashInventory[2] += amount / 100;
    }
    
    public void printCashInventory() {
        System.out.println("ATM Cash Inventory:");
        System.out.println("1000 notes: " + cashInventory[0]);
        System.out.println("500 notes: " + cashInventory[1]);
        System.out.println("100 notes: " + cashInventory[2]);
    }
}

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ATM atm = new ATM();
        atm.initializeSampleAccounts();
        
        System.out.println("Welcome to the ATM Simulation");
        
        
        System.out.print("Enter account number: ");
        String accNum = scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();
        
        if (!atm.authenticate(accNum, pin)) {
            System.out.println("Authentication failed. Invalid account number or PIN.");
            return;
        }
        
        System.out.println("\nAuthentication successful!");
        
        
        while (true) {
            System.out.println("\nMain Menu:");
            System.out.println("1. Check Balance");
            System.out.println("2. Withdraw Cash");
            System.out.println("3. Deposit Money");
            System.out.println("4. View Transaction History");
            System.out.println("5. View ATM Cash Inventory");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); 
            
            switch (choice) {
                case 1:
                    System.out.println("Current Balance: " + atm.checkBalance(accNum));
                    break;
                    
                case 2:
                    System.out.print("Enter amount to withdraw: ");
                    int withdrawAmount = scanner.nextInt();
                    scanner.nextLine(); 
                    if (atm.withdraw(accNum, withdrawAmount)) {
                        System.out.println("Please take your cash.");
                    } else {
                        System.out.println("Withdrawal failed. Insufficient balance or ATM out of cash.");
                    }
                    break;
                    
                case 3:
                    System.out.print("Enter amount to deposit: ");
                    int depositAmount = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    atm.deposit(accNum, depositAmount);
                    System.out.println("Deposit successful.");
                    break;
                    
                case 4:
                    System.out.println("Transaction History:");
                    List<String> history = atm.getTransactionHistory(accNum);
                    for (String transaction : history) {
                        System.out.println(transaction);
                    }
                    break;
                    
                case 5:
                    atm.printCashInventory();
                    break;
                    
                case 6:
                    System.out.println("Thank you for using our ATM. Goodbye!");
                    return;
                    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
