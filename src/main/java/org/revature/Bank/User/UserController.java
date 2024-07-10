package org.revature.Bank.User;

import org.revature.Bank.util.exceptions.InvalidInputException;
import org.revature.Bank.util.exceptions.LoginException;
import org.revature.Bank.util.exceptions.LogoutException;


import java.util.Scanner;

public class UserController {
    public Scanner scanner;
    private final UserService userService;
    public UserController(Scanner scanner, UserService userService) {
        this.scanner=scanner;
        this.userService=userService;
    }

    /**
     * register method retrieves user input for email and password, then passes User object
     * into userService.registerUser()
     *
     *
     * @throws InvalidInputException
     */
    public void register() throws InvalidInputException {
        System.out.println("Please enter your email: ");
        String email = scanner.next();
        System.out.println();
        // TODO: verify no duplicate emails already in Users table


        System.out.println("Please enter your password(8 - 64 characters): ");
        String password = scanner.next();
        System.out.println();

        User userToAdd = new User(email, password);

        userService.registerUser(userToAdd);
        System.out.println("Registration complete!");
    }

    /**
     * Takes in a User object and asks user for email and password, which are passed to UserService.login()
     * and returns the corresponding User object, or throws LoginException if none found.
     *
     * @param userLoggedIn - Initialized User object with email and password
     * @return - returns the User object in userService.userList with the corresponding email and password
     */
    public User login(User userLoggedIn) throws LoginException {
        if(userLoggedIn != null) {
            throw new LoginException("A user is already logged in.");
        } else if(userService.getUserList().isEmpty()){
            throw new LoginException("There are no registered users.");
        }
        System.out.println("Enter email: ");
        String email = scanner.next();
        System.out.println();

        System.out.println("Enter password: ");
        String password = scanner.next();
        System.out.println();

        userLoggedIn =userService.login(email,password);
        if( userLoggedIn!= null) {
            return userLoggedIn;
        }
        throw new LoginException("No user with those credentials found.") ;
    }

    /**
     * Contains options to view the user's balance, withdraw, deposit, or log out.
     *
     * @param userLoggedIn - User object that has logged in.
     * @param scanner - Scanner object to retrieve user input.
     * @param userService - handles user input validation and returns requested balance.
     */
    public User loggedIn(User userLoggedIn, Scanner scanner, UserController userController, UserService userService){
        int choice = 0;


        do {
            System.out.println("Welcome "+userLoggedIn.getEmail()+"!");
            System.out.println("1. View Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Logout");
            System.out.println();
            System.out.println("Enter your numeric choice from above: ");

            if (!scanner.hasNextInt()) {
                System.out.println("Invalid input, please enter a number 1-4.");
                scanner.nextLine();
                continue;
            }


            choice = scanner.nextInt();

            switch (choice) {
                case 4:
                    try {
                        userLoggedIn = userService.logout(userLoggedIn);
                        System.out.println("Logged out.");
                    } catch (LogoutException e) {
                        e.printStackTrace();
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    System.out.println("Invalid Input, Please enter a number 1-4.");
            }
        } while(choice!= 4);
        return null;

    }


}
