package university.management.system;

import java.sql.*;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Conn {
    private static final Logger LOGGER = Logger.getLogger(Conn.class.getName());
    private static final String URL = "jdbc:mysql://localhost:3306/universitymanagementsystem";
    private static final String USER = "root";
    private static final String PASSWORD = "7372998336";
    
    private static Conn instance;
    private Connection connection;
    private Statement statement;
    
    private Conn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            statement = connection.createStatement();
            LOGGER.info("Database connection established successfully");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found", e);
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to establish database connection", e);
            throw new RuntimeException("Failed to establish database connection", e);
        }
    }
    
    public static synchronized Conn getInstance() {
        if (instance == null) {
            instance = new Conn();
        }
        return instance;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public Statement getStatement() {
        return statement;
    }
    
    public void close() {
        try {
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
            LOGGER.info("Database connection closed successfully");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing database connection", e);
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}
