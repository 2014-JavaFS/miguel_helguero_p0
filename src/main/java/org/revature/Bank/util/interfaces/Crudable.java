package org.revature.Bank.util.interfaces;

import org.revature.Bank.User.User;

public interface Crudable<O> extends Serviceable<O>{
    O create(O o);

    O findByEmailAndPassword(String email, String password);

    boolean deposit(User user, double deposit);

    boolean withdraw(User user, double amount);

    boolean delete();

}
