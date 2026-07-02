package App;

import Enums.EstadoPago;
import Eventos.Evento;
import Excepciones.UnknownEventException;
import Excepciones.UnknownPaymentException;
import Excepciones.UnknownUserException;
import Ficheros.GestorFicheros;
import Menu.Menu;
import Pagos.Pago;
import Rankings.RankingEventosCreados;
import Rankings.RankingMoroso;
import Rankings.RankingPenalizacion;
import Usuarios.ParticipanteEvento;
import Usuarios.Usuario;
import Utilidades.Entrada;

import java.io.IOException;
import java.util.List;

public class ServiciosUsuario {

    private Usuario usuario;
    private final GestorMorosos gestorMorosos;

    public ServiciosUsuario(GestorMorosos gestor) {
        this.gestorMorosos = gestor;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    //region SELECCIONAR USUARIO
    public Usuario seleccionarUsuario() throws UnknownUserException {
        List<Usuario> usuarios = gestorMorosos.obtenerUsuariosDisponibles(usuario);

        for (Usuario u: usuarios) {
            System.out.println("ID -> " +u.getId()+ " - Email -> " +u.getEmail());
        }

        int id = Entrada.leerIntPositivo("Selecciona un ID: ");

        return gestorMorosos.buscarUsuarioActivoID(id);
    }

    private Usuario seleccionarUsuarioActivoParaEvento() throws UnknownUserException {
        mostrarUsuariosActivosDisponibles();

        int id = Entrada.leerIntPositivo("Selecciona un ID: ");

        return gestorMorosos.buscarUsuarioActivoID(id);
    }


    private void mostrarUsuariosActivosDisponibles() {
        for (Usuario u : gestorMorosos.obtenerUsuariosDisponibles(usuario)) {
            System.out.println("User_ID: " + u.getId() + " --> " + u.getEmail());
        }
    }
    //endregion

    //region CREAR EVENTO
    public void crearEvento() {
        if (usuario == null) {
            System.out.println("No hay ningún usuario seleccionado.");
            return;
        }

        if (!usuario.isActivo()) {
            System.out.println("No puedes crear eventos porque tu usuario está desactivado.");
            return;
        }

        boolean creado = gestorMorosos.aniadirEvento(
                new Evento(nombreEvento(), importeEvento(), usuario));

        if (creado) {
            System.out.println("Evento creado correctamente.");
        } else {
            System.out.println("No se ha podido crear el evento.");
        }
    }

    private String nombreEvento() {
        return Entrada.leerNombre("Introduce el nombre del evento: ");
    }

    private double importeEvento() {
        return Entrada.leerDoublePositivo("Introduce el importe del evento: ");
    }
    //endregion

    //region AÑADIR PARTICIPANTES
    public void aniadirParticipantes() {
        try {
            if (!consultarEventosCreados()) {
                return;
            }

            int idEvento = Entrada.leerIntPositivo("Selecciona la ID del evento: ");
            Evento evento = gestorMorosos.buscarEventoCreadoPorUsuario(idEvento, usuario);

            if (!puedeModificarParticipantes(evento)) {
                return;
            }

            boolean seguirAniadiendo;

            do {
                List<Usuario> usuariosDisponibles = gestorMorosos.obtenerUsuariosDisponiblesParaEvento(evento);

                if (usuariosDisponibles.isEmpty()) {
                    System.out.println("No hay usuarios disponibles para añadir a este evento.");
                    return;
                }

                mostrarUsuariosDisponibles(usuariosDisponibles);

                int idUsuario = Entrada.leerIntPositivo("Selecciona el ID del usuario a añadir: ");
                Usuario usuarioAniadir = buscarUsuarioEnLista(usuariosDisponibles, idUsuario);

                boolean aniadido = gestorMorosos.aniadirParticipanteAEvento(evento, usuarioAniadir);

                if (aniadido) {
                    System.out.printf("%s se ha añadido a %s ✅%n%n",
                            usuarioAniadir.getNombre(),
                            evento.getNombre());
                } else {
                    System.out.println("No se ha podido añadir el participante.");
                }

                seguirAniadiendo = Entrada.leerSiNo("¿Desea introducir más participantes? (si-no): ");

            } while (seguirAniadiendo);

        } catch (UnknownEventException | UnknownUserException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean puedeModificarParticipantes(Evento evento) {
        if (evento.tienePagosIniciados()) {
            System.out.println("No se puede modificar el evento porque ya hay pagos realizados o pendientes de confirmación.");
            return false;
        }

        return true;
    }

    private void mostrarUsuariosDisponibles(List<Usuario> usuariosDisponibles) {
        System.out.println("Usuarios disponibles:");

        for (Usuario usuario : usuariosDisponibles) {
            System.out.printf("""
                [ID USUARIO: %d]
                Nombre: %s
                Email: %s
                
                """,
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getEmail());
        }
    }

    private Usuario buscarUsuarioEnLista(List<Usuario> usuariosDisponibles, int idUsuario) {
        for (Usuario usuario : usuariosDisponibles) {
            if (usuario.getId() == idUsuario) {
                return usuario;
            }
        }

        throw new UnknownUserException();
    }
    //endregion

    //region ELIMINAR PARTICIPANTE
    public void eliminarParticipante() {
        try {
            if (!consultarEventosCreados()) {
                return;
            }

            int idEvento = Entrada.leerIntPositivo("Selecciona la ID del evento: ");
            Evento evento = gestorMorosos.buscarEventoCreadoPorUsuario(idEvento, usuario);

            if (!puedeModificarParticipantes(evento)) {
                return;
            }

            if (!evento.tieneParticipantes()) {
                System.out.println("Este evento no tiene participantes.");
                return;
            }

            mostrarParticipantesEvento(evento);

            int idParticipante = Entrada.leerIntPositivo("Introduce el ID del participante a eliminar: ");
            ParticipanteEvento participante = evento.buscarParticipantePorId(idParticipante);

            if (participante == null) {
                System.out.println("No existe ningún participante con ese ID en este evento.");
                return;
            }

            if (evento.eliminarParticipante(idParticipante)) {
                System.out.printf("%s ha sido eliminado del evento %s.%n%n",
                        participante.getUsuario().getNombre(),
                        evento.getNombre());
            }

        } catch (UnknownEventException e) {
            System.out.println(e.getMessage());
        }
    }

    private void mostrarParticipantesEvento(Evento evento) {
        System.out.printf("Participantes del evento %s:%n%n", evento.getNombre());

        for (ParticipanteEvento p : evento.getListParticipantes()) {
            System.out.printf("""
                        [ID PARTICIPANTE: %d]
                        Usuario: %s
                        Email: %s
                        Estado pago: %s
                        Importe: %.2f€
                        
                        """,
                    p.getIdParticipante(),
                    p.getUsuario().getNombre(),
                    p.getUsuario().getEmail(),
                    p.getPago().getEstadoPago(),
                    p.getPago().getImporteBase());
        }
    }
    //endregion

    //region CONSULTAR EVENTOS
    public boolean consultarEventosCreados() {
        boolean hayEventos = false;

        for (Evento e : gestorMorosos.getEventos()) {
            if (e.getCreador() == usuario) {
                hayEventos = true;
                mostrarEventoCreado(e);
            }
        }

        System.out.println();

        if (!hayEventos) {
            System.out.println("No hay eventos creados.");
        }

        return hayEventos;
    }

    public void consultarEventosDondeParticipo() {
        boolean hayEventos = false;

        for (Evento e : gestorMorosos.getEventos()) {
            for (ParticipanteEvento p : e.getListParticipantes()) {
                if (p.getUsuario() == usuario) {
                    hayEventos = true;
                    mostrarEventoParticipado(e, p);
                }
            }
        }

        System.out.println();

        if (!hayEventos) {
            System.out.println("No participas en ningún evento.\n");
        }
    }

    public void consultarTodosMisEventos() {
        System.out.println("Eventos creados: ");
        consultarEventosCreados();
        System.out.println("Eventos en los que participo: ");
        consultarEventosDondeParticipo();
    }

    private void mostrarEventoCreado(Evento e) {
        System.out.printf("""
                        [ID EVENTO: %d]
                        Nombre: %s
                        Importe total evento: %.2f€
                        Participantes: %d
                        Fecha creación: %s
                        Fecha límite: %s
                        
                        """,
                e.getId(),
                e.getNombre(),
                e.getImporteTotal(),
                e.getListParticipantes().size(),
                e.getFechaCreacion(),
                e.getFechaPagoLimite());
    }

    private void mostrarEventoParticipado(Evento e, ParticipanteEvento p) {
        System.out.printf("""
                        [ID EVENTO: %d]
                        Evento: %s
                        Estado pago: %s
                        Importe base: %.2f€
                        Penalización: %s
                        Importe total: %.2f€
                        
                        """,
                e.getId(),
                e.getNombre(),
                p.getPago().getEstadoPago(),
                p.getPago().getImporteBase(),
                formatearPenalizacion(p.getPago()),
                p.getPago().getImporteBase() + p.getPago().getPenalizacionAplicada());
    }
    //endregion

    //region PAGOS PENDIENTES
    public boolean consultarPagosPendientes() {
        boolean hayPendientes = false;

        for (Evento e : gestorMorosos.getEventos()) {
            for (ParticipanteEvento p : e.getListParticipantes()) {
                if (p.getUsuario() == usuario && p.getPago().puedeSerSaldado()) {
                    hayPendientes = true;
                    mostrarPagoPendiente(e, p);
                }
            }
        }

        if (!hayPendientes) {
            System.out.println("No hay pagos pendientes\n");
        }

        return hayPendientes;
    }

    public void saldarPagos() {
        try {
            if (!consultarPagosPendientes()) {
                return;
            }

            int idPago = Entrada.leerIntPositivo("Introduce el ID del pago a saldar: ");
            Pago pago = gestorMorosos.buscarPagoPendienteUsuario(idPago, usuario);

            if (pago.solicitarConfirmacion(gestorMorosos.getFechaModificada())) {
                System.out.println("Pago enviado para confirmación.");
            }

        } catch (UnknownPaymentException e) {
            System.out.println("No se ha encontrado un pago pendiente con esa ID.");
        }
    }

    private void mostrarPagoPendiente(Evento e, ParticipanteEvento p) {
        System.out.printf("""
                        [ID PAGO: %d]
                        Evento: %s
                        Estado: %s
                        Importe base: %.2f€
                        Penalización actual: %s
                        Total pendiente: %.2f€
                        Fecha límite: %s
                        
                        """,
                p.getPago().getId(),
                e.getNombre(),
                p.getPago().getEstadoPago(),
                p.getPago().getImporteBase(),
                formatearPenalizacion(p.getPago()),
                p.getPago().getImporteBase() + p.getPago().getPenalizacionAplicada(),
                e.getFechaPagoLimite());
    }
    //endregion

    //region CONFIRMAR PAGO
    public void confirmarPagos() {
        try {
            if (!consultarEventosCreados()) {
                return;
            }

            int idEvento = Entrada.leerIntPositivo("Introduce el ID del evento: ");
            Evento evento = gestorMorosos.buscarEventoCreadoPorUsuario(idEvento, usuario);

            if (!evento.tieneParticipantes()) {
                System.out.println("Este evento no tiene participantes.");
                return;
            }

            if (!mostrarPagosPendientesConfirmar(evento)) {
                System.out.println("No hay pagos pendientes de confirmar.\n");
                return;
            }

            int idPago = Entrada.leerIntPositivo("Introduce el ID del pago: ");
            Pago pago = gestorMorosos.buscarPagoPendienteConfirmar(evento, idPago);

            Menu.mostrarMenuPago();
            int opcion = Entrada.leerOpcionMenu("Selecciona una opción: ", 1, 3);

            switch (opcion) {
                case 1 -> pago.confirmar();
                case 2 -> pago.rechazar();
                case 3 -> System.out.println("Operación cancelada.");
            }

        } catch (UnknownEventException | UnknownPaymentException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean mostrarPagosPendientesConfirmar(Evento evento) {
        boolean hayPagos = false;

        System.out.println("PAGOS PENDIENTES DE CONFIRMAR:");

        for (ParticipanteEvento p : evento.getListParticipantes()) {
            if (p.getPago().getEstadoPago() == EstadoPago.PENDIENTE_CONFIRMAR) {
                hayPagos = true;
                mostrarPagoPendienteConfirmar(p);
            }
        }

        return hayPagos;
    }

    private void mostrarPagoPendienteConfirmar(ParticipanteEvento p) {
        System.out.printf("""
                        [ID PAGO: %d]
                        Usuario: %s
                        Estado: %s
                        Importe base: %.2f€
                        Penalización: %s
                        Importe total: %.2f€
                        Fecha pago: %s
                        
                        """,
                p.getPago().getId(),
                p.getUsuario().getNombre(),
                p.getPago().getEstadoPago(),
                p.getPago().getImporteBase(),
                formatearPenalizacion(p.getPago()),
                p.getPago().getImporteBase() + p.getPago().getPenalizacionAplicada(),
                p.getPago().getFechaPago() != null
                        ? p.getPago().getFechaPago()
                        : "No realizado");
    }
    //endregion

    //region RANKINGS
    public void verRankings() {
        Menu.mostrarMenuRankings();

        int opcion = Entrada.leerOpcionMenu("Selecciona una opción: ", 1, 4);

        switch (opcion) {
            case 1 -> {
                gestorMorosos.setRanking(new RankingMoroso(gestorMorosos));
                gestorMorosos.getRanking().mostrarRanking();
            }
            case 2 -> {
                gestorMorosos.setRanking(new RankingEventosCreados(gestorMorosos));
                gestorMorosos.getRanking().mostrarRanking();
            }
            case 3 -> {
                gestorMorosos.setRanking(new RankingPenalizacion(gestorMorosos));
                gestorMorosos.getRanking().mostrarRanking();
            }
            case 4 -> System.out.println();
        }
    }
    //endregion

    //region EXPORTAR MIS EVENTOS
    public void exportarMisEventos(Usuario usuario) throws IOException {
        GestorFicheros gestorFicheros = new GestorFicheros("Ficheros/", gestorMorosos);

        for (Evento e : gestorMorosos.getEventos()) {
            if (e.getCreador() == usuario) {
                gestorFicheros.generarRegistroEvento(e, usuario);
            }
        }
    }
    //endregion

    //region DESACTIVAR USUARIO
    public boolean desactivarUsuario() {
        if (!usuario.isActivo()) {
            System.out.println("El usuario ya está desactivado.");
            return false;
        }

        usuario.setActivo(false);
        System.out.println("Usuario desactivado correctamente.");
        return true;
    }
    //endregion

    //region UTILIDADES
    private String formatearPenalizacion(Pago pago) {
        return pago.getPenalizacionAplicada() > 0
                ? String.format("%.2f€", pago.getPenalizacionAplicada())
                : "Sin penalización";
    }
    //endregion
}
