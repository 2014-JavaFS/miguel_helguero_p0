package org.revature.Bank.util.exceptions;

import org.revature.Bank.User.User;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String message){
        super(message);
    }
}
