package Usuarios;

import Eventos.Evento;
import Pagos.Pago;

public class ParticipanteEvento {

    private static int ID_PARTICIPANTE = 0;

    private final Usuario usuario;
    private final Evento evento;
    private Pago pago;

    public ParticipanteEvento(Usuario usuario, Evento evento,
                              double importeDebe) {
        this.ID_PARTICIPANTE = ++ID_PARTICIPANTE;
        this.usuario = usuario;
        this.evento = evento;
        this.pago = new Pago(importeDebe);
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setImporteDebe(double importeDebe) {
        pago.setImporteBase(importeDebe);
    }

    public Pago getPago() {
        return pago;
    }
}
