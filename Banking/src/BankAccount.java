import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BankAccount {
    private int id;
    private String accountNumber;
    private String accountHolderName;
    private double balance;

    public void save() {
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO accounts (account_number, account_holder_name, balance) VALUES (?, ?, ?)")) {
            statement.setString(1, accountNumber);
            statement.setString(2, accountHolderName);
            statement.setDouble(3, balance);
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    id = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Failed to get the generated id.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static BankAccount getByAccountNumberAndHolderName(String accountNumber, String accountHolderName) {
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT * FROM accounts WHERE account_number = ? AND account_holder_name = ?")) {
            statement.setString(1, accountNumber);
            statement.setString(2, accountHolderName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                BankAccount account = new BankAccount();
                account.accountNumber = resultSet.getString("account_number");
                account.accountHolderName = resultSet.getString("account_holder_name");
                account.balance = resultSet.getDouble("balance");
                account.id = resultSet.getInt("id");
                return account;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateBalanceInDatabase() {
        try (Connection connection = DatabaseManager.getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "UPDATE accounts SET balance = ? WHERE id = ?")) {
            statement.setDouble(1, balance);
            statement.setInt(2, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deposit(double amount) {
        balance += amount;
        updateBalanceInDatabase();
    }

    public void withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            updateBalanceInDatabase();
        } else {
            System.out.println("Insufficient balance");
        }
    }

    public double checkBalance() {
        return balance;
    }
}
