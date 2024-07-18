package org.revature.Bank.util.interfaces;

public interface CrudableAccount<O>{
    O create(O o);

    O deposit(O o, double depositAmount);

    O withdraw(O o, double withdrawalAmount);

    boolean delete();
}
