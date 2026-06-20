package Rankings;

import App.GestorMorosos;
import Eventos.Evento;
import Usuarios.Usuario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankingEventosCreados extends Ranking{

    HashMap<Integer, Integer> rankingEventosCreados = new HashMap<>();

    public RankingEventosCreados(GestorMorosos gestorMorosos) {
        super(gestorMorosos);
    }
    //Mas eventos creados

    @Override
    protected void generarRanking() {

        rankingEventosCreados.clear();

        for (Usuario u : gestorMorosos.getUsuarios()) {
            int numEventos = 0;
            for (Evento e : gestorMorosos.getEventos()) {
                if (e.getCreador() == u) {
                    numEventos++;
                }
            }
            if (numEventos > 0) {
                rankingEventosCreados.put(u.getId(), numEventos);
            }
        }
    }

    @Override
    public void mostrarRanking() {
        generarRanking();

        List<Map.Entry<Integer, Integer>> lista =
                new ArrayList<>(rankingEventosCreados.entrySet());

        if (lista.isEmpty()) {
            System.out.println("Todavía no hay eventos creados para generar este ranking.\n");
            return;
        }

        lista.sort((a, b) ->
                Integer.compare(b.getValue(), a.getValue()));

        for (int i = 0; i < 3 && i < lista.size(); i++) {

            Map.Entry<Integer, Integer> entry = lista.get(i);

            Usuario usuario = gestorMorosos.buscarUsuarioID(entry.getKey());

            String medalla = "";

            switch (i) {
                case 0 -> medalla = "🥇";
                case 1 -> medalla = "🥈";
                case 2 -> medalla = "🥉";
            }

            System.out.printf(
                    "%s TOP %d -> Usuario: %S | Eventos creados: %d %n",
                    medalla,
                    i + 1,
                    usuario.getNombre(),
                    entry.getValue()
            );
        }

        System.out.println();
    }
}
