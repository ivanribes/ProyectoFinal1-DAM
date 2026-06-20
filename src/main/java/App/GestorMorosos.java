package App;

import Enums.EstadoPago;
import Eventos.Evento;
import Excepciones.UnknownEventException;
import Excepciones.UnknownPaymentException;
import Excepciones.UnknownUserException;
import Pagos.Pago;
import Rankings.Ranking;
import Usuarios.ParticipanteEvento;
import Usuarios.Usuario;
import Utilidades.Entrada;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestorMorosos {

    private final ArrayList<Usuario> usuarios;
    private final ArrayList<Evento> eventos;
    private Ranking ranking;

    // Para pruebas de penalización
    private LocalDate fechaModificada;

    public GestorMorosos() {
        this.usuarios = new ArrayList<>();
        this.eventos = new ArrayList<>();
        this.fechaModificada = LocalDate.now();
    }

    //region GETTERS Y SETTERS
    public LocalDate getFechaModificada() {
        return fechaModificada;
    }

    public List<Usuario> getUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    public List<Evento> getEventos() {
        return Collections.unmodifiableList(eventos);
    }

    public Ranking getRanking() {
        return ranking;
    }

    public void setRanking(Ranking tipoRanking) {
        this.ranking = tipoRanking;
    }
    //endregion

    //region USUARIOS
    public void aniadirUsuario(Usuario user) {
        usuarios.add(user);
    }

    public void mostrarUsuarios(Usuario usuarioActual) {
        for (Usuario u : usuarios) {
            if (u != usuarioActual) {
                System.out.println("User_ID: " + u.getId() + " --> " + u.getEmail());
            }
        }
    }

    public void mostrarUsuariosActivos(Usuario usuarioActual) {
        for (Usuario u : usuarios) {
            if (u.isActivo() && u != usuarioActual) {
                System.out.println("User_ID: " + u.getId() + " --> " + u.getEmail());
            }
        }
    }

    public Usuario buscarUsuarioID(int id) throws UnknownUserException {
        for (Usuario u : usuarios) {
            if (id == u.getId()) {
                return u;
            }
        }

        throw new UnknownUserException();
    }

    public Usuario buscarUsuarioActivoID(int id) throws UnknownUserException {
        for (Usuario u : usuarios) {
            if (id == u.getId() && u.isActivo()) {
                return u;
            }
        }

        throw new UnknownUserException();
    }

    public boolean hayUsuariosActivosDisponibles(Usuario usuarioActual) {
        for (Usuario u : usuarios) {
            if (u.isActivo() && u != usuarioActual) {
                return true;
            }
        }

        return false;
    }
    //endregion

    //region EVENTOS
    public boolean aniadirEvento(Evento evento) {
        if (evento == null) {
            return false;
        }

        if (evento.getCreador() == null) {
            return false;
        }

        if (!evento.getCreador().isActivo()) {
            return false;
        }

        eventos.add(evento);
        return true;
    }

    public Evento buscarEvento(int id) throws UnknownEventException {
        for (Evento e : eventos) {
            if (id == e.getId()) {
                return e;
            }
        }

        throw new UnknownEventException();
    }

    public Evento buscarEventoCreadoPorUsuario(int idEvento, Usuario usuario)
            throws UnknownEventException {

        for (Evento evento : eventos) {
            if (evento.getId() == idEvento && evento.getCreador() == usuario) {
                return evento;
            }
        }

        throw new UnknownEventException();
    }
    //endregion

    //region PAGOS
    public Pago buscarPago(Evento evento, int idPago) throws UnknownPaymentException {
        for (ParticipanteEvento p : evento.getListParticipantes()) {
            if (p.getPago().getId() == idPago) {
                return p.getPago();
            }
        }

        throw new UnknownPaymentException();
    }

    public Pago buscarPago(int idPago) throws UnknownPaymentException {
        for (Evento e : eventos) {
            for (ParticipanteEvento p : e.getListParticipantes()) {
                if (p.getPago().getId() == idPago) {
                    return p.getPago();
                }
            }
        }

        throw new UnknownPaymentException();
    }

    public Pago buscarPagoPendienteUsuario(int idPago, Usuario usuario)
            throws UnknownPaymentException {

        for (Evento evento : eventos) {
            for (ParticipanteEvento participante : evento.getListParticipantes()) {
                Pago pago = participante.getPago();

                if (participante.getUsuario() == usuario &&
                        pago.getId() == idPago &&
                        pago.puedeSerSaldado()) {

                    return pago;
                }
            }
        }

        throw new UnknownPaymentException();
    }

    public Pago buscarPagoPendienteConfirmar(Evento evento, int idPago)
            throws UnknownPaymentException {

        for (ParticipanteEvento participante : evento.getListParticipantes()) {
            Pago pago = participante.getPago();

            if (pago.getId() == idPago &&
                    pago.getEstadoPago() == EstadoPago.PENDIENTE_CONFIRMAR) {
                return pago;
            }
        }

        throw new UnknownPaymentException();
    }

    public void actualizarPenalizaciones() {
        for (Evento e : eventos) {
            for (ParticipanteEvento p : e.getListParticipantes()) {
                if (p.getPago().getEstadoPago() == EstadoPago.PENDIENTE ||
                        p.getPago().getEstadoPago() == EstadoPago.RECHAZADO) {
                    p.getPago().setPenalizacionAplicada(
                            p.getPago().calcularPenalizacion(
                                    e.getFechaPagoLimite(), fechaModificada));
                }
            }
        }
    }
    //endregion

    //region MODIFICAR FECHA
    public void sumarDias() {
        int dias = Entrada.leerIntPositivo(
                "Introduce la cantidad de dias que quieres aumentar la fecha: ");

        this.fechaModificada = fechaModificada.plusDays(dias);
    }

    public void mostrarFecha() {
        System.out.println(fechaModificada);
    }
    //endregion
}
