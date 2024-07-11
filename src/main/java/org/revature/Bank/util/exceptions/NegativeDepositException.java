package org.revature.Bank.util.exceptions;

public class NegativeDepositException extends Exception{
    public NegativeDepositException(String message){
        super(message);
    }
}
