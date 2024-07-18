package org.revature.Bank.util.interfaces;

import org.revature.Bank.util.exceptions.InvalidInputException;

import java.util.List;

public interface UserInterface<O>{
    List<O> findAll();

    O create(O o) throws InvalidInputException;

    O findByEmailAndPassword(String email, String password);
}
