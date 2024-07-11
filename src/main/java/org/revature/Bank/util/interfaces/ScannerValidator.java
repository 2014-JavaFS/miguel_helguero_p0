package org.revature.Bank.util.interfaces;
import java.util.Scanner;

@FunctionalInterface
public interface ScannerValidator {
    boolean isValid(Scanner scanner, String errorMessage);
}
