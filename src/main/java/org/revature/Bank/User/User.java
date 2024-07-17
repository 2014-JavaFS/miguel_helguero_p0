package org.revature.Bank.User;

import org.revature.Bank.Account.Account;

import java.util.List;

public class User {
    private int userId;
    private String email;
    private String password;

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





    public User(){}

    public User(String email, String password){
        this.email=email;
        this.password=password;
    }

    @Override
    public String toString(){
        return "\nUser ID: "+ getUserId()+"\nEmail: " + getEmail()+"\n";
    }
}

