package org.revature.Bank.Account;

import java.text.NumberFormat;

public class Account {
    private int accountId;
    private int userId;
    private String accountType;
    private double balance;
    private NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String type) {
        this.accountType = type;
    }

    public int getAccountId(){return accountId;}

    public void setAccountId(int accountId){this.accountId=accountId;}

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
            this.balance = balance;
        }



    public Account(){}

    public Account(int userId, String accountType){
        this.userId = userId;
        this.accountType=accountType;
        this.balance = 0.00;
    }

    @Override
    public String toString(){
        return "Account Id: " + getAccountId() + "\nUser ID: " + getUserId()+ "\nAccount Type: " + getAccountType()+"\nAccount Balance: " + numberFormat.format(getBalance())+"\n";
    }
}



