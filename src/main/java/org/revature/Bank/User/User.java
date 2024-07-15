package org.revature.Bank.User;

import org.revature.Bank.Account.Account;

import java.util.List;

public class User {
    private int userId;
    private String email;
    private String password;
    private double balance;
    private List<Account> accounts;

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public int getUserId(){return userId;}

    public void setUserId(int user_id){this.userId=user_id;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }



    public User(){}

    public User(String email, String password){
        this.email=email;
        this.password=password;
        this.balance = 0.00;
    }

    @Override
    public String toString(){
        return "User: "+ getEmail()+" " + getBalance()+"\n";
    }
}

