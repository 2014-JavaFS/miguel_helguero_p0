package org.revature.Bank.User;

import org.revature.Bank.util.InputValidator;


import java.util.Scanner;

public class UserController {
    public Scanner scanner;
    private final UserService userService;
    public UserController(Scanner scanner, UserService userService) {
        this.scanner=scanner;
        this.userService=userService;
    }

    InputValidator validEmail = (email, errorMessage) -> {
        String emailRegexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if(!email.matches(emailRegexPattern)){
            System.out.println(errorMessage);
            scanner.nextLine();
            return false;
        }
        return true;
    };

    public void register(){
        System.out.println("Please enter your email: ");
        String email = scanner.next();
        System.out.println();

        if(!validEmail.isValid(email, "Please enter a valid email address.")) return;


    }
}
