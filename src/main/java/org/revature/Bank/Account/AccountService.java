package org.revature.Bank.Account;

import org.revature.Bank.User.User;
import org.revature.Bank.User.UserRepository;
import org.revature.Bank.util.ConnectionFactory;
import org.revature.Bank.util.exceptions.AccountNotFoundException;
import org.revature.Bank.util.exceptions.InvalidAccountTypeException;
import org.revature.Bank.util.exceptions.NegativeDepositException;
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

    public Account deposit(int userId, String accountType, double depositAmount) throws NegativeDepositException, InvalidAccountTypeException {
        if (depositAmount < 0) throw new NegativeDepositException("Deposit cannot be negative.");
        if(!userHasAccountType(userId, accountType)) throw new InvalidAccountTypeException("The user does not have that account type.");

        Account depositAccount = new Account(userId, accountType);
        return accountRepository.deposit(depositAccount, depositAmount);
    }


//    public User deposit(User userLoggedIn, double depositAmount){
//        double currentBalance = userLoggedIn.getBalance();
//        try {
//            if (depositAmount < 0) throw new NegativeDepositException("Deposit cannot be negative.");
//            if (!userRepository.deposit(userLoggedIn.getEmail(), depositAmount)) {
//                throw new UpdateException("Deposit failed.");
//            }
//
//            userLoggedIn.setBalance(currentBalance + depositAmount);
//            return userLoggedIn;
//        } catch(UpdateException | NegativeDepositException e){
//            logger.warn("Deposit failed.");
//            logger.warn(e.getMessage());
//        }
//        return null;
//    }
//
//
//    /**
//     * Takes in a User object and a double withdrawalAmount and subtracts the withdrawal amount from the user's balance.
//     * Throws an OverdraftException is the withdrawal amount is greater than the current balance.
//     * @param userLoggedIn - User object that references the user that is currently logged in.
//     * @param withdrawalAmount - Double amount to be withdrawn.
//     * @throws OverdraftException - Thrown if withdrawal amount is greater than current balance.
//     */
//    public void withdraw(User userLoggedIn, double withdrawalAmount) throws OverdraftException, NegativeWithdrawalException, UpdateException{
//        double currentBalance = userLoggedIn.getBalance();
//
//        if(withdrawalAmount > currentBalance) throw new OverdraftException("Withdrawal amount cannot be greater than current balance.");
//        if(withdrawalAmount < 0) throw new NegativeWithdrawalException("Withdrawal amount cannot be negative.");
//        if(!userRepository.withdraw(userLoggedIn.getEmail(), withdrawalAmount)){
//            throw new UpdateException("Withdrawal failed.");
//        }
//        userLoggedIn.setBalance(currentBalance - withdrawalAmount);
//    }

}
