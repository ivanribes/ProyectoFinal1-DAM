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

public class RankingPenalizacion extends Ranking {

    HashMap<Integer, Double> rankingTotalPenalizado = new HashMap<>();

    public RankingPenalizacion(GestorMorosos gestorMorosos) {
        super(gestorMorosos);
    }

    @Override
    protected void generarRanking() {

        rankingTotalPenalizado.clear();

        for (Usuario u : gestorMorosos.getUsuarios()) {
            double cantPenalizado = 0;
            for (Evento e : gestorMorosos.getEventos()) {
                for (ParticipanteEvento p : e.getListParticipantes()) {
                    if (p.getUsuario() == u && p.getPago().getEstadoPago() == EstadoPago.PAGADO) {
                        cantPenalizado += p.getPago().getPenalizacionAplicada();
                    }
                }
            }
            if (cantPenalizado > 0) {
                rankingTotalPenalizado.put(u.getId(), cantPenalizado);
            }
        }
    }

    @Override
    public void mostrarRanking() {
        generarRanking();

        List<Map.Entry<Integer, Double>> lista =
                new ArrayList<>(rankingTotalPenalizado.entrySet());

        if (lista.isEmpty()) {
            System.out.println("Todavía no hay penalizaciones pagadas para generar este ranking.\n");
            return;
        }

        lista.sort((a, b) ->
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
                    "%s TOP %d -> Usuario: %S | Penalización total pagada: %.2f€ %n",
                    medalla,
                    i + 1,
                    usuario.getNombre(),
                    entry.getValue()
            );
        }

        System.out.println();
    }
}
