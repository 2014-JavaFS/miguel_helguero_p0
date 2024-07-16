package org.revature.Bank.Account;

// ctx header for staying logged in, sep branch for Account model integration with deposit/withdraw and validation/ Account model will have attrib type for checking, saving, retirement, investment, etc
// one User can have multiple Accounts(savings, checkings, etc);
// validation of deposit not negative, no overdrawn withdrawals etc handled in AccountService
public class Account {
    private int accountId;
    private int userId;
    private String accountType;
    private double balance;

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

    public void setAccountId(int id){this.accountId=accountId;}

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
        return "\nUser ID: " + getUserId()+ "\nAccount Type: " + getAccountType()+"\nBalance: " + getBalance()+"\n";
    }
}



