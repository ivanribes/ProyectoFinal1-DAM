package Bd;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConexionBD {

    public static void main(String[] args) {
        try (Connection connection = ConexionBD.getConnection()) {
            System.out.println("Conexión correcta con Supabase/PostgreSQL.");
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos.");
            System.out.println(e.getMessage());
        }
    }
}