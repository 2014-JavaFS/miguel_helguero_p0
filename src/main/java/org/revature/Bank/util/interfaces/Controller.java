package org.revature.Bank.util.interfaces;

import io.javalin.Javalin;

public interface Controller {
    void registerPaths(Javalin app);
}
