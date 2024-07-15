package org.revature.Bank.Account;

import org.revature.Bank.User.User;
import org.revature.Bank.User.UserRepository;
import org.revature.Bank.util.exceptions.AccountNotFoundException;
import org.revature.Bank.util.exceptions.UserNotFoundException;

import java.util.List;

public class AccountService {
    private AccountRepository accountRepository;


    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    public List<Account> findAccounts(int userId) throws UserNotFoundException{
        List<Account> accounts = accountRepository.findByUserId(userId);

        return accounts;
    }
}
