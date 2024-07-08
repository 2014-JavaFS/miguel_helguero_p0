package org.revature.Bank;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int choice = 0;
        Scanner scanner = new Scanner(System.in);

        do{
            System.out.println("Welcome to Beryl Bank!");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.println();
            System.out.println("Enter your numeric choice from above: ");

            if(!scanner.hasNextInt()){
                System.out.println("Invalid input, please enter a number 1-3.");
                scanner.nextLine();
                continue;
            }

            choice = scanner.nextInt();

            switch (choice) {
                case 1: // If choice == 1
                    System.out.println("Logging in....");
                    break; //include break, otherwise it will fall through to the next case statement
                case 2:
                    System.out.println("Registering a new account...");
                    break;
                case 3:
                    System.out.println("Thanks for visiting Beryl Bank, have a nice day!");
                    break;
                default:
                    System.out.println("Invalid Input, Please enter a number 1-3.");

            }
        } while(choice !=3);
    }
}