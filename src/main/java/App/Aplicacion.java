package App;

import Excepciones.UnknownUserException;
import Menu.Menu;
import Prueba.DatosPrueba;
import Usuarios.Usuario;
import java.io.IOException;

public class Aplicacion {
    private final GestorMorosos gestorMorosos;
    private Usuario usuarioActual;
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
        DatosPrueba.cargarDatosPrueba(gestorMorosos);
        ejecutarPrograma();
    }

    private void ejecutarPrograma() {
        int opcion;
        boolean continuar = true;

        do {
            if (usuarioActual == null) {
                Menu.mostrarMenuGeneral();

                opcion = Integer.parseInt(IO.readln("Selecciona una opcion: "));

                switch (opcion) {
                    case 1-> {
                        try {
                            this.usuarioActual = serviciosUsuario.seleccionarUsuario();
                            serviciosUsuario.setUsuario(usuarioActual);
                            System.out.println("Sesión iniciado como " + usuarioActual.getNombre() + "✅\n");
                        } catch (UnknownUserException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    case 2-> System.out.println("Pedir los datos y crear un nuevo usuario");
                    case 3->{
                        gestorMorosos.sumarDias();
                        gestorMorosos.actualizarPenalizaciones();
                    }
                    case 4->gestorMorosos.mostrarFecha();
                    case 5-> {
                        continuar = false;
                        System.out.println("Cerrando aplicación...");
                    }
                    default -> System.out.println("Opcion no valida");
                }
            } else {
                Menu.mostrarMenuUsuario();

                opcion = Integer.parseInt(IO.readln("Selecciona una opción: "));

                switch (opcion) {
                    case 1 -> serviciosUsuario.crearEvento();
                    case 2 -> serviciosUsuario.anadirParticipantes();
                    case 3 -> serviciosUsuario.consultarEventosCreados();
                    case 4 -> serviciosUsuario.consultarEventosDondeParticipo();
                    case 5 -> serviciosUsuario.consultarTodosMisEventos();
                    case 6 -> serviciosUsuario.consultarPagosPendientes();
                    case 7 -> serviciosUsuario.saldarPagos();
                    case 8 -> serviciosUsuario.confirmarPagos();
                    case 9 -> serviciosUsuario.verRankings();
                    case 10 -> {
                        try {
                            serviciosUsuario.exportarMisEventos(usuarioActual);
                        } catch (IOException e) {
                            System.out.println("Error al exportar los eventos.");
                        }
                    }
                    case 11 -> serviciosUsuario.desactivarUsuario();
                    case 12 -> cerrarSesion();
                    default -> System.out.println("Opción no válida");
                }
            }
        } while (continuar);
    }

    private void cargarUsuarios() {
        System.out.println("Cargando aplicacion...");

        gestorMorosos.aniadirUsuario(
                new Usuario("Carlos Martinez", "carlos.martinez@gmail.com"));
        gestorMorosos.aniadirUsuario(
                new Usuario( "Laura Gomez", "laura.gomez@gmail.com"));
        gestorMorosos.aniadirUsuario(
                new Usuario( "David Fernandez", "david.fernandez@gmail.com"));
        gestorMorosos.aniadirUsuario(
                new Usuario( "Marta Lopez", "marta.lopez@gmail.com"));
        gestorMorosos.aniadirUsuario(
                new Usuario( "Sergio Navarro", "sergio.navarro@gmail.com"));
        gestorMorosos.aniadirUsuario(
                new Usuario( "Andrea Ruiz", "andrea.ruiz@gmail.com"));
        gestorMorosos.aniadirUsuario(
                new Usuario( "Pablo Torres", "pablo.torres@gmail.com"));
        gestorMorosos.aniadirUsuario(
                new Usuario( "Lucia Moreno", "lucia.moreno@gmail.com"));
        gestorMorosos.aniadirUsuario(
                new Usuario( "Javier Castillo", "javier.castillo@gmail.com"));
        gestorMorosos.aniadirUsuario(
                new Usuario( "Elena Romero", "elena.romero@gmail.com"));

        // En caso de implementar base de datos se eliminaria esta carga "manual"
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
