package org.revature.Bank.Account;


import org.revature.Bank.util.ConnectionFactory;
import org.revature.Bank.util.exceptions.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public List<Account> findAccounts(int userId) throws UserNotFoundException{
        return accountRepository.findByUserId(userId);
    }

    public Account createAccount(int userId, String accountType) throws InvalidAccountTypeException{

            if (!accountType.equals("checkings") && !accountType.equals("savings") && !accountType.equals("investment"))
                throw new InvalidAccountTypeException("Account type can only be checkings, savings, or investment");
            if(userHasAccountType(userId, accountType)) throw new InvalidAccountTypeException("The user already has that account type.");

            Account accountToCreate = new Account(userId, accountType);
            return accountRepository.create(accountToCreate);

    }

    private boolean userHasAccountType(int userId, String accountType){
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()) {
            String sql = "select * from accounts where user_id = ? and account_type = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, accountType);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();

        } catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    public Account deposit(int userId, String accountType, double depositAmount) throws NegativeDepositException, InvalidAccountTypeException, AccountNotFoundException {
        if(accountType.isEmpty()) throw new InvalidAccountTypeException("No account type given.");

        if (!accountType.equals("checkings") && !accountType.equals("savings") && !accountType.equals("investment"))
            throw new InvalidAccountTypeException("Account type can only be checkings, savings, or investment");

        if(!userHasAccountType(userId, accountType)) throw new AccountNotFoundException("The user does not have that account type.");

        if (depositAmount < 0) throw new NegativeDepositException("Deposit cannot be negative.");

        Account depositAccount = new Account(userId, accountType);
        return accountRepository.deposit(depositAccount, depositAmount);
    }

    public Account withdraw(int userId, String accountType, double withdrawalAmount) throws NegativeWithdrawalException, InvalidAccountTypeException, OverdraftException, AccountNotFoundException {
        if(accountType.isEmpty()) throw new InvalidAccountTypeException("No account type given.");

        if (!accountType.equals("checkings") && !accountType.equals("savings") && !accountType.equals("investment"))
            throw new InvalidAccountTypeException("Account type can only be checkings, savings, or investment");

        if(withdrawalAmount < 0) throw new NegativeWithdrawalException("Withdrawal amount cannot be negative.");

        if(!userHasAccountType(userId, accountType)) throw new AccountNotFoundException("The user does not have that account type.");

        if(accountRepository.findByUserIdAndAccountType(userId, accountType) < withdrawalAmount) throw new OverdraftException("Withdrawal amount cannot be greater than balance.");

        Account withdrawAccount = new Account(userId, accountType);
        return accountRepository.withdraw(withdrawAccount, withdrawalAmount);

    }
}
