package Rankings;

import App.GestorMorosos;
import Eventos.Evento;
import Usuarios.ParticipanteEvento;
import java.util.HashMap;

public class RankingEventosCreados extends Ranking{

    HashMap<Integer, Double> rankingEventosCreados = new HashMap<>();

    public RankingEventosCreados(GestorMorosos gestorMorosos) {
        super(gestorMorosos);
    }
    //Mas eventos creados

    @Override
    public void generarRanking() {
        for (Evento e : gestorMorosos.getEventos()) {
            for (ParticipanteEvento p : e.getListParticipantes()) {

            }
        }
    }

    @Override public void mostrarRanking() {

    }
}
