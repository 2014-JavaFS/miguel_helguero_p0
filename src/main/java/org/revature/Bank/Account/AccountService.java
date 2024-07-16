package org.revature.Bank.Account;

import org.revature.Bank.User.User;
import org.revature.Bank.User.UserRepository;
import org.revature.Bank.util.exceptions.AccountNotFoundException;
import org.revature.Bank.util.exceptions.InvalidAccountTypeException;
import org.revature.Bank.util.exceptions.UserNotFoundException;

import java.util.List;

import static org.revature.Bank.BankFrontController.logger;

public class AccountService {
    private AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public List<Account> findAccounts(int userId) throws UserNotFoundException{
        List<Account> accounts = accountRepository.findByUserId(userId);

        return accounts;
    }

    public Account createAccount(int userId, String accountType) throws InvalidAccountTypeException{

            if (!accountType.equals("checkings") && !accountType.equals("savings") && !accountType.equals("investment"))
                throw new InvalidAccountTypeException("Account type can only be checkings, savings, or investment");

            // TODO: dont create account of a type that already exists
            Account accountToCreate = new Account(userId, accountType);
            return accountRepository.create(accountToCreate);

    }
}
