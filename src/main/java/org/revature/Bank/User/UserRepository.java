package org.revature.Bank.User;
import org.revature.Bank.util.ConnectionFactory;
import org.revature.Bank.util.exceptions.InvalidInputException;
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
    public boolean update(User user) {
        return false;
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
                //TODO: put this code into a generateUserFromResultSet method for viewing logged in user's info in findById, that returns a User
                User user = new User();
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
