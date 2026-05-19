package Menus;

import App.GestorMorosos;
import Usuarios.Usuario;

public class MenuGeneral {
    private GestorMorosos gestorMorosos;

    public MenuGeneral(GestorMorosos gestor) {
        this.gestorMorosos = gestor;
    }

    public void mostrar() {

        System.out.println("""
                === MENU GENERAL ===
                1. Iniciar sesion
                2. Salir
                """);
    }
}
