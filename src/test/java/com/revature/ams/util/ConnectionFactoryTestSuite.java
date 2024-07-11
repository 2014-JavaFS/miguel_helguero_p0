package com.revature.ams.util;

import org.junit.jupiter.api.Test;
import org.revature.Bank.util.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConnectionFactoryTestSuite {
    @Test public void test_getConnection_returnValidConnection(){
        try (Connection conn = ConnectionFactory.getConnectionFactory().getConnection();){
            assertNotNull(conn);
        } catch(SQLException e){
            e.printStackTrace();
        }
    }
}
