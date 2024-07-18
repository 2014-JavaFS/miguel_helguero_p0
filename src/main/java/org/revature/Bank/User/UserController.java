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
        app.get("/login", this::getLogin);
        app.get("/logout", this::getLogout);
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
     * Sends arguments for email and password to UserService.login()
     * which returns the corresponding the User object made from the row found in the User table with the corresponding
     * email and balance, or throws LoginException if none found.
     *
     * @param ctx - Current context.
     */
    public void getLogin(Context ctx) {
        logger.info("Accessing login...");

        String email = ctx.queryParam("email");
        String password = ctx.queryParam("password");
        logger.info("Email {}, Password {}, {}", email, password, "was sent in through path parameter.");
        try {
            if(!ctx.header("userId").equals("null")) throw new LoginException("A user is already logged in.");
            User loggedInUser = userService.login(email, password);
            logger.info("User logged in: {}", loggedInUser);

            ctx.header("userId", String.valueOf(loggedInUser.getUserId()));
            ctx.result("Login successful!\n" + loggedInUser);
            ctx.status(200);
        } catch (LoginException | NullPointerException e) {
            logger.warn(e.getMessage());
            ctx.result(e.getMessage());
            ctx.status(404);
        }
    }

    public void getLogout(Context ctx) {
        logger.info("Accessing logout...");
        ctx.result("User was logged out.");
        ctx.status(200);
    }
}
