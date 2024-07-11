package org.revature.Bank.util.interfaces;

import org.revature.Bank.User.User;

import java.sql.SQLException;

public interface Crudable<O> extends Serviceable<O>{
    O create(O o);

    boolean update(O o);
    boolean delete();

}
