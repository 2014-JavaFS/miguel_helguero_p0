package org.revature.Bank.User;
import org.revature.Bank.util.ConnectionFactory;
import org.revature.Bank.util.interfaces.Crudable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements Crudable<User>{
    @Override
    public User create(User user){
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()){



            String sql = "insert into users(email, password) values(?, ?)";

            // sanitze sql insert statements before executing
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());

            if(preparedStatement.executeUpdate() == 0){
                throw new RuntimeException("User was not inserted into database.");
            }

            return user;
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public User findByEmailAndPassword(String email, String password){
        User user = new User();
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()){
            String sql = "select id, email, balance from users where email = ? and password = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {

                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setBalance(resultSet.getDouble("balance"));

            }
            else{
                return null;
            }
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
        return user;
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


            if (preparedStatement.executeUpdate() == 0) {
                throw new RuntimeException("Deposit failed.");
            }
            return true;
        }catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
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

    @Override
    public List<User> findAll() {
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()){
            List<User> users = new ArrayList<>();

            String sql = "select * from users";
            ResultSet rs = conn.createStatement().executeQuery(sql);

            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setBalance(rs.getDouble("balance"));


                users.add(user);
            }

            return users;
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }
}
