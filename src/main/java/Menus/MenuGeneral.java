package Menus;

import App.GestorMorosos;
import Usuarios.Usuario;

public class MenuGeneral {
    private GestorMorosos gestorMorosos;

    public static void mostrar() {

        System.out.println("""
                === MENU GENERAL ===
                1. Iniciar sesion
                2. Salir
                """);
    }

    private static void mostrarUsuarios() {
        System.out.println("Aqui mostramos los usuarios");
    }

    public static Usuario seleccionarUsuario() {
        Usuario usuario = null;

        /*Seleccionar uno de los usuarios por id para no poder confundir por nombre
         */

        return usuario;
    }
}
