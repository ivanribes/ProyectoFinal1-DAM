package App;

import Menus.MenuGeneral;
import Menus.MenuRankings;
import Menus.MenuUsuario;
import Usuarios.Usuario;

public class Aplicacion {
    private Usuario usuarioActual;
    private final MenuGeneral menuGeneral;
    private final MenuUsuario menuUsuario;
    private final MenuRankings menuRankings;
    private final GestorMorosos gestorMorosos;

    public Aplicacion(MenuGeneral menuGeneral, MenuUsuario menuUsuario, MenuRankings menuRankings,
                      GestorMorosos gestorMorosos) {
        this.usuarioActual = null;
        this.menuGeneral = menuGeneral;
        this.menuUsuario = menuUsuario;
        this.menuRankings = menuRankings;
        this.gestorMorosos = gestorMorosos;
    }

    public MenuGeneral getMenuGeneral() {
        return menuGeneral;
    }

    public MenuUsuario getMenuUsuario() {
        return menuUsuario;
    }

    public MenuRankings getMenuRankings() {
        return menuRankings;
    }

    public GestorMorosos getGestorMorosos() {
        return gestorMorosos;
    }

    public void iniciar() {
        cargarUsuarios();
        ejecutarPrograma();
    }

    private void ejecutarPrograma() {
        int opcion;
        boolean continuar = true;

        do {
            if (usuarioActual == null) {
                menuGeneral.mostrar();

                opcion = Integer.parseInt(IO.readln("Selecciona una opcion: "));

                if (opcion == 1) {
                    this.usuarioActual =  menuUsuario.seleccionarUsuario();
                    menuUsuario.setUsuario(usuarioActual);
                    System.out.println("Sesion iniciado como " + usuarioActual.getNombre() + "✅\n");
                } else if (opcion == 2) {
                    continuar = false;
                    System.out.println("Cerrando aplicación...");
                } else {
                    System.out.println("Opción no valida.");
                }
            } else {
                menuUsuario.mostrar();

                opcion = Integer.parseInt(IO.readln("Selecciona una opcion: "));

                switch (opcion) {
                    case 1 -> menuUsuario.crearEvento();
                    case 2 -> menuUsuario.anadirParticipantes();
                    case 3 -> menuUsuario.consultarEventosCreados();
                    case 4 -> menuUsuario.consultarEventosDondeParticipo();
                    case 5 -> menuUsuario.consultarTodosMisEventos();
                    case 6 -> menuUsuario.consultarPagosPendientes();
                    case 7 -> menuUsuario.confirmarPagos();
                    case 8 -> menuUsuario.verRankings();
                    case 9 -> menuUsuario.exportarMisEventos();
                    case 10 -> cerrarSesion();
                    default -> System.out.println("Opción no válida");
                }
            }
        } while (continuar);
    }

    private void cargarUsuarios() {
        System.out.println("Cargando aplicacion...");

        gestorMorosos.aniadirUsuario(
                new Usuario(1, "Carlos Martinez", "carlos.martinez@gmail" + ".com"));
        gestorMorosos.aniadirUsuario(new Usuario(2, "Laura Gomez", "laura.gomez@gmail.com"));
        gestorMorosos.aniadirUsuario(
                new Usuario(3, "David Fernandez", "david.fernandez@gmail" + ".com"));
        gestorMorosos.aniadirUsuario(new Usuario(4, "Marta Lopez", "marta.lopez@gmail.com"));
        gestorMorosos.aniadirUsuario(new Usuario(5, "Sergio Navarro", "sergio.navarro@gmail.com"));
        gestorMorosos.aniadirUsuario(new Usuario(6, "Andrea Ruiz", "andrea.ruiz@gmail.com"));
        gestorMorosos.aniadirUsuario(new Usuario(7, "Pablo Torres", "pablo.torres@gmail.com"));
        gestorMorosos.aniadirUsuario(new Usuario(8, "Lucia Moreno", "lucia.moreno@gmail.com"));
        gestorMorosos.aniadirUsuario(
                new Usuario(9, "Javier Castillo", "javier.castillo@gmail" + ".com"));
        gestorMorosos.aniadirUsuario(new Usuario(10, "Elena Romero", "elena.romero@gmail.com"));

        // En caso de implementar base de datos se eliminaria esta carga manual
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void seleccionarUsuario() {
        this.usuarioActual = menuUsuario.seleccionarUsuario();
    }


    public void cerrarSesion() {
        this.usuarioActual = null;
        System.out.println("Se ha cerrado la sesion 🔒\n");
    }
}
