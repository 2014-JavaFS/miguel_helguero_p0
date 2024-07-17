package org.revature.Bank.Account;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.revature.Bank.util.exceptions.InvalidAccountTypeException;
import org.revature.Bank.util.exceptions.NegativeDepositException;
import org.revature.Bank.util.exceptions.UserNotFoundException;
import org.revature.Bank.util.interfaces.Controller;
import static org.revature.Bank.BankFrontController.logger;
import java.text.NumberFormat;
import java.util.List;

public class AccountController implements Controller{
        private final AccountService accountService;

        public AccountController(AccountService accountService) {
            this.accountService = accountService;
        }


        @Override
        public void registerPaths(Javalin app) {
            app.get("/accounts", this::getAccountsById);
            app.post("/accounts/create", this::postAccount);
            app.post("/accounts/deposit", this::postDeposit);
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
                    logger.info("\nAccount Id: {}\nAccount Type: {}\nAccount Balance: {}", account.getAccountId(), account.getAccountType(), numberFormat.format(account.getBalance()));
                }
                ctx.json(accounts);
                ctx.status(200);
            } catch(UserNotFoundException e){
                logger.warn(e.getMessage());
                ctx.status(404);
            }
        }

        public void postAccount(Context ctx){
            // uses environment variable userId in postman to insert an account with the userId into accounts table
            logger.info("Creating account...");
            String accountType = ctx.queryParam("accountType");
            int userId = Integer.parseInt(ctx.header("userId"));
            logger.info("UserId {}, AccountType {}, {}", userId, accountType, "was sent in through path parameter.");
            try {
                Account createdAccount = accountService.createAccount(userId, accountType);
                ctx.json(createdAccount);
                logger.info("Account created: {}", createdAccount);
                ctx.status(HttpStatus.CREATED);
           } catch(InvalidAccountTypeException e){
                logger.warn(e.getMessage());
                ctx.status(400);
            }

        }

        public void postDeposit(Context ctx){
            // uses environment variable userId and queryParams account type and deposit to update balance in accounts table
            logger.info("Making deposit...");
            String accountType = ctx.queryParam("accountType");
            double depositAmount = Double.parseDouble(ctx.queryParam("depositAmount"));
            int userId = Integer.parseInt(ctx.header("userId"));
            logger.info("UserId {}, AccountType {}, deposit amount {} {}", userId, accountType, depositAmount, "was sent in through path parameter.");

            try{
                Account depositAccount = accountService.deposit(userId, accountType, depositAmount);
                if(depositAccount == null) {
                    logger.warn("The user does not have that type of account.");
                    ctx.status(400);
                    return;
                }
                logger.info("Deposit successful!\nAccount Id: {}\nAccount Type: {}\nAccount Balance: {}", depositAccount.getAccountId(), depositAccount.getAccountType(), depositAccount.getBalance());
                ctx.json(depositAccount);
                ctx.status(200);
            } catch(NegativeDepositException | InvalidAccountTypeException e){
                logger.warn(e.getMessage());
                ctx.status(400);
            }
        }
}
