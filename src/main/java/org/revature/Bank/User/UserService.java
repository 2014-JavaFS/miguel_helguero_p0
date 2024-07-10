package org.revature.Bank.User;

import org.revature.Bank.util.exceptions.InvalidInputException;

public class UserService {

    public void registerUser(User user) throws InvalidInputException {
        validateUser(user);
    }

    public void validateUser(User user) throws InvalidInputException{
        String emailRegexPattern = "^(?=.{2,254}$)[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if(!user.getEmail().matches(emailRegexPattern)){
            throw new InvalidInputException("Email address must be between 2 and 254 characters");
        }

        if(!(user.getPassword().length() >= 8 && user.getPassword().length()<=64)){
            throw new InvalidInputException("Password must be between 8 and 64 characters");
        }

    }
}
