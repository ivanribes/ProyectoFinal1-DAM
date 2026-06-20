package App;

import Excepciones.UnknownUserException;
import Menu.Menu;
import Prueba.DatosPrueba;
import Usuarios.Usuario;
import Utilidades.Entrada;

import java.io.IOException;

public class Aplicacion {

    private final GestorMorosos gestorMorosos;
    private final ServiciosUsuario serviciosUsuario;
    private Usuario usuarioActual;

    public Aplicacion(GestorMorosos gestorMorosos, ServiciosUsuario serviciosUsuario) {
        this.usuarioActual = null;
        this.gestorMorosos = gestorMorosos;
        this.serviciosUsuario = serviciosUsuario;
    }

    public GestorMorosos getGestorMorosos() {
        return gestorMorosos;
    }

    public Usuario getUsuarioActual() {
        return usuarioActual;
    }

    public void iniciar() {
        cargarUsuarios();
        DatosPrueba.cargarDatosPrueba(gestorMorosos);
        ejecutarPrograma();
    }

    //region FLUJO PRINCIPAL
    private void ejecutarPrograma() {
        boolean continuar = true;

        do {
            if (usuarioActual == null) {
                continuar = ejecutarMenuGeneral();
            } else {
                ejecutarMenuUsuario();
            }
        } while (continuar);
    }

    private boolean ejecutarMenuGeneral() {
        Menu.mostrarMenuGeneral();

        switch (Entrada.leerOpcionMenu("Selecciona una opcion: ", 1, 5)) {
            case 1 -> iniciarSesion();
            case 2 -> System.out.println("Pedir los datos y crear un nuevo usuario");
            case 3 -> {
                gestorMorosos.sumarDias();
                gestorMorosos.actualizarPenalizaciones();
            }
            case 4 -> gestorMorosos.mostrarFecha();
            case 5 -> {
                System.out.println("Cerrando aplicación...");
                return false;
            }
        }

        return true;
    }

    private void ejecutarMenuUsuario() {
        Menu.mostrarMenuUsuario();

        switch (Entrada.leerOpcionMenu("Selecciona una opcion: ", 1, 13)) {
            case 1 -> serviciosUsuario.crearEvento();
            case 2 -> serviciosUsuario.anadirParticipantes();
            case 3 -> {
                int dias = Entrada.leerIntPositivo(
                        "Introduce la cantidad de dias que quieres aumentar la fecha: ");

                gestorMorosos.sumarDias(dias);
                gestorMorosos.actualizarPenalizaciones();
            }
            case 4 -> System.out.println(gestorMorosos.getFechaModificada());
            case 5 -> serviciosUsuario.consultarEventosDondeParticipo();
            case 6 -> serviciosUsuario.consultarTodosMisEventos();
            case 7 -> serviciosUsuario.consultarPagosPendientes();
            case 8 -> serviciosUsuario.saldarPagos();
            case 9 -> serviciosUsuario.confirmarPagos();
            case 10 -> serviciosUsuario.verRankings();
            case 11 -> exportarEventos();
            case 12 -> serviciosUsuario.desactivarUsuario();
            case 13 -> cerrarSesion();
        }
    }
    //endregion

    //region SESIÓN
    private void iniciarSesion() {
        try {
            this.usuarioActual = serviciosUsuario.seleccionarUsuario();
            serviciosUsuario.setUsuario(usuarioActual);
            System.out.println("Sesión iniciado como " + usuarioActual.getNombre() + "✅\n");
        } catch (UnknownUserException e) {
            System.out.println(e.getMessage());
        }
    }

    public void seleccionarUsuario() {
        this.usuarioActual = serviciosUsuario.seleccionarUsuario();
    }

    public void cerrarSesion() {
        this.usuarioActual = null;
        System.out.println("Se ha cerrado la sesion 🔒\n");
    }
    //endregion

    //region EXPORTAR
    private void exportarEventos() {
        try {
            serviciosUsuario.exportarMisEventos(usuarioActual);
        } catch (IOException e) {
            System.out.println("Error al exportar los eventos.");
        }
    }
    //endregion

    //region CARGA INICIAL
    private void cargarUsuarios() {
        System.out.println("Cargando aplicacion...");

        gestorMorosos.aniadirUsuario(
                new Usuario("Carlos Martinez", "carlos.martinez@gmail.com"));
        gestorMorosos.aniadirUsuario(
                new Usuario("Laura Gomez", "laura.gomez@gmail.com"));
        gestorMorosos.aniadirUsuario(
                new Usuario("David Fernandez", "david.fernandez@gmail.com"));
        gestorMorosos.aniadirUsuario(
                new Usuario("Marta Lopez", "marta.lopez@gmail.com"));
        gestorMorosos.aniadirUsuario(
                new Usuario("Sergio Navarro", "sergio.navarro@gmail.com"));
        gestorMorosos.aniadirUsuario(
                new Usuario("Andrea Ruiz", "andrea.ruiz@gmail.com"));
        gestorMorosos.aniadirUsuario(
                new Usuario("Pablo Torres", "pablo.torres@gmail.com"));
        gestorMorosos.aniadirUsuario(
                new Usuario("Lucia Moreno", "lucia.moreno@gmail.com"));
        gestorMorosos.aniadirUsuario(
                new Usuario("Javier Castillo", "javier.castillo@gmail.com"));
        gestorMorosos.aniadirUsuario(
                new Usuario("Elena Romero", "elena.romero@gmail.com"));

        // En caso de implementar base de datos se eliminaria esta carga "manual"
    }
    //endregion
}
