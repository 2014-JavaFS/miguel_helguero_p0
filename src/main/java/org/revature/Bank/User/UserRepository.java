package org.revature.Bank.User;
import org.revature.Bank.util.ConnectionFactory;
import org.revature.Bank.util.exceptions.UserNotFoundException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.revature.Bank.BankFrontController.logger;

public class UserRepository{

    /**
     * Executes a SELECT query to retrieve all rows in Users table convert it into a List of User objects which is then
     * returned.
     * @return users - Returns the List of User objects retrieved from the Users table.
     */

    public List<User> findAll() {
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()){
            List<User> users = new ArrayList<>();

            String sql = "select * from users";
            ResultSet rs = conn.createStatement().executeQuery(sql);


            while(rs.next()){
                users.add(generateUserFromResultSet(rs));
            }
            if(users.isEmpty()){
                logger.warn("No users found");
                throw new UserNotFoundException("No users found");
            }
            return users;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        } catch(UserNotFoundException e){
            logger.warn("No users were found.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates and returns a User object with the user_id and email received in the Postman response.
     * @param rs - ResultSet with the user_id and email
     * @return user - newly created User object
     * @throws SQLException - Thrown if exception encountered when executing query.
     */
    public User generateUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setEmail(rs.getString("email"));
        return user;
    }

    /**
     * Inserts validated User object called used into database, throws RuntimeException if INSERT query not executed.
     * @param user - Validated User object with email and password
     * @return user - returns the Validated User object after it has been inserted into the Users table.
     */
    public User create(User user){
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()){
            String sql = "insert into users(email, password) values(?, ?) returning user_id";
            int userId;
            // sanitize sql insert statements before executing
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                userId = resultSet.getInt("user_id");
                user.setUserId(userId);
            }else{
                throw new SQLException("User Id was not retrieved.");
            }

            return user;
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public User findByEmailAndPassword(String email, String password){
        User user = new User();
        try(Connection conn = ConnectionFactory.getConnectionFactory().getConnection()){
            String sql = "select user_id, email from users where email = ? and password = ?";

            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                user.setUserId(resultSet.getInt("user_id"));
                user.setEmail(resultSet.getString("email"));
            }
            else{
                return null;
            }
        } catch(SQLException e){
            e.printStackTrace();
            return null;
        }
        return user;
    }
}
