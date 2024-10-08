package org.revature.Bank.Account;

import org.revature.Bank.util.ConnectionFactory;
import org.revature.Bank.util.interfaces.AccountInterface;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.revature.Bank.BankFrontController.logger;

public class AccountRepository implements AccountInterface<Account> {
    public Account generateAccountFromResultSet(ResultSet resultSet) throws SQLException {
        Account account = new Account();

        account.setAccountId(resultSet.getInt("account_id"));
        account.setUserId(resultSet.getInt("user_id"));
        account.setAccountType(resultSet.getString("account_type"));
        account.setBalance(resultSet.getDouble("balance"));
        return account;
    }

    /**
     * Inserts validated Account object called used into database, throws RuntimeException if INSERT query not executed.
     * @param accountToCreate - Validated Account object with accountType and userId, gets accountId after query executed.
     * @return account - returns the Validated Account object after it has been inserted into the Accounts table.
     */
    @Override
    public Account create(Account accountToCreate){
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()){

            String sql = "insert into accounts(account_type, user_id) values(?, ?) returning account_id";

            // sanitize sql insert statements before executing
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, accountToCreate.getAccountType());
            preparedStatement.setInt(2, accountToCreate.getUserId());
            logger.info(preparedStatement.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                accountToCreate.setAccountId(resultSet.getInt("account_id"));
            } else{
                throw new SQLException("account_id not generated.");
            }

            return accountToCreate;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Searches accounts table for all accounts that belong to the corresponding user_id and returns a List of Account
     * objects filled with data of each account row found. Returns null if none found.
     * @param userId - int userId used in the query.
     * @return - List<Account> with one account object for each row found in accounts table that have the corresponding
     * userId.
     */
    public List<Account> findByUserId(int userId){
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()){
            List<Account> accounts = new ArrayList<>();
            boolean noAccounts = true;
            String sql = "select * from accounts where user_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                noAccounts = false;
                accounts.add(generateAccountFromResultSet(resultSet));
            }
            if(noAccounts){
                return null;
            }

            return accounts;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Finds the balance of the accounts row with the corresponding user_id and account_type. Throws a RuntimeException
     * if none found.
     * @param userId - int userId used in the query.
     * @param accountType - String accountType used in the query.
     * @return - The balance of the account found.
     */
    public double findByUserIdAndAccountType(int userId, String accountType){
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()){
            String sql = "select balance from accounts where user_id = ? and account_type = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, accountType);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return resultSet.getDouble("balance");
            } else{
                throw new RuntimeException("Balance not found.");
            }
        } catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Increases the balance of the corresponding account. Throws a RuntimeException if query unsuccessful.
     * @param depositAccount - Account; The account to be deposited in.
     * @param depositAmount - Double; The amount to increase the balance.
     * @return depositAccount - Updated Account object with current balance.
     */
    @Override
    public Account deposit(Account depositAccount, double depositAmount) {
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()) {
            String sql = "update accounts set balance = balance + ? where user_id = ? and account_type = ? returning account_id, balance";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setDouble(1, depositAmount);
            preparedStatement.setInt(2, depositAccount.getUserId());
            preparedStatement.setString(3, depositAccount.getAccountType());
            logger.info(preparedStatement.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                depositAccount.setAccountId(resultSet.getInt("account_id"));
                depositAccount.setBalance(resultSet.getDouble("balance"));
            }
            else{
                throw new RuntimeException("Deposit failed.");
            }

            return depositAccount;
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Decreases the balance of the corresponding account. Throws a Runtime exception if query unsuccessful.
     * @param withdrawAccount Account; The account to be withdrawn from.
     * @param withdrawalAmount - Double; The amount to decrease the balance.
     * @return withdrawAccount - Updated Account object with current balance.
     */
    @Override
    public Account withdraw(Account withdrawAccount, double withdrawalAmount){
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()){
            String sql = "update accounts set balance = balance - ? where user_id = ? and account_type = ? returning account_id, balance";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setDouble(1, withdrawalAmount);
            preparedStatement.setInt(2, withdrawAccount.getUserId());
            preparedStatement.setString(3, withdrawAccount.getAccountType());
            logger.info(preparedStatement.toString());

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                withdrawAccount.setAccountId(resultSet.getInt("account_id"));
                withdrawAccount.setBalance(resultSet.getDouble("balance"));
            } else{
                throw new RuntimeException("Withdrawal failed.");
            }
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }

        return withdrawAccount;
    }


}


