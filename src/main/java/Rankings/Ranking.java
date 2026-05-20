package Rankings;

import App.GestorMorosos;

public abstract class Ranking {
    protected GestorMorosos gestorMorosos;

    public Ranking(GestorMorosos gestorMorosos) {
        this.gestorMorosos = gestorMorosos;
    }

    public abstract void generarRanking();
    public abstract void mostrarRanking();
}
