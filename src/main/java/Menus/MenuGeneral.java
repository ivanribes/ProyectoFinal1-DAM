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

    private void mostrarUsuarios() {
        for (Usuario u: gestorMorosos.getUsuarios()) {
            System.out.println("User_ID: " + u.getId() + " --> " + u.getEmail());
        }
    }

    public Usuario seleccionarUsuario() {
        Usuario usuario = null;
        int id;

        mostrarUsuarios();
        /*Seleccionar uno de los usuarios por id para no poder confundir por nombre
         */
        id = Integer.parseInt(IO.readln("Introduce el id del usuario: "));

        for (Usuario u : gestorMorosos.getUsuarios()) {
            if (id == u.getId())  {
                usuario = u;
                System.out.println("Sesion iniciado como " + u.getNombre() + "✅\n");
                break;
            }
        }

        return usuario;
    }
}
