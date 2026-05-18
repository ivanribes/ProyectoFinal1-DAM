package App;

import Menus.MenuGeneral;

public class MorososNo {
    public static void main(String[] args) {

        Aplicacion app = new Aplicacion();

        app.iniciar();

        if (app.getUsuarioActual() == null) {
            MenuGeneral.mostrar();
            app.seleccionarUsuario(MenuGeneral.seleccionarUsuario());
        }

    }
}
