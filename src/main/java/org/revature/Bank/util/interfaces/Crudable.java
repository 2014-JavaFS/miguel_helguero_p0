package org.revature.Bank.util.interfaces;

import org.revature.Bank.User.User;

public interface Crudable<O> extends Serviceable<O>{
    O create(O o);

    O findByEmailAndPassword(String email, String password);

    //TODO: change param to String email and replace implementation's User object with email
    boolean deposit(String email, double deposit);

    boolean withdraw(String email, double amount);

    boolean delete();

}
