package org.revature.Bank.User;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.HttpStatus;
import org.revature.Bank.util.exceptions.*;
import org.revature.Bank.util.interfaces.Controller;
import org.revature.Bank.util.interfaces.ScannerValidator;

import java.util.List;
import java.util.Scanner;
import java.text.NumberFormat;

public class UserController implements Controller {
    private final UserService userService;
    public UserController( UserService userService) {
        this.userService=userService;
    }

    public void getUserInfo(){
        List<User> users = userService.findAll();
        if(users != null){
            for(int i=0;i<users.size();i++){
                System.out.println(users.get(i).toString());
            }
        }
    }
    /**
     * register method retrieves user input for email and password, then passes User object
     * into userService.registerUser()
     *
     *
     * @throws InvalidInputException
     */
    public void register() throws InvalidInputException {


        //userService.registerUser(userToAdd);
        System.out.println("Registration complete!");
    }

    /**
     * Takes in a User object and asks user for email and password, which are passed to UserService.login()
     * and returns the corresponding User object, or throws LoginException if none found.
     *
     * @param userLoggedIn - Initialized User object with email and password
     * @return - returns the User object in userService.userList with the corresponding email and password
     */
    public User login(User userLoggedIn) throws LoginException {
        if(userLoggedIn != null) {
            throw new LoginException("A user is already logged in.");
        }


        userLoggedIn =userService.login(userLoggedIn.getEmail(),userLoggedIn.getPassword());
        if( userLoggedIn!= null) {
            return userLoggedIn;
        }
        throw new LoginException("No user with those credentials found.") ;
    }


    @Override
    public void registerPaths(Javalin app) {
        app.get("/users", this::getAllUsers);
        app.post("/users", this::postNewUser);
    }

    public void getAllUsers(Context ctx){
        List<User> users = userService.findAll();
        ctx.json(users);
    }

    public void postNewUser(Context ctx){
        // checks body for info to map into a User obj
        User user = ctx.bodyAsClass(User.class);

        ctx.json(userService.registerUser(user)); // response is the created object
        ctx.status(HttpStatus.CREATED);
    }
}
