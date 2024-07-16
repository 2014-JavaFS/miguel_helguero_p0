package org.revature.Bank.Account;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.revature.Bank.User.User;
import org.revature.Bank.User.UserService;
import org.revature.Bank.util.exceptions.LoginException;
import org.revature.Bank.util.exceptions.UserNotFoundException;
import org.revature.Bank.util.interfaces.Controller;

import javax.security.auth.login.AccountNotFoundException;

import static org.revature.Bank.BankFrontController.logger;
import java.text.NumberFormat;
import java.util.List;

import static org.revature.Bank.BankFrontController.logger;

public class AccountController implements Controller{
        private final AccountService accountService;

        public AccountController(AccountService accountService) {
            this.accountService = accountService;
        }


        @Override
        public void registerPaths(Javalin app) {
            app.get("/accounts", this::getAccountsById);
        }

        /**
         * Retrieves a List of User objects from the Users table and sends it back as the json response.
         *
         * @param ctx - Current context.
         */
        public void getAccountsById(Context ctx) {
            NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
            logger.info("Accessing getAccountsById...");
            int userId = Integer.parseInt(ctx.header("userId"));
            logger.info("UserId {}, {}", userId, "was sent in through path parameter.");

            try{
                List<Account> accounts = accountService.findAccounts(userId);
                if(accounts == null){
                    logger.info("That user has no accounts.");
                    ctx.status(204);
                    return;
                }
                logger.info("Accounts for UserId = {}:", userId);
                for(Account account: accounts){
                    logger.info("{} balance: {}", account.getAccountType(), numberFormat.format(account.getBalance()));
                }
                ctx.json(accounts);
                ctx.status(200);
            } catch(UserNotFoundException e){
                logger.warn(e.getMessage());
                ctx.status(404);
            }
        }

        //TODO: implement method to create desired type of account for User

}
