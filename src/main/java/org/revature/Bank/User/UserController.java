package org.revature.Bank.User;

import org.revature.Bank.util.exceptions.InvalidInputException;


import java.util.Scanner;

public class UserController {
    public Scanner scanner;
    private final UserService userService;
    public UserController(Scanner scanner, UserService userService) {
        this.scanner=scanner;
        this.userService=userService;
    }

    InputValidator validEmail = (email, errorMessage) -> {
        // regex ensures local part and domain part are separated by a '@' and contain valid email characters
        // and entire string is between 7 and 64 characters
        String emailRegexPattern = "^(?=.{7,64}$)[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if(!email.matches(emailRegexPattern)){
            throw new InvalidInputException(errorMessage);
        }
    };

    InputValidator validPassword = (password, errorMessage) ->{
        if(!(password.length() >= 8 && password.length()<=64)){
            throw new InvalidInputException(errorMessage);
        }
    };

    public void register() throws InvalidInputException {
        System.out.println("Please enter your email: ");
        String email = scanner.next();
        System.out.println();
        // TODO: verify no duplicate emails already in Users table


        System.out.println("Please enter your password: ");
        String password = scanner.next();
        System.out.println();

        User userToAdd = new User(email, password);

        userService.registerUser(userToAdd);

        System.out.println("Registration complete!");



    }
}
