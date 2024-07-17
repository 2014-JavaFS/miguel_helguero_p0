package org.revature.Bank.util;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/*
    Factory Design
        - Creational, used to abstract creation/instantiaion of our class away from the user
        - churn out instances of Connections to all the other objects in our API
        - User will need  a connection
        - Singleton Design Pattern:
            - Creational design, restricts that theres only a single instance of the class that can be made
 */
public class ConnectionFactory {
    private static final ConnectionFactory connectionFactory = new ConnectionFactory();
    private final Properties properties = new Properties();

    // Singleton Design
    private ConnectionFactory(){
        try{
            properties.load(new FileReader("src/main/resources/db.properties"));
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    // static block to check driver availability, runs once, as soon as class loaded into memory(before any instantiation)
    // without "static", code block would run when an object is instantiated
    static {
        try{
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
    public static ConnectionFactory getConnectionFactory(){
        return connectionFactory;
    }

    public Connection getConnection(){
        // use JDBC driver manager
        try {
            // ensure you set up your db.properties in resources directory
            return DriverManager.getConnection(properties.getProperty("url"), properties.getProperty("user"), properties.getProperty("password"));
        } catch(SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            return null;
        }
    }
}
