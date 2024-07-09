package org.revature.Bank.util;

@FunctionalInterface
public interface InputValidator {
    boolean isValid(String input, String errorMessage);
}
