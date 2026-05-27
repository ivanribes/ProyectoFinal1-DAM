package App;

import Enums.EstadoPago;
import Eventos.Evento;
import Excepciones.UnknownEventException;
import Excepciones.UnknownPaymentException;
import Excepciones.UnknownUserException;
import Ficheros.GestorFicheros;
import Pagos.Pago;
import Rankings.Ranking;
import Usuarios.ParticipanteEvento;
import Usuarios.Usuario;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestorMorosos {
    private ArrayList<Usuario> usuarios;
    private ArrayList<Evento> eventos;
    private Ranking ranking;
    private GestorFicheros gestorFicheros;

    //para pruebas de penalización
    private LocalDate fechaModificada;
    //---------------------------

    public GestorMorosos() {
        this.usuarios = new ArrayList<>();
        this.eventos = new ArrayList<>();
        this.fechaModificada = LocalDate.now();
    }

    public LocalDate getFechaModificada() {
        return fechaModificada;
    }

    public List<Usuario> getUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    public List<Evento> getEventos() {
        return Collections.unmodifiableList(eventos);
    }

    public void setRanking(Ranking tipoRanking) {
        this.ranking = tipoRanking;
    }

    public Ranking getRanking() {
        return ranking;
    }

    public void aniadirUsuario(Usuario user) {
        usuarios.add(user);
    }

    public void aniadirEvento(Evento evento) {
        eventos.add(evento);
    }

    //region MODIFICAR FECHA
    public void sumarDias() {
        int dias = Integer.parseInt(IO.readln("Introduce la cantidad de dias que quieres " +
                "aumentar la fecha: "));

        this.fechaModificada = fechaModificada.plusDays(dias);
    }

    public void mostrarFecha() {
        System.out.println(fechaModificada);
    }
    //endregion

    public void mostrarUsuarios(Usuario usuarioActual) {

        for (Usuario u: usuarios) {
            if (u != usuarioActual) {
                System.out.println("User_ID: " + u.getId() + " --> " + u.getEmail());
            }
        }
    }

    public Usuario buscarUsuarioID(int id) throws UnknownUserException {
        for (Usuario u : usuarios) {
            if (id == u.getId())  {
                return u;
            }
        }

        throw new UnknownUserException();
    }

    public Evento buscarEvento(int id) throws UnknownEventException {
        for (Evento e : eventos) {
            if (id == e.getId())  {
                return e;
            }
        }

        throw new UnknownEventException();
    }

    public Pago buscarPago(Evento evento, int idPago) throws UnknownPaymentException{
        for (ParticipanteEvento p : evento.getListParticipantes()) {
            if (p.getPago().getId() == idPago) {
                return p.getPago();
            }
        }

        throw new UnknownPaymentException();
    }

    public Pago buscarPago(int idPago) throws UnknownPaymentException {
        for (Evento e: eventos) {
            for (ParticipanteEvento p : e.getListParticipantes()) {
                if (p.getPago().getId() == idPago) {
                    return p.getPago();
                }
            }
        }

        throw new UnknownPaymentException();
    }

    public void actualizarPenalizaciones() {
        for (Evento e : eventos) {
            for (ParticipanteEvento p : e.getListParticipantes()) {
                if (p.getPago().getEstadoPago() == EstadoPago.PENDIENTE ||
                        p.getPago().getEstadoPago() == EstadoPago.RECHAZADO) {
                    p.getPago().setPenalizacionAplicada(p.getPago()
                            .calcularPenalizacion(e.getFechaPagoLimite(), fechaModificada));
                }
            }
        }
    }
}
