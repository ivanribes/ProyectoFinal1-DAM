package App;

import Menus.MenuGeneral;
import Menus.MenuRankings;
import Menus.MenuUsuario;
import Usuarios.Usuario;

public class Aplicacion {
    private Usuario usuarioActual;
    private MenuGeneral menuGeneral;
    private MenuUsuario menuUsuario;
    private MenuRankings menuRankings;
    private GestorMorosos gestorMorosos = new GestorMorosos();

    public void iniciar() {

        System.out.println("Cargando aplicacion...");
        gestorMorosos.getUsuarios().add(new Usuario(1, "Carlos Martinez",
                "carlos.martinez@gmail" + ".com"));
        gestorMorosos.getUsuarios().add(new Usuario(2, "Laura Gomez",
                "laura.gomez@gmail.com"));
        gestorMorosos.getUsuarios().add(new Usuario(3, "David Fernandez",
                "david.fernandez@gmail" + ".com"));
        gestorMorosos.getUsuarios().add(new Usuario(4, "Marta Lopez",
                "marta.lopez@gmail.com"));
        gestorMorosos.getUsuarios().add(new Usuario(5, "Sergio Navarro",
                "sergio.navarro@gmail" + ".com"));
        gestorMorosos.getUsuarios().add(new Usuario(6, "Andrea Ruiz",
                "andrea.ruiz@gmail.com"));
        gestorMorosos.getUsuarios().add(new Usuario(7, "Pablo Torres",
                "pablo.torres@gmail.com"));
        gestorMorosos.getUsuarios().add(new Usuario(8, "Lucia Moreno",
                "lucia.moreno@gmail.com"));
        gestorMorosos.getUsuarios().add(new Usuario(9, "Javier Castillo",
                "javier.castillo@gmail" + ".com"));
        gestorMorosos.getUsuarios().add(new Usuario(10, "Elena Romero",
                "elena.romero@gmail.com"));

        // En caso de implementar base de datos se eliminaria esta carga manual
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void seleccionarUsuario(Usuario user) {
        this.usuarioActual = user;
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
    }
}
