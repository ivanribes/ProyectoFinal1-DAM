package Bd;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final Dotenv DOTENV = Dotenv.configure()
            .ignoreIfMissing()
            .load();

    private static final String URL = obtenerVariable("DB_URL");
    private static final String USER = obtenerVariable("DB_USER");
    private static final String PASSWORD = obtenerVariable("DB_PASSWORD");

    private ConexionBD() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    private static String obtenerVariable(String nombre) {
        String valor = System.getenv(nombre);

        if (valor == null || valor.isBlank()) {
            valor = DOTENV.get(nombre);
        }

        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException("Falta configurar la variable: " + nombre);
        }

        return valor;
    }
}