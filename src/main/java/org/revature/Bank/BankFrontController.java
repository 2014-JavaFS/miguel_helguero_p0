package org.revature.Bank;

import io.javalin.Javalin;
import org.revature.Bank.User.UserController;
import org.revature.Bank.User.UserRepository;
import org.revature.Bank.User.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BankFrontController {
    public static final Logger logger = LoggerFactory.getLogger(BankFrontController.class);

    public static void main(String[] args){
        logger.info("Beryl Bank is up and running");
        Javalin app = Javalin.create();

        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);
        UserController userController = new UserController(userService);
        userController.registerPaths(app);

        app.start(8080);
//        do{
//            System.out.println("Welcome to Beryl Bank!");
//            System.out.println("1. Login");
//            System.out.println("2. Register");
//            System.out.println("3. See all users");
//            System.out.println("4. Exit");
//            System.out.println();
//            System.out.println("Enter your numeric choice from above: ");
//
//            if(!scanner.hasNextInt()){
//                System.out.println("Invalid input, please enter a number 1-4.");
//                scanner.nextLine();
//                continue;
//            }
//
//            choice = scanner.nextInt();
//
//            switch (choice) {
//                case 1:
//                    System.out.println("Logging in....");
//                    try {
//                        userLoggedIn = userController.login(userLoggedIn);
//                        System.out.println(userLoggedIn.getEmail() + " login successful!");
//                        userLoggedIn = userController.loggedIn(userLoggedIn, scanner, userController, userService);
//
//                    } catch(LoginException e){
//                        e.printStackTrace();
//                        System.out.println(e.getMessage());
//                    }
//                    break;
//                case 2:
//                    System.out.println("Registering a new account...");
//                    try {
//                        userController.register();
//                    } catch(InvalidInputException e){
//                        e.printStackTrace();
//                        System.out.println(e.getMessage());
//                    }
//                    break;
//                case 3:
//                    userController.getUserInfo();
//                    break;
//                case 4:
//                    System.out.println("Thanks for visiting Beryl Bank, have a nice day!");
//                    break;
//                default:
//                    System.out.println("Invalid Input, Please enter a number 1-4.");
//            }
//        } while(choice !=4);
    }
}