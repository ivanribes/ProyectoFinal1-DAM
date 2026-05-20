package Rankings;

import App.GestorMorosos;
import Eventos.Evento;
import Usuarios.ParticipanteEvento;

import java.util.HashMap;

public class RankingPenalizacion extends Ranking{

    HashMap<Integer, Double> rankingTotalPenalizado = new HashMap<>();

    public RankingPenalizacion(GestorMorosos gestorMorosos) {
        super(gestorMorosos);
    }
    //Mas penalización acumulada lleva

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
