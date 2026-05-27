package Prueba;


import App.GestorMorosos;
import Enums.EstadoPago;
import Eventos.Evento;
import Ficheros.GestorFicheros;
import Usuarios.ParticipanteEvento;
import Usuarios.Usuario;


public class DatosPrueba {

    public static void cargarDatosPrueba(GestorMorosos gestor) {

        GestorFicheros lector = new GestorFicheros("Datos/Datos.csv", gestor);

        Usuario carlos = gestor.buscarUsuarioID(1);
        Usuario laura = gestor.buscarUsuarioID(2);
        Usuario david = gestor.buscarUsuarioID(3);
        Usuario marta = gestor.buscarUsuarioID(4);
        Usuario sergio = gestor.buscarUsuarioID(5);
        Usuario andrea = gestor.buscarUsuarioID(6);

        // EVENTO 1
        Evento cena = new Evento("Cena clase DAM", 180, carlos);

        ParticipanteEvento p1 =
                new ParticipanteEvento(laura, cena);

        ParticipanteEvento p2 =
                new ParticipanteEvento(david, cena);

        ParticipanteEvento p3 =
                new ParticipanteEvento(marta, cena);

        cena.aniadirParticipantes(p1);
        cena.aniadirParticipantes(p2);
        cena.aniadirParticipantes(p3);

        gestor.aniadirEvento(cena);

        // EVENTO 2
        Evento viaje = new Evento("Viaje Peñiscola", 450, laura);

        ParticipanteEvento p4 =
                new ParticipanteEvento(carlos, viaje);

        ParticipanteEvento p5 =
                new ParticipanteEvento(andrea, viaje);

        ParticipanteEvento p6 =
                new ParticipanteEvento(sergio, viaje);

        viaje.aniadirParticipantes(p4);
        viaje.aniadirParticipantes(p5);
        viaje.aniadirParticipantes(p6);

        gestor.aniadirEvento(viaje);

        // EVENTO 3
        Evento regalo = new Evento("Regalo cumpleaños", 120, marta);

        ParticipanteEvento p7 =
                new ParticipanteEvento(carlos, regalo);

        ParticipanteEvento p8 =
                new ParticipanteEvento(laura, regalo);

        regalo.aniadirParticipantes(p7);
        regalo.aniadirParticipantes(p8);

        gestor.aniadirEvento(regalo);

        // =========================
        // PAGOS
        // =========================

        // Laura paga tarde
        p1.getPago().setEstadoPago(EstadoPago.PAGADO);
        p1.getPago().setFechaPago(
                cena.getFechaCreacion().plusDays(6));

        p1.getPago().setPenalizacionAplicada(
                p1.getPago().calcularPenalizacion(
                        cena.getFechaPagoLimite(),
                        p1.getPago().getFechaPago()));

        // David paga MUY tarde
        p2.getPago().setEstadoPago(EstadoPago.PAGADO);
        p2.getPago().setFechaPago(
                cena.getFechaCreacion().plusDays(12));

        p2.getPago().setPenalizacionAplicada(
                p2.getPago().calcularPenalizacion(
                        cena.getFechaPagoLimite(),
                        p2.getPago().getFechaPago()));

        // Marta sigue pendiente
        p3.getPago().setEstadoPago(EstadoPago.PENDIENTE);

        // Carlos paga normal
        p4.getPago().setEstadoPago(EstadoPago.PAGADO);
        p4.getPago().setFechaPago(
                viaje.getFechaCreacion().plusDays(2));

        // Andrea rechazada
        p5.getPago().setEstadoPago(EstadoPago.RECHAZADO);

        // Sergio pendiente confirmar
        p6.getPago().setEstadoPago(
                EstadoPago.PENDIENTE_CONFIRMAR);

        p6.getPago().setFechaPago(
                viaje.getFechaCreacion().plusDays(5));

        // Carlos vuelve a pagar tarde
        p7.getPago().setEstadoPago(EstadoPago.PAGADO);

        p7.getPago().setFechaPago(
                regalo.getFechaCreacion().plusDays(7));

        p7.getPago().setPenalizacionAplicada(
                p7.getPago().calcularPenalizacion(
                        regalo.getFechaPagoLimite(),
                        p7.getPago().getFechaPago()));

        // Laura paga rápido
        p8.getPago().setEstadoPago(EstadoPago.PAGADO);

        p8.getPago().setFechaPago(
                regalo.getFechaCreacion().plusDays(1));


        lector.cargarFichero();
    }
}
