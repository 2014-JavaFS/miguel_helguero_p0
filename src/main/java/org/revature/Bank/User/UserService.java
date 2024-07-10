package org.revature.Bank.User;

import org.revature.Bank.util.exceptions.*;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private List<User> userList = new ArrayList<>();

    public List<User> getUserList() {
        return userList;
    }

    /**
     * Takes in a User object and passes it to validateUser(), if no exceptions caught then
     * adds it to List of Users.
     *
     * @param user - Initialized User object with id, email, and password.
     * @throws InvalidInputException - Thrown if email or password do not meet requirements.
     */
    public void registerUser(User user) throws InvalidInputException {
        validateUser(user);
        userList.add(user);
    }


    /**
     * Takes in a User object and validates the email and password based on constraints in the database.
     *
     * @param user - Initialized User object with id, email, and password.
     * @throws InvalidInputException - Thrown if email or password do not meet requirements.
     */
    public void validateUser(User user) throws InvalidInputException{
        if(!(user.getEmail().length() >= 2 && user.getEmail().length()<=254))
            throw new InvalidInputException("Email address must be between 2 and 254 characters");

        String emailRegexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if(!user.getEmail().matches(emailRegexPattern))
            throw new InvalidInputException("Please enter a valid email address");

        // TODO: check for spaces in password input
        if(!(user.getPassword().length() >= 8 && user.getPassword().length()<=64))
            throw new InvalidInputException("Password must be between 8 and 64 characters");


    }

    /**
     * Takes in an email and password and searches List of User objects for a matching User,
     * which is returned if found and if not, null is returns.
     * @param email - Entered email String.
     * @param password - Entered password String.
     * @return - User object found in List of Users, or null if none found.
     */
    public User login(String email, String password){

        for(User u : userList)
            if(u.getEmail().equals(email) && u.getPassword().equals(password)) return u;

        return null;
    }

    public User logout(User userLoggedIn) throws LogoutException {
        if(userLoggedIn == null) throw new LogoutException("No user is logged in.");
        return null;
    }
}
