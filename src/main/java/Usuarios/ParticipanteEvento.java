package Usuarios;

import Eventos.Evento;
import Pagos.Pago;

public class ParticipanteEvento {

    //TODO podria crear la id a partir del idevento + usuario o algo asi
    private static int ID_PARTICIPANTE = 0;

    private final Usuario usuario;
    private final Evento evento;
    private Pago pago;

    public ParticipanteEvento(Usuario usuario, Evento evento) {
        this.ID_PARTICIPANTE = ++ID_PARTICIPANTE;
        this.usuario = usuario;
        this.evento = evento;
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
