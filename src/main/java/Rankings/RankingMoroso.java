package Rankings;

import App.GestorMorosos;
import Enums.EstadoPago;
import Eventos.Evento;
import Usuarios.ParticipanteEvento;
import Usuarios.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankingMoroso extends Ranking{

    HashMap<Integer, Double> rankingTiempoPago = new HashMap<>();

    public RankingMoroso(GestorMorosos gestorMorosos) {
        super(gestorMorosos);
    }
    //Mas dias tarda en pagar

    @Override protected void generarRanking() {

        rankingTiempoPago.clear();

        for (Usuario u : gestorMorosos.getUsuarios()) {
            int numPagos = 0;
            int dias = 0;
            for (Evento e : gestorMorosos.getEventos()) {
                for (ParticipanteEvento p : e.getListParticipantes()) {
                    if (p.getUsuario() == u && p.getPago().getEstadoPago() == EstadoPago.PAGADO) {
                        numPagos++;
                        dias += p.getPago().calcularDias(e.getFechaCreacion(), p.getPago()
                                .getFechaPago());
                    }
                }
            }

            if (numPagos > 0) {
                rankingTiempoPago.put(u.getId(), mediaTiempo(dias, numPagos));
            }
        }
    }

    private double mediaTiempo(int dias, int numPagos) {
        return (double) dias /numPagos;
    }

    @Override public void mostrarRanking() {


        generarRanking();

        //TODO esto en el super?? crear un String en un metodo y otro que solo lo muestre??

        List<Map.Entry<Integer, Double>> lista = new ArrayList<>(rankingTiempoPago.entrySet());

        lista.sort((a,b)->
                Double.compare(b.getValue(), a.getValue()));

        for (int i = 0; i < 3 && i < lista.size(); i++) {

            Map.Entry<Integer, Double> entry = lista.get(i);
            Usuario usuario = gestorMorosos.buscarUsuarioID(entry.getKey());

            String medalla = "";
            switch (i) {
                case 0 -> medalla = "🥇";
                case 1 -> medalla = "🥈";
                case 2 -> medalla = "🥉";
            }

            System.out.printf(
                    "%s TOP %d -> Usuario: %S | Tiempo medio de pago: %.2f días%n",
                    medalla,
                    i + 1,
                    usuario.getNombre(),
                    entry.getValue()
            );
        }
    }
}
