package Bd.DAO;

import Bd.ConexionBD;
import Usuarios.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private Usuario mapearUsuario(ResultSet rs) throws SQLException {

        return new Usuario(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("email"),
                rs.getBoolean("activo")
        );
    }

    public Usuario buscarPorId(int id) {
        String sql = """
                SELECT id, nombre, email, activo
                FROM usuarios
                WHERE id = ?
                """;

        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1,id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public Usuario buscarPorEmail(String email) {

        String sql = """
                SELECT id, nombre, email, activo
                FROM usuarios
                WHERE email = ?
                """;

        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1,email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public List<Usuario> buscarTodosActivos() {

        List<Usuario> usuarios = new ArrayList<>();

        String sql = """
                SELECT id, nombre, email, activo
                FROM usuarios
                WHERE activo = true
                ORDER BY id
                """;

        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(mapearUsuario(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return usuarios;
    }

    public Usuario insertarUsuario(String nombre, String email) {

        String sql = """
                INSERT INTO usuarios (nombre, email, activo)
                VALUES (?,?,true)
                RETURNING id, nombre, email, activo
                """;

        try (Connection connection = ConexionBD.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public boolean desactivarUsuario(int id) {

        String sql = """
                UPDATE usuarios
                SET activo = false
                WHERE id = ?
                """;

        try (Connection connection = ConexionBD.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1,id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Usuario reactivarUsuario(String email) {

        String sql = """
                UPDATE usuarios
                SET activo = true
                WHERE email = ?
                RETURNING id, nombre, email, activo
                """;

        try (Connection connection = ConexionBD.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1,email);

            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

}
