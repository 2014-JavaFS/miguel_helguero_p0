package org.revature.Bank.User;

import org.revature.Bank.util.exceptions.*;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private UserRepository userRepository;


    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

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
     * adds it to List of Users.
     *
     * @param user - Initialized User object with id, email, and password.
     * @throws InvalidInputException - Thrown if email or password do not meet requirements.
     */
    public User registerUser(User user){
        try {
            validateUser(user);
            userRepository.create(user);
        } catch (InvalidInputException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return user;

    }

    /**
     * Takes in a User object and a double depositAmount and adds the deposit to the User's balance.
     * Throws a NegativeDepositException if the double was negative.
     * @param userLoggedIn - User object that references the user that is currently logged in.
     * @param depositAmount - Double amount to be deposited.
     * @throws NegativeDepositException - Thrown if deposit amount is negative.
     */
    public User deposit(User userLoggedIn, double depositAmount) throws UpdateException, NegativeDepositException {
        double currentBalance = userLoggedIn.getBalance();

        if(depositAmount<0) throw new NegativeDepositException("Deposit cannot be negative.");
        if(!userRepository.deposit(userLoggedIn.getEmail(), depositAmount)){
            throw new UpdateException("Deposit failed.");
        }

        userLoggedIn.setBalance(currentBalance + depositAmount);
        return userLoggedIn;
    }


    /**
     * Takes in a User object and a double withdrawalAmount and subtracts the withdrawal amount from the user's balance.
     * Throws an OverdraftException is the withdrawal amount is greater than the current balance.
     * @param userLoggedIn - User object that references the user that is currently logged in.
     * @param withdrawalAmount - Double amount to be withdrawn.
     * @throws OverdraftException - Thrown if withdrawal amount is greater than current balance.
     */
    public void withdraw(User userLoggedIn, double withdrawalAmount) throws OverdraftException, NegativeWithdrawalException, UpdateException{
        double currentBalance = userLoggedIn.getBalance();

        if(withdrawalAmount > currentBalance) throw new OverdraftException("Withdrawal amount cannot be greater than current balance.");
        if(withdrawalAmount < 0) throw new NegativeWithdrawalException("Withdrawal amount cannot be negative.");
        if(!userRepository.withdraw(userLoggedIn.getEmail(), withdrawalAmount)){
            throw new UpdateException("Withdrawal failed.");
        }
        userLoggedIn.setBalance(currentBalance - withdrawalAmount);
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
     * which is returned if found and if not, null is returns.
     * @param email - Entered email String.
     * @param password - Entered password String.
     * @return - User object found in List of Users, or null if none found.
     */
    public User login(String email, String password){
        return userRepository.findByEmailAndPassword(email, password);
    }

    public void logout(User userLoggedIn) throws LogoutException {
        if(userLoggedIn == null) throw new LogoutException("No user is logged in.");
    }




}
