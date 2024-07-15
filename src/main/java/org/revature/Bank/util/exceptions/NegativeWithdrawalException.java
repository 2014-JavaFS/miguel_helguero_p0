package org.revature.Bank.util.exceptions;

public class NegativeWithdrawalException extends Exception{
    public NegativeWithdrawalException(String message){
        super(message);
    }
}
