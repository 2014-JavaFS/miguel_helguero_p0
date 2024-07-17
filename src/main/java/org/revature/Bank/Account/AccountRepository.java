package org.revature.Bank.Account;

import org.revature.Bank.util.ConnectionFactory;
import org.revature.Bank.util.interfaces.CrudableAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.revature.Bank.BankFrontController.logger;

public class AccountRepository implements CrudableAccount<Account> {

    private static final Logger log = LoggerFactory.getLogger(AccountRepository.class);


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

    public List<Account> findByUserId(int user_id){
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()){
            List<Account> accounts = new ArrayList<>();

            String sql = "select * from accounts where user_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, user_id);

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                accounts.add(generateAccountFromResultSet(resultSet));
            }

            return accounts;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

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

    @Override
    public boolean withdraw(String email, double amount){
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()){
            String sql = "update users set balance = ("+
                    "select balance from users " +
                    "where email = ?" +
                    ") - ? where email = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, email);
            preparedStatement.setDouble(2, amount);
            preparedStatement.setString(3, email);

            logger.info(preparedStatement.toString());
            if (preparedStatement.executeUpdate() == 0) {
                throw new RuntimeException("Deposit failed.");
            }
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }
    @Override
    public boolean delete() {
        return false;
    }


}


