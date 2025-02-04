package electricity.billing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class Conn implements AutoCloseable {  // Implement AutoCloseable
    public Connection c;
    public Statement s;

    public Conn() {
        try {
            // Load the JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish the connection to the database
            c = DriverManager.getConnection("jdbc:mysql://localhost:3306/elec", "root", "tanishka2@T");

            // Create a statement for executing queries
            s = c.createStatement();

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found! Ensure MySQL Connector/J is added to your classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database. Check your connection URL, username, and password.");
            e.printStackTrace();
        }
    }

    // Method to prepare SQL statements
    public PreparedStatement prepareStatement(String query) throws SQLException {
        if (c == null) {
            throw new SQLException("Connection is not established!");
        }
        return c.prepareStatement(query);
    }

    // Implement close() method for AutoCloseable
    @Override
    public void close() {
        try {
            if (s != null) {
                s.close();
            }
            if (c != null) {
                c.close();
            }
        } catch (SQLException e) {
            System.out.println("Error closing the database connection.");
            e.printStackTrace();
        }
    }
}
