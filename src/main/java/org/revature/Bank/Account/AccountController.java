package org.revature.Bank.Account;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.revature.Bank.util.exceptions.*;
import org.revature.Bank.util.interfaces.Controller;
import static org.revature.Bank.BankFrontController.logger;
import java.text.NumberFormat;
import java.util.List;

public class AccountController implements Controller{
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    private final NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

    /**
     * Creates connections to the specified paths so that Postman can execute queries to the database through them.
     * @param app - Javalin instance
     */
    @Override
    public void registerPaths(Javalin app) {
        app.get("/accounts", this::getAccountsById);
        app.post("/accounts/create", this::postAccount);
        app.patch("/accounts/deposit", this::patchDeposit);
        app.patch("/accounts/withdraw", this::patchWithdraw);
    }

    /**
     * Retrieves a List of User objects from the Users table and sends back account_id, user_id, account_type, and balance as the response.
     *
     * @param ctx - Current context.
     */
    public void getAccountsById(Context ctx) {
        StringBuilder stringBuilder = new StringBuilder();
        logger.info("Accessing getAccountsById...");



        try{
            int userId = Integer.parseInt(ctx.header("userId"));
            logger.info("UserId {}, {}", userId, "was sent in through path parameter.");
            List<Account> accounts = accountService.findAccounts(userId);

            if(accounts==null){
                ctx.result("That user has no accounts.");
                logger.info("That user has no accounts.");
                ctx.status(200);
                return;
            }

            logger.info("Accounts for UserId = {}:", userId);
            for(Account account: accounts){
                stringBuilder.append("Account Id: ").append(account.getAccountId()).append("\nAccount Type: ").append(account.getAccountType()).append("\nAccount Balance: ").append(numberFormat.format(account.getBalance())).append("\n\n");
                logger.info("\nAccount Id: {}\nAccount Type: {}\nAccount Balance: {}", account.getAccountId(), account.getAccountType(), numberFormat.format(account.getBalance()));
            }

            ctx.result(stringBuilder.toString());
            ctx.status(200);
        } catch(UserNotFoundException e){
            logger.warn(e.getMessage());
            ctx.result(e.getMessage());
            ctx.status(404);
        } catch (NumberFormatException e){
            ctx.result("No user is logged in.");
            ctx.status(400);
        }
    }

    /**
     * Uses Postman environment variable userIdand query paremeter accountType to insert a new account into the accounts
     * table with the corresponding userId, if the user does not have an account of that type already.
     * @param ctx - Current context.
     */
    public void postAccount(Context ctx){
        logger.info("Creating account...");
        String accountType = ctx.queryParam("accountType");


        try {
            int userId = Integer.parseInt(ctx.header("userId"));
            logger.info("UserId {}, AccountType {}, {}", userId, accountType, "was sent in through path parameter.");
            Account createdAccount = accountService.createAccount(userId, accountType);

            ctx.result("Account created!\n" + createdAccount);
            ctx.status(HttpStatus.CREATED);
       } catch(InvalidAccountTypeException e){
            logger.warn(e.getMessage());
            ctx.status(400);
        } catch (NumberFormatException e){
            ctx.result("No user is logged in.");
            ctx.status(400);
        }

    }

    /**
     * Uses Postman environment variable userId and queryParams accountType and depositAmount to update the balance of
     * the corresponding account in the accounts table.
     * @param ctx - Current context.
     */
    public void patchDeposit(Context ctx){
        // uses environment variable userId and queryParams account type and deposit to update balance in accounts table
        logger.info("Making deposit...");
        String accountType = ctx.queryParam("accountType");


        try{
            double depositAmount = Double.parseDouble(ctx.queryParam("depositAmount"));
            int userId = Integer.parseInt(ctx.header("userId"));
            logger.info("UserId {}, AccountType {}, deposit amount {} {}", userId, accountType, depositAmount, "was sent in through path parameter.");
            Account depositAccount = accountService.deposit(userId, accountType, depositAmount);

            if(depositAccount == null) {
                logger.warn("The user does not have that type of account.");
                ctx.status(400);
                return;
            }

            ctx.result("Deposit successful!\n" + depositAccount);
            ctx.status(200);
        } catch(NegativeDepositException | InvalidAccountTypeException e){
            logger.warn(e.getMessage());
            ctx.result(e.getMessage());
            ctx.status(400);
        } catch(NumberFormatException e){
            logger.warn(e.getMessage());
            if(e.getMessage().equals("empty String")) ctx.result("No deposit amount given.");
            else if(e.getMessage().equals("For input string: \"null\"")) ctx.result("No user is logged in.");
            ctx.status(400);
        }
    }

    /**
     * uses Postman environment variable userId and query parameters accountType and withdrawalAmount to withdraw from
     * the corresponding account in the accounts table.
     * @param ctx - Current context.
     */
    public void patchWithdraw(Context ctx){
        logger.info("Making withdrawal...");
        String accountType = ctx.queryParam("accountType");


        try{
            double withdrawalAmount = Double.parseDouble(ctx.queryParam("withdrawalAmount"));
            int userId = Integer.parseInt(ctx.header("userId"));
            logger.info("UserID {}, AccountType {}, withdrawal amount {} {}", userId, accountType, withdrawalAmount, "was sent in through path parameter");
            Account withdrawAccount = accountService.withdraw(userId, accountType, withdrawalAmount);

            ctx.result("Withdrawal successful!\n" + withdrawAccount);
            ctx.status(200);
        } catch(OverdraftException | InvalidAccountTypeException | NegativeWithdrawalException e){
            logger.warn(e.getMessage());
            ctx.result(e.getMessage());
            ctx.status(400);
        } catch (NumberFormatException e){
            if(e.getMessage().equals("empty String")) ctx.result("No withdrawal amount given.");
            else if(e.getMessage().equals("For input string: \"null\"")) ctx.result("No user is logged in.");
            ctx.status(400);
        }
    }
}
