package org.revature.Bank.util.exceptions;

public class OverdraftException extends Exception {
    public OverdraftException(String message){
        super(message);
    }
}
