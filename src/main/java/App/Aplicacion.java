package App;

import Menu.Menu;
import Usuarios.Usuario;

public class Aplicacion {
    private Usuario usuarioActual;
    private final GestorMorosos gestorMorosos;
    private ServiciosUsuario serviciosUsuario;

    public Aplicacion(GestorMorosos gestorMorosos, ServiciosUsuario serviciosUsuario) {
        this.usuarioActual = null;
        this.gestorMorosos = gestorMorosos;
        this.serviciosUsuario = serviciosUsuario;
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
                Menu.mostrarMenuGeneral();

                opcion = Integer.parseInt(IO.readln("Selecciona una opcion: "));

                if (opcion == 1) {
                    this.usuarioActual =  serviciosUsuario.seleccionarUsuario();
                    serviciosUsuario.setUsuario(usuarioActual);
                    System.out.println("Sesion iniciado como " + usuarioActual.getNombre() + "✅\n");
                } else if (opcion == 2) {
                    continuar = false;
                    System.out.println("Cerrando aplicación...");
                } else {
                    System.out.println("Opción no valida.");
                }
            } else {
                Menu.mostrarMenuUsuario();

                opcion = Integer.parseInt(IO.readln("Selecciona una opcion: "));

                switch (opcion) {
                    case 1 -> serviciosUsuario.crearEvento();
                    case 2 -> serviciosUsuario.anadirParticipantes();
                    case 3 -> serviciosUsuario.consultarEventosCreados();
                    case 4 -> serviciosUsuario.consultarEventosDondeParticipo();
                    case 5 -> serviciosUsuario.consultarTodosMisEventos();
                    case 6 -> serviciosUsuario.consultarPagosPendientes();
                    case 7 -> serviciosUsuario.confirmarPagos();
                    case 8 -> serviciosUsuario.verRankings();
                    case 9 -> serviciosUsuario.exportarMisEventos();
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
        this.usuarioActual = serviciosUsuario.seleccionarUsuario();
    }


    public void cerrarSesion() {
        this.usuarioActual = null;
        System.out.println("Se ha cerrado la sesion 🔒\n");
    }
}
