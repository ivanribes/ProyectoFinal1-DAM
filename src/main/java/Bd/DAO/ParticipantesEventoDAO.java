package Bd.DAO;


import Bd.ConexionBD;
import Enums.EstadoPago;
import Eventos.Evento;
import Pagos.Pago;
import Usuarios.ParticipanteEvento;
import Usuarios.Usuario;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParticipantesEventoDAO {

    private UsuarioDAO usuarioDAO;
    private EventoDAO eventoDAO;

    public ParticipantesEventoDAO(UsuarioDAO usuarioDAO, EventoDAO eventoDAO) {
        this.usuarioDAO = usuarioDAO;
        this.eventoDAO = eventoDAO;
    }

    private ParticipanteEvento mapearParticipante(ResultSet rs) throws SQLException {

        return new ParticipanteEvento(
                rs.getInt("id"),
                usuarioDAO.buscarPorId(rs.getInt("usuario_id")),
                eventoDAO.buscarPorId(rs.getInt("evento_id")),
                mapearPago(rs));
    }

    private Pago mapearPago(ResultSet rs) throws SQLException {

        LocalDate fechaPago = null;

        double importeBase = rs.getDouble("importe_base");
        double penalizacion = rs.getDouble("penalizacion_aplicada");
        double importeFinal = rs.getDouble("importe_final");
        java.sql.Date sqlDate = rs.getDate("fecha_pago");
        if (!rs.wasNull()) {
            fechaPago = sqlDate.toLocalDate();
        }
        EstadoPago estadoPago = EstadoPago.valueOf(rs.getString("estado_pago"));

        return new Pago(importeBase, penalizacion, importeFinal, fechaPago, estadoPago);
    }

    public List<ParticipanteEvento> buscarTodos(Evento evento) {
        List<ParticipanteEvento> participantesEvento = new ArrayList<>();

        String sql = """
                SELECT id, usuario_id, evento_id, importe_base,penalizacion_aplicada,
                       importe_final, fecha_pago, estado_pago
                FROM participantes_evento
                WHERE evento_id = ?
                """;

        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, evento.getId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    participantesEvento.add(mapearParticipante(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return participantesEvento;
    }

    public List<ParticipanteEvento> buscarPorEstadoPago(Evento evento, EstadoPago estadoPago) {
        List<ParticipanteEvento> participantesEvento = new ArrayList<>();

        String sql = """
                SELECT id, usuario_id, evento_id, importe_base,penalizacion_aplicada,
                       importe_final, fecha_pago, estado_pago
                FROM participantes_evento
                WHERE evento_id = ? AND estado_pago = ?
                """;

        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, evento.getId());
            ps.setString(2, estadoPago.toString());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    participantesEvento.add(mapearParticipante(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return participantesEvento;
    }

    public List<Evento> buscarPorUsuario(Usuario usuario) {
        List<Evento> eventosParticipa = new ArrayList<>();

        String sql = """
                SELECT evento_id
                FROM participantes_evento
                WHERE usuario_id = ?
                """;

        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, usuario.getId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int eventoId = rs.getInt("evento_id");
                    eventosParticipa.add(eventoDAO.buscarPorId(eventoId));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return eventosParticipa;
    }

    public List<Evento> buscarPorUsuarioYEstadoPago(Usuario usuario, EstadoPago estadoPago) {
        List<Evento> eventos = new ArrayList<>();

        String sql = """
                SELECT evento_id
                FROM participantes_evento
                WHERE usuario_id = ? AND estado_pago = ?
                """;

        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, usuario.getId());
            ps.setString(2, estadoPago.name());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int eventoId = rs.getInt("evento_id");
                    eventos.add(eventoDAO.buscarPorId(eventoId));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return eventos;
    }

    public boolean insertar(Usuario usuario, Evento evento) {
        String sql = """
                INSERT INTO participantes_evento (usuario_id,
                                                  evento_id,
                                                  importe_base,
                                                  penalizacion_aplicada,
                                                  importe_final,
                                                  fecha_pago,
                                                  estado_pago,
                                                  dias_retraso)
                VALUES (?,?,?,?,?,?,?,?)
                """;
        try (Connection connection = ConexionBD.getConnection();
        PreparedStatement ps = connection.prepareStatement(sql)) {

            double importeBase = evento.recalcularImporte();

            ps.setInt(1, usuario.getId());
            ps.setInt(2, evento.getId());
            ps.setDouble(3, importeBase);
            ps.setDouble(4, 0);
            ps.setDouble(5, importeBase);
            ps.setNull(6, Types.DATE);
            ps.setString(7, EstadoPago.PENDIENTE.name());
            ps.setNull(8, Types.INTEGER );

            int filas = ps.executeUpdate();

            if (filas > 0) {
                actualizarImportes(evento);
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public boolean eliminar(Usuario usuario, Evento evento) {
        String sql = """
                DELETE FROM participantes_evento
                WHERE evento_id = ? AND usuario_id = ?
                """;
        try (Connection connection = ConexionBD.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, evento.getId());
            ps.setInt(2, usuario.getId());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                actualizarImportes(evento);
                return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    private void actualizarImportes(Evento evento) {

        String sql = """
                UPDATE participantes_evento
                SET importe_base = ?,
                importe_final = ? + penalizacion_aplicada
                WHERE evento_id = ?
                """;

        try (Connection connection = ConexionBD.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setDouble(1, evento.recalcularImporte());
            ps.setDouble(2, evento.recalcularImporte());
            ps.setInt(3, evento.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int contarParticipantes (Evento evento) {
        String sql = """
                SELECT COUNT(*) as participantes
                FROM participantes_evento
                WHERE evento_id = ?
                """;

        try (Connection connection = ConexionBD.getConnection();
            PreparedStatement ps = connection.prepareStatement(sql)){

            ps.setInt(1, evento.getId());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("participantes");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }
}
