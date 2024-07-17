package org.revature.Bank.util.interfaces;

public interface CrudableAccount<O>{
    O create(O o);

    O deposit(O o, double depositAmount);

    boolean withdraw(String email, double amount);

    boolean delete();

}
