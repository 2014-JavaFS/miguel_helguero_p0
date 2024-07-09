package org.revature.Bank;

import org.revature.Bank.User.User;
import org.revature.Bank.User.UserController;
import org.revature.Bank.User.UserService;

import java.util.Scanner;

// use AuthenticationException if entered email/password combo not found in db
// this file determines where ur going
// controller takes in info, passes Flight obj into service with the info, which does stuff with the info(create, find, check if balance would go below 0)
// /Bank/util/auth/AuthController tries to login, takes in a Member
// auth/AuthService asks for email and password, passes to findByEmailAndPassword() in Member service(was passed into this service class), if successful the stores the Member obj in
// Main's memberLoggedIn obj
// JavaDocs: used to define what a method/class is doing

/**
 * keep short and sweet
 * ex:
 * Update method takes in a Member obj w/the updated info, searches the list for a matching Member.id.
 * Once found, replaces the old obj with the updated Member obj.
 * If no Member.id is matched, throw an exception(shown below).
 *
 * @param updateMember - an existing member with updated info based on request // this will show when hover over Member class's .update() in code
 * @throws DataNotFoundException - Member.id provided doesn't match with anything in the db
 *
 * ex: on top of public Member findByEmailAndPassword(...):
 * Searches the datbase for info where email and password provided must be equal to a record within the database
 *
 * @param email - String
 * @param password - String
 * @return - Member object, if not member found it will return null
 */
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
                    userController.register();
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