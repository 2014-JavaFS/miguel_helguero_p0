package org.revature.Bank.Account;

import org.revature.Bank.User.User;
import org.revature.Bank.util.ConnectionFactory;
import org.revature.Bank.util.exceptions.UserNotFoundException;
import org.revature.Bank.util.interfaces.Crudable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.revature.Bank.BankFrontController.logger;

public class AccountRepository implements Crudable<Account>{

    private static final Logger log = LoggerFactory.getLogger(AccountRepository.class);

    /**
     * Executes a SELECT query to retrieve all rows in Users table convert it into a List of User objects which is then
     * returned.
     * @return users - Returns the List of User objects retrieved from the Users table.
     */
    @Override
    public List<Account> findAll() {
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()){
            List<Account> accounts = new ArrayList<>();

            String sql = "select * from accounts";
            ResultSet rs = conn.createStatement().executeQuery(sql);


            while(rs.next()){
                accounts.add(generateAccountFromResultSet(rs));
            }

            return accounts;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public Account generateAccountFromResultSet(ResultSet resultSet) throws SQLException {
        Account account = new Account();
        // FIXME: result set is getting correct account_id, make sure Account object is setting it as its accountId

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

                ResultSet resultSet = preparedStatement.executeQuery();
                if(resultSet.next()){
                    accountToCreate.setAccountId(resultSet.getInt("account_id"));
                } else{
                    throw new SQLException("account_id not generated.");
                }

                logger.info(preparedStatement.toString());
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
    public boolean deposit(String email, double amount) {
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()) {
            String sql = "update users set balance = (" +
                    "select balance from users " +
                    "where email = ?" +
                    ") + ? where email = ? ";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);

            preparedStatement.setString(1, email);
            preparedStatement.setDouble(2, amount);
            preparedStatement.setString(3, email);

            logger.info(preparedStatement.toString());

            if (preparedStatement.executeUpdate() == 0) {
                throw new RuntimeException("Deposit failed.");
            }
            return true;
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
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


