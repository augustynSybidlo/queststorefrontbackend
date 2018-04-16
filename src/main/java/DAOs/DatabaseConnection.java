package DAOs;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static DatabaseConnection instance = null;
    private Connection connection = null;
    private final String DBNAME = "jdbc:sqlite:src/main/resources/database/questStore.db";


    private DatabaseConnection() {
        getConnectionToDatabase();
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    private void getConnectionToDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.connection = DriverManager.getConnection(DBNAME);
        } catch (SQLException e) {
            System.out.println(e.getClass() + ": " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getClass() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public void closeConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (SQLException e) {
                /* ignored */
            }
        }
    }
}


