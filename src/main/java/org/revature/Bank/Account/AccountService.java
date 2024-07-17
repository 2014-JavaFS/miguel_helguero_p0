package org.revature.Bank.Account;

import org.revature.Bank.User.User;
import org.revature.Bank.User.UserRepository;
import org.revature.Bank.util.ConnectionFactory;
import org.revature.Bank.util.exceptions.AccountNotFoundException;
import org.revature.Bank.util.exceptions.InvalidAccountTypeException;
import org.revature.Bank.util.exceptions.UserNotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.revature.Bank.BankFrontController.logger;

public class AccountService {
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public List<Account> findAccounts(int userId) throws UserNotFoundException{
        List<Account> accounts = accountRepository.findByUserId(userId);

        return accounts;
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
}
