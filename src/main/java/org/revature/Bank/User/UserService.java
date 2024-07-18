package org.revature.Bank.User;

import org.revature.Bank.util.exceptions.*;
import java.util.List;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * Generates a List of User objects after invoking userRepository.findAll(), and throws a UserNotFoundException if
     * no users were found in the Users table, otherwise it returns the List of Users.
     * @return users - generated List of Users from the rows in the Users table.
     */
    public List<User> findAll(){
        try {
            List<User> users = userRepository.findAll();
            if (users.isEmpty()) {
                throw new UserNotFoundException("No users are registered.");
            }
            return users;
        } catch(UserNotFoundException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * Takes in a User object and passes it to validateUser(), if no exceptions caught then
     * inserts it into Users table.
     *
     * @param user - Initialized User object with email, and password retrieved from Postman POST body.
     */
    public User registerUser(User user){
        try {
            validateUser(user);
            user = userRepository.create(user);
        } catch (InvalidInputException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return user;

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

        if(!(user.getPassword().length() >= 8 && user.getPassword().length()<=64))
            throw new InvalidInputException("Password must be between 8 and 64 characters");


    }

    /**
     * Takes in an email and password and searches List of User objects for a matching User,
     * which is returned if found and if not, null is returned.
     * @param email - Entered email String.
     * @param password - Entered password String.
     * @return - User object found in List of Users, or null if none found.
     */
    public User login(String email, String password) throws LoginException{
        User foundUser = userRepository.findByEmailAndPassword(email, password);
        if(foundUser == null) throw new LoginException("No user with those credentials was found.");
        return foundUser;
    }





}
