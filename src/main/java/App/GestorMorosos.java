package App;

import Bd.DAO.EventoDAO;
import Bd.DAO.ParticipantesEventoDAO;
import Bd.DAO.UsuarioDAO;
import Enums.EstadoPago;
import Eventos.Evento;
import Excepciones.UnknownEventException;
import Excepciones.UnknownPaymentException;
import Excepciones.UnknownUserException;
import Pagos.Pago;
import Rankings.Ranking;
import Usuarios.ParticipanteEvento;
import Usuarios.Usuario;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestorMorosos {

    private final ArrayList<Usuario> usuarios;
    private Ranking ranking;
    private final UsuarioDAO usuarioDAO;
    private final EventoDAO eventoDAO;
    private final ParticipantesEventoDAO participantesEventoDAO;

    // Para pruebas de penalización
    private LocalDate fechaModificada;

    public GestorMorosos() {
        this.usuarioDAO = new UsuarioDAO();
        this.eventoDAO = new EventoDAO();
        this.participantesEventoDAO = new ParticipantesEventoDAO(usuarioDAO, eventoDAO);
        this.usuarios = new ArrayList<>();
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
        return eventoDAO.buscarTodos();
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

    public void insertarUsuario(String nombre, String email) {
        usuarioDAO.insertarUsuario(nombre, email);
    }

    public Usuario registrarUsuario(String nombre, String email) {
        Usuario usuarioExistente = usuarioDAO.buscarPorEmail(email);

        if (usuarioExistente == null) {
            return usuarioDAO.insertarUsuario(nombre, email);
        }

        if (!usuarioExistente.isActivo()) {
            return usuarioDAO.reactivarUsuario(email);
        }

        return null;
    }

    public boolean aniadirParticipanteAEvento(Evento evento, Usuario usuario) {
        if (evento == null || usuario == null) {
            return false;
        }

        if (evento.tienePagosIniciados()) {
            return false;
        }

        if (evento.esCreador(usuario)) {
            return false;
        }

        if (evento.usuarioEsParticipante(usuario)) {
            return false;
        }

        ParticipanteEvento participante = new ParticipanteEvento(usuario, evento);
        return evento.aniadirParticipantes(participante);
    }

    public List<Usuario> obtenerUsuariosDisponibles(Usuario usuarioActual) {
        List<Usuario> usuarios = usuarioDAO.buscarTodosActivos();
        List<Usuario> usuariosDisponibles = new ArrayList<>();

        if (usuarioActual == null) {
            return usuarios;
        }

        for (Usuario u : usuarios) {

            if (u.getId() != usuarioActual.getId()) {
                usuariosDisponibles.add(u);
            }
        }

        return usuariosDisponibles;
    }

    public List<Usuario> obtenerUsuariosDisponiblesParaEvento(Evento evento) {
        List<Usuario> usuariosActivos = usuarioDAO.buscarTodosActivos();
        List<Usuario> usuariosDisponibles = new ArrayList<>();

        for (Usuario usuario : usuariosActivos) {
            if (!evento.esCreador(usuario) && !evento.usuarioEsParticipante(usuario)) {
                usuariosDisponibles.add(usuario);
            }
        }

        return usuariosDisponibles;
    }

    public Usuario buscarUsuarioID(int id) throws UnknownUserException {
       Usuario usuario = usuarioDAO.buscarPorId(id);

       if (usuario != null) {
           return usuario;
       } else {
           throw new UnknownUserException();
       }
    }

    public Usuario buscarUsuarioActivoID(int id) throws UnknownUserException {
        Usuario usuario = buscarUsuarioID(id);

        if (usuario != null && usuario.isActivo()) {
            return usuario;
        } else {
            throw new UnknownUserException();
        }
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
    public Evento crearEvento(String nombre, double importeTotal, String descripcion, Usuario creador) {
        if (creador == null || !creador.isActivo()) {
            return null;
        }

        return eventoDAO.insertar(nombre, importeTotal, creador.getId(), descripcion);
    }

    public Evento aniadirEvento(Evento evento) {
        if (evento == null || evento.getCreador() == null) {
            return null;
        }

        return eventoDAO.insertar(
                evento.getNombre(),
                evento.getImporteTotal(),
                evento.getCreador().getId(),
                evento.getDescripcion());
    }

    public Evento buscarEvento(int id) throws UnknownEventException {
        Evento evento = eventoDAO.buscarPorId(id);

        if (evento != null) {
            return evento;
        }

        throw new UnknownEventException();
    }

    public Evento buscarEventoCreadoPorUsuario(int idEvento, Usuario usuario)
            throws UnknownEventException {

        Evento evento = eventoDAO.buscarPorId(idEvento);

        if (evento != null && evento.getCreador().getId() == usuario.getId()) {
            return evento;
        }

        throw new UnknownEventException();
    }

    public List<Evento> obtenerEventosCreadosPor(Usuario usuario) {
        if (usuario == null) {
            return new ArrayList<>();
        }

        return eventoDAO.buscarPorCreador(usuario.getId());
    }
    //endregion

    //region PARTICIPANTE EVENTO

    public boolean sePuedeModificarParticipantes(Evento evento) {

        List<ParticipanteEvento> listaPendientes = participantesEventoDAO.buscarPorEstadoPago(evento,
                EstadoPago.PENDIENTE);
        List<ParticipanteEvento> listaTodosParticipantes = participantesEventoDAO.buscarTodos(evento);

        return listaPendientes.size() == listaTodosParticipantes.size();

    }

    public boolean agregarParticipante(Usuario usuario, Evento evento) {

        return participantesEventoDAO.insertar(usuario, evento);

    }

    public boolean eliminarParticipante(Usuario usuario, Evento evento) {

        return participantesEventoDAO.eliminar(usuario, evento);

    }

    public List<ParticipanteEvento> obtenerParticipantesDeEvento(Evento evento) {

        return participantesEventoDAO.buscarTodos(evento);
    }

    //endregion

    //region PAGOS

    public Pago buscarPagoPendienteUsuario(int idPago, Usuario usuario)
            throws UnknownPaymentException {

        //TODO usar dao (creo)
        for (Evento evento : eventoDAO.buscarTodos()) {
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
        //TODO usar dao (creo)
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
        for (Evento e : eventoDAO.buscarTodos()) {
            for (ParticipanteEvento p : e.getListParticipantes()) {
                if (p.getPago().getEstadoPago() == EstadoPago.PENDIENTE ||
                        p.getPago().getEstadoPago() == EstadoPago.RECHAZADO) {

                    p.getPago().setPenalizacionAplicada(
                            p.getPago().calcularPenalizacion(
                                    e.getFechaPagoLimite(),
                                    fechaModificada));
                }
            }
        }
    }
    //endregion

    //region MODIFICAR FECHA
    public void sumarDias(int dias) {
        this.fechaModificada = fechaModificada.plusDays(dias);
    }
    //endregion
}