package Prueba;


import App.GestorMorosos;
import Enums.EstadoPago;
import Eventos.Evento;
import Rankings.RankingMoroso;
import Usuarios.ParticipanteEvento;
import Usuarios.Usuario;

public class DatosPrueba {

    public static void cargarDatosRanking(GestorMorosos gestor) {

        Usuario carlos = new Usuario(1, "Carlos Martinez", "carlos@gmail.com");
        Usuario laura = new Usuario(2, "Laura Gomez", "laura@gmail.com");
        Usuario david = new Usuario(3, "David Fernandez", "david@gmail.com");
        Usuario marta = new Usuario(4, "Marta Lopez", "marta@gmail.com");

        gestor.aniadirUsuario(carlos);
        gestor.aniadirUsuario(laura);
        gestor.aniadirUsuario(david);
        gestor.aniadirUsuario(marta);

        Evento cena = new Evento("Cena clase", 120, carlos);
        Evento regalo = new Evento("Regalo cumpleaños", 200, laura);
        Evento viaje = new Evento("Viaje fin de semana", 300, david);

        gestor.aniadirEvento(cena);
        gestor.aniadirEvento(regalo);
        gestor.aniadirEvento(viaje);

        ParticipanteEvento p1 = new ParticipanteEvento(laura, cena, 40);
        ParticipanteEvento p2 = new ParticipanteEvento(david, cena, 40);
        ParticipanteEvento p3 = new ParticipanteEvento(marta, cena, 40);

        cena.aniadirParticipantes(p1);
        cena.aniadirParticipantes(p2);
        cena.aniadirParticipantes(p3);

        ParticipanteEvento p4 = new ParticipanteEvento(carlos, regalo, 100);
        ParticipanteEvento p5 = new ParticipanteEvento(david, regalo, 100);

        regalo.aniadirParticipantes(p4);
        regalo.aniadirParticipantes(p5);

        ParticipanteEvento p6 = new ParticipanteEvento(carlos, viaje, 100);
        ParticipanteEvento p7 = new ParticipanteEvento(laura, viaje, 100);
        ParticipanteEvento p8 = new ParticipanteEvento(marta, viaje, 100);

        viaje.aniadirParticipantes(p6);
        viaje.aniadirParticipantes(p7);
        viaje.aniadirParticipantes(p8);

        // Laura paga tarde: 5 días
        p1.getPago().setEstadoPago(EstadoPago.PAGADO);
        p1.getPago().setFechaPago(cena.getFechaCreacion().plusDays(5));

        // David paga muy tarde: 10 días
        p2.getPago().setEstadoPago(EstadoPago.PAGADO);
        p2.getPago().setFechaPago(cena.getFechaCreacion().plusDays(10));

        // Marta paga pronto: 1 día
        p3.getPago().setEstadoPago(EstadoPago.PAGADO);
        p3.getPago().setFechaPago(cena.getFechaCreacion().plusDays(1));

        // Carlos paga en dos eventos: media 4 días
        p4.getPago().setEstadoPago(EstadoPago.PAGADO);
        p4.getPago().setFechaPago(regalo.getFechaCreacion().plusDays(3));

        p6.getPago().setEstadoPago(EstadoPago.PAGADO);
        p6.getPago().setFechaPago(viaje.getFechaCreacion().plusDays(5));

        // Laura vuelve a pagar tarde: media Laura = (5 + 8) / 2 = 6.5
        p7.getPago().setEstadoPago(EstadoPago.PAGADO);
        p7.getPago().setFechaPago(viaje.getFechaCreacion().plusDays(8));

        // Marta queda pendiente en otro evento, no cuenta para el ranking
        p8.getPago().setEstadoPago(EstadoPago.PENDIENTE);

        RankingMoroso ranking = new RankingMoroso(gestor);
        ranking.mostrarRanking();
    }
}
