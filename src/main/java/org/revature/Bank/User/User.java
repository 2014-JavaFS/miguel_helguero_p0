package org.revature.Bank.User;

public class User {
    private int id;
    private String email;
    private String password;
    private double balance;



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
}

