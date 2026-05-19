package App;

import Menus.MenuGeneral;
import Menus.MenuRankings;
import Menus.MenuUsuario;

public class MorososNo {
    public static void main(String[] args) {


        GestorMorosos gestor = new GestorMorosos();
        Aplicacion app = new Aplicacion(new MenuGeneral(gestor), new MenuUsuario(gestor),
                new MenuRankings(gestor), gestor);

        app.iniciar();
    }
}
