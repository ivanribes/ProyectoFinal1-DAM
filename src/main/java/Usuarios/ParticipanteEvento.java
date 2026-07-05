package Usuarios;

import Eventos.Evento;
import Pagos.Pago;

public class ParticipanteEvento {

    private final int idParticipante;
    private final Usuario usuario;
    private final Evento evento;
    private Pago pago;

    public ParticipanteEvento(int idParticipante, Usuario usuario, Evento evento) {
        this.idParticipante = idParticipante;
        this.usuario = usuario;
        this.evento = evento;
    }

    public ParticipanteEvento(int idParticipante, Usuario usuario, Evento evento, Pago pago) {
        this.idParticipante = idParticipante;
        this.usuario = usuario;
        this.evento = evento;
        this.pago = pago;
    }

    public int getIdParticipante() {
        return idParticipante;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setImporteDebe(double importeDebe) {
        this.pago = new Pago(importeDebe);
    }

    public Pago getPago() {
        return pago;
    }
}
