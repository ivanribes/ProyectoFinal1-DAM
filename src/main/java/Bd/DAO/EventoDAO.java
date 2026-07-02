package Bd.DAO;

import Bd.ConexionBD;
import Eventos.Evento;
import Usuarios.Usuario;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EventoDAO {

    private Evento mapearEvento(ResultSet rs, UsuarioDAO usuarioDAO) throws SQLException {
        int id = rs.getInt("id");
        String nombre = rs.getString("nombre");
        String descripcion = rs.getString("descripcion");
        double importeTotal = rs.getDouble("importe_total");
        int creadorId = rs.getInt("creador_id");
        LocalDate fechaCreacion = rs.getDate("fecha_creacion").toLocalDate();
        LocalDate fechaPagoLimite = rs.getDate("fecha_pago_limite").toLocalDate();

        Usuario creador = usuarioDAO.buscarPorId(creadorId);
        if (creador == null) {
            throw new RuntimeException("Creador con ID " + creadorId + " no encontrado");
        }

        return new Evento(id, nombre, importeTotal, creador, fechaCreacion, fechaPagoLimite, descripcion);
    }

    public Evento buscarPorId(int id) {
        String sql = """
                SELECT id, nombre, descripcion, importe_total, creador_id, fecha_creacion, fecha_pago_limite
                FROM eventos
                WHERE id = ?
                """;

        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UsuarioDAO usuarioDAO = new UsuarioDAO();
                    return mapearEvento(rs, usuarioDAO);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public List<Evento> buscarTodos() {
        List<Evento> eventos = new ArrayList<>();

        String sql = """
                SELECT id, nombre, descripcion, importe_total, creador_id, fecha_creacion, fecha_pago_limite
                FROM eventos
                ORDER BY fecha_creacion DESC
                """;

        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                while (rs.next()) {
                    eventos.add(mapearEvento(rs, usuarioDAO));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return eventos;
    }

    public List<Evento> buscarPorCreador(int creadorId) {
        List<Evento> eventos = new ArrayList<>();

        String sql = """
                SELECT id, nombre, descripcion, importe_total, creador_id, fecha_creacion, fecha_pago_limite
                FROM eventos
                WHERE creador_id = ?
                ORDER BY fecha_creacion DESC
                """;

        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, creadorId);

            try (ResultSet rs = ps.executeQuery()) {
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                while (rs.next()) {
                    eventos.add(mapearEvento(rs, usuarioDAO));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return eventos;
    }

    public Evento insertar(String nombre, double importeTotal, int creadorId, String descripcion) {
        String sql = """
                INSERT INTO eventos (nombre, descripcion, importe_total, creador_id, fecha_creacion, fecha_pago_limite)
                VALUES (?, ?, ?, ?, ?, ?)
                RETURNING id, nombre, descripcion, importe_total, creador_id, fecha_creacion, fecha_pago_limite
                """;

        LocalDate hoy = LocalDate.now();
        LocalDate fechaPago = hoy.plusDays(2);

        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setDouble(3, importeTotal);
            ps.setInt(4, creadorId);
            ps.setDate(5, Date.valueOf(hoy));
            ps.setDate(6, Date.valueOf(fechaPago));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UsuarioDAO usuarioDAO = new UsuarioDAO();
                    return mapearEvento(rs, usuarioDAO);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public boolean actualizar(int id, String nombre, double importeTotal, String descripcion) {
        String sql = """
                UPDATE eventos
                SET nombre = ?, descripcion = ?, importe_total = ?
                WHERE id = ?
                """;

        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, nombre);
            ps.setString(2, descripcion);
            ps.setDouble(3, importeTotal);
            ps.setInt(4, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean eliminar(int id) {
        String sql = """
                DELETE FROM eventos
                WHERE id = ?
                """;

        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
