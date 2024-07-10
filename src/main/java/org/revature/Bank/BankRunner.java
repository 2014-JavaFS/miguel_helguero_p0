package org.revature.Bank;

import org.revature.Bank.User.User;
import org.revature.Bank.User.UserController;
import org.revature.Bank.User.UserService;
import org.revature.Bank.util.exceptions.InvalidInputException;

import java.util.Scanner;


public class BankRunner {
    public static void main(String[] args) {
        int choice = 0;
        Scanner scanner = new Scanner(System.in);
        User userLoggedIn = new User();
        UserService userService = new UserService();
        UserController userController = new UserController(scanner, userService);

        do{
            System.out.println("Welcome to Beryl Bank!");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Logout");
            System.out.println("4. Exit");
            System.out.println();
            System.out.println("Enter your numeric choice from above: ");

            if(!scanner.hasNextInt()){
                System.out.println("Invalid input, please enter a number 1-3.");
                scanner.nextLine();
                continue;
            }


            choice = scanner.nextInt();

            switch (choice) {
                case 1: // If choice == 1
                    System.out.println("Logging in....");
                    break; //include break, otherwise it will fall through to the next case statement
                case 2:
                    System.out.println("Registering a new account...");
                    try {
                        userController.register();
                    } catch(InvalidInputException e){
                        e.printStackTrace();
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Logged out");
                    userLoggedIn = null;
                case 4:
                    System.out.println("Thanks for visiting Beryl Bank, have a nice day!");
                    break;
                default:
                    System.out.println("Invalid Input, Please enter a number 1-4.");

            }
        } while(choice !=4);
    }
}