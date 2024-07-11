package org.revature.Bank.User;

import org.revature.Bank.util.exceptions.*;
import org.revature.Bank.util.interfaces.ScannerValidator;
import java.util.Scanner;
import java.text.NumberFormat;

public class UserController {
    public Scanner scanner;
    private final UserService userService;
    public UserController(Scanner scanner, UserService userService) {
        this.scanner=scanner;
        this.userService=userService;
    }
    /**
     * returns whether the user input was an int.
     */
    ScannerValidator anyInt = (scanner, errorMessage) ->{
        if(!scanner.hasNextInt()){
            System.out.println(errorMessage);
            scanner.nextLine();
            return false;
        }
        return true;
    };

    /**
     * returns whether the user input was a double.
     */
    ScannerValidator anyDouble = (scanner, errorMessage) ->{
        if(!scanner.hasNextDouble()){
            System.out.println(errorMessage);
            scanner.nextLine();
            return false;
        }
        return true;
    };


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
        NumberFormat numbeFormatter = NumberFormat.getCurrencyInstance();

        do {
            System.out.println("Welcome "+userLoggedIn.getEmail()+"!");
            System.out.println("1. View Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Logout");
            System.out.println();
            System.out.println("Enter your numeric choice from above: ");

            if(!anyInt.isValid(scanner, "Invalid Input, please enter a number 1-4.")) continue;


            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // view balance
                    System.out.println("Your balance is: " + numbeFormatter.format(userLoggedIn.getBalance()));
                    break;
                case 2:
                    // deposit
                    System.out.println("Enter the amount you want to deposit: ");
                    if(!anyDouble.isValid(scanner, "Invalid Input, please enter a number.")) continue;
                    double depositAmount = scanner.nextDouble();
                    scanner.nextLine();

                    try {
                        userService.deposit(userLoggedIn, depositAmount);
                        System.out.println("Deposit successful!");
                    } catch(NegativeDepositException e){
                        e.printStackTrace();
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    // withdraw, make sure withdrawal amount is not more than balance
                    System.out.println("Enter the amount you want to withdraw: ");
                    if(!anyDouble.isValid(scanner, "Invalid Input, please enter a number.")) continue;
                    double withdrawAmount = scanner.nextDouble();
                    scanner.nextLine();

                    try{
                        userService.withdraw(userLoggedIn, withdrawAmount);
                        System.out.println("Withdrawal successful!");
                    } catch(OverdraftException e){
                        e.printStackTrace();
                        System.out.println(e.getMessage());
                    }

                    break;
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
                    System.out.println("Invalid Input, please enter a number 1-4.");
            }
        } while(choice!= 4);
        return null;

    }
}
