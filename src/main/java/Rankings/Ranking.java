package Rankings;

import App.GestorMorosos;

public abstract class Ranking {
    protected GestorMorosos gestorMorosos;

    protected Ranking(GestorMorosos gestorMorosos) {
        this.gestorMorosos = gestorMorosos;
    }

    protected abstract void generarRanking();
    public abstract void mostrarRanking();
}
