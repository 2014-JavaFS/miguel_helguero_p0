package org.revature.Bank;

import io.javalin.Javalin;
import org.revature.Bank.Account.Account;
//import org.revature.Bank.Account.AccountController;
import org.revature.Bank.Account.AccountController;
import org.revature.Bank.Account.AccountRepository;
import org.revature.Bank.Account.AccountService;
import org.revature.Bank.User.UserController;
import org.revature.Bank.User.UserRepository;
import org.revature.Bank.User.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BankFrontController {
    public static final Logger logger = LoggerFactory.getLogger(BankFrontController.class);

    public static void main(String[] args){
        logger.info("Beryl Bank is up and running");
        Javalin app = Javalin.create();

        UserRepository userRepository = new UserRepository();
        UserService userService = new UserService(userRepository);
        UserController userController = new UserController(userService);
        userController.registerPaths(app);

        AccountRepository accountRepository = new AccountRepository();
        AccountService accountService = new AccountService(accountRepository);
        AccountController accountController = new AccountController(accountService);
        accountController.registerPaths(app);

        app.start(8080);

    }
}