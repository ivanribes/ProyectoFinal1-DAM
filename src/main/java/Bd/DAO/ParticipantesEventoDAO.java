package Bd.DAO;


import Bd.ConexionBD;
import Enums.EstadoPago;
import Eventos.Evento;
import Pagos.Pago;
import Usuarios.ParticipanteEvento;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
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
}
