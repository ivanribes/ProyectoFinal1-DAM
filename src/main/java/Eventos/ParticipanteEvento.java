package Eventos;

import Enums.EstadoPago;
import Enums.EstadoParticipante;
import Pagos.Pago;
import Usuarios.Usuario;

public class ParticipanteEvento {

    private final int ID_PARTICIPANTE;

    private final Usuario usuario;
    private final Evento evento;
    private double importeDebe;
    private EstadoPago estadoPago;
    private Pago pago;

    public ParticipanteEvento(int ID_PARTICIPANTE, Usuario usuario, Evento evento,
                              double importeDebe,
                              EstadoPago estadoPago, Pago pago) {
        this.ID_PARTICIPANTE = ID_PARTICIPANTE;
        this.usuario = usuario;
        this.evento = evento;
        this.importeDebe = importeDebe;
        this.estadoPago = estadoPago;
        this.pago = pago;
    }
}
