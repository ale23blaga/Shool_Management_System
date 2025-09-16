package school.management.system.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/Proiect_PlatformaDeStudiu2";
    private static final String USER = "root";
    private static final String PASSWORD = "A@e230404"; // change accordingly

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
