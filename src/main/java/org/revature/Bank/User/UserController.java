package org.revature.Bank.User;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.revature.Bank.util.exceptions.*;
import org.revature.Bank.util.interfaces.Controller;
import java.util.List;
import java.text.NumberFormat;



import static org.revature.Bank.BankFrontController.logger;

public class UserController implements Controller {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Override
    public void registerPaths(Javalin app) {
        app.get("/users", this::getAllUsers);
        app.post("/users", this::postNewUser);
        app.get("/balance/{email}/{password}", this::getBalanceByEmailAndPassword);
        app.get("/user/{email}/{password}", this::login);
    }

    /**
     * Retrieves a List of User objects from the Users table and sends it back as the json response.
     *
     * @param ctx - Current context.
     */
    public void getAllUsers(Context ctx) {
        logger.info("Accessing getAllUsers");
        List<User> users = userService.findAll();
        logger.info("All users found, converting to json...");
        ctx.json(users);
        logger.info("Users {}", users);
        logger.info("Sending back to user...");
        ctx.status(200);
    }

    /**
     * Receives the json body with the email and password fields and executes an INSERT query to insert a new User row
     * into the Users table.
     *
     * @param ctx - Current Context.
     */
    public void postNewUser(Context ctx) {
        // checks body for info to map into a User obj
        User user = ctx.bodyAsClass(User.class);
        logger.info("Creating user...");
        ctx.json(userService.registerUser(user)); // response is the created object
        ctx.status(HttpStatus.CREATED);
        logger.info("User created: {}", user);
    }

    /**
     * Sends an email and password as parameters to userService.findBalance() and eventually the email and password are
     * used in a SELECT query that retrieves the corresponding user's balance.
     *
     * @param ctx - Current Context.
     */
    public void getBalanceByEmailAndPassword(Context ctx) {
        NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
        logger.info("Accessing getBalanceById...");
        String email = ctx.pathParam("email");
        String password = ctx.pathParam("password");
        logger.info("Email {}, Password {}, {}", email, password, "was sent in through path parameter.");
        try {
            double balance = userService.findBalance(email, password);
            logger.info("The balance is : {}", numberFormatter.format(balance));
            ctx.json(balance);
        } catch (UserNotFoundException e) {
            logger.warn("The user was not found");
            ctx.status(HttpStatus.CREATED);
        }
    }


    /**
     * Sends arguments for email and password to UserService.login()
     * which returns the corresponding the User object made from the row found in the User table with the corresponding
     * email and balance, or throws LoginException if none found.
     *
     * @param ctx - Current context.
     */
    public void login(Context ctx) {
        NumberFormat numberFormatter = NumberFormat.getCurrencyInstance();
        logger.info("Accessing login...");
        String email = ctx.pathParam("email");
        String password = ctx.pathParam("password");
        logger.info("Email {}, Password {}, {}", email, password, "was sent in through path parameter.");
        try {
            User loggedInUser = userService.login(email, password);
            logger.info("User logged in: {}", loggedInUser);
            ctx.json(loggedInUser);
        } catch (LoginException e) {
            logger.warn("No user with those credentials was found.");
            ctx.status(404);
        }
    }

    /**
     * Sends arguments for
     */
}
