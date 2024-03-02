import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Prompt the user to enter a valid account number
        String accountNumber;
        do {
            System.out.print("Enter account number: ");
            accountNumber = scanner.nextLine();
        } while (!isValidAccountNumber(accountNumber));

        System.out.print("Enter account holder name: ");
        String accountHolderName = scanner.nextLine();

        // Retrieve the account from the database
        BankAccount account = BankAccount.getByAccountNumberAndHolderName(accountNumber, accountHolderName);

        // Check if the account exists and the account holder name matches
        if (account != null) {
            // Menu for banking operations
            int choice;
            do {
                System.out.println("1. Deposit");
                System.out.println("2. Withdraw");
                System.out.println("3. Check Balance");
                System.out.println("4. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        System.out.print("Enter amount to deposit: ");
                        double depositAmount = scanner.nextDouble();
                        account.deposit(depositAmount);
                        break;
                    case 2:
                        System.out.print("Enter amount to withdraw: ");
                        double withdrawAmount = scanner.nextDouble();
                        account.withdraw(withdrawAmount);
                        break;
                    case 3:
                        double balance = account.checkBalance();
                        System.out.println("Current Balance: " + balance);
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number from 1 to 4.");
                }
            } while (choice != 4);
        } else {
            System.out.println("Invalid account number or account holder name.");
        }

        scanner.close();
    }

    // Method to validate the account number format
    private static boolean isValidAccountNumber(String accountNumber) {
        // Check if the account number consists of digits only and has a specific length
        if (accountNumber.matches("\\d{11}")) {
            return true;
        } else {
            System.out.println("Invalid account number format. Please enter a valid 11-digit account number.");
            return false;
        }
    }
}
