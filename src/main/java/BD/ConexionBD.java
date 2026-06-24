package BD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL = System.getenv("DB_URL");
    private static final String USER = System.getenv("DB_USER");

    private static final String PASSWORD = System.getenv("DB_PASSWORD");

    private ConexionBD() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}