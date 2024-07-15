package org.revature.Bank.Account;

// ctx header for staying logged in, sep branch for Account model integration with deposit/withdraw and validation/ Account model will have attrib type for checking, saving, retirement, investment, etc
// one User can have multiple Accounts(savings, checkings, etc);
// validation of deposit not negative, no overdrawn withdrawals etc handled in AccountService
public class Account {
    private int accountId;
    private int userId;
    private String type;
    private double balance;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Account(String type){
        this.type=type;
        this.balance = 0.00;
    }

    @Override
    public String toString(){
        return "Account: "+ getType()+" " + getBalance()+"\n";
    }
}



