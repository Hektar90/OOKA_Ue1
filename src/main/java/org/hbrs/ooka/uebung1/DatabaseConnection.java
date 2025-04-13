package org.hbrs.ooka.uebung1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:h2:~/test";
    private static final String USER = "tk";
    private static final String PASSWORD = "";

    private Connection connection;

    public void openConnection() {
        try {
            DriverManager.registerDriver(new org.h2.Driver());
            if (connection == null) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            } else {
                if (connection.isClosed()) {
                    connection = DriverManager.getConnection(URL, USER, PASSWORD);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}


