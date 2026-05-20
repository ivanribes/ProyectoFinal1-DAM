package App;

import Enums.EstadoPago;
import Eventos.Evento;
import Menu.Menu;
import Pagos.Pago;
import Usuarios.ParticipanteEvento;
import Usuarios.Usuario;

public class ServiciosUsuario {
    private Usuario usuario;
    private final GestorMorosos gestorMorosos;

    public ServiciosUsuario(GestorMorosos gestor) {
        this.gestorMorosos = gestor;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario seleccionarUsuario() {
        gestorMorosos.mostrarUsuarios();

        int id = Integer.parseInt(IO.readln("Introduce el ID del usuario: "));

        return gestorMorosos.buscarUsuarioID(id);
    }

    //region CREAR EVENTO
    public void crearEvento() {
        gestorMorosos.aniadirEvento(new Evento(nombreEvento(), importeEvento(), usuario));
    }

    private String nombreEvento() {
        return IO.readln("Introduce el nombre del evento: ");
    }

    private double importeEvento() {
        return Double.parseDouble(IO.readln("Introduce el importe total: "));
    }

    //endregion

    //region AÑADIR PARTICIPANTES
    public void anadirParticipantes() {
        Evento evento;
        if (consultarEventosCreados()) {
            int idEvento = Integer.parseInt(IO.readln("Introduce el id del evento: "));

            evento = gestorMorosos.buscarEvento(idEvento);

            if (evento != null) {
                Usuario usuarioAniadir;
                gestorMorosos.mostrarUsuarios();

                do {
                    usuarioAniadir = seleccionarUsuario();

                    evento.aniadirParticipantes(new ParticipanteEvento(usuarioAniadir, evento,
                            evento.getImporteTotal() / evento.getParticipantes()));
                    System.out.printf("%S se ha añadido a %S👤✅%n%n", usuarioAniadir.getNombre(),
                            evento.getNombre());

                } while (IO.readln("Desea introducir mas participantes? (si-no): ")
                        .equalsIgnoreCase("si"));
            } else {
                System.out.println("No se ha encontrado el evento⚠️\n");
            }
        } else {
            System.out.println("No hay eventos creados.\n");
        }
    }
    //endregion

    //region CONSULTAR EVENTOS CREADOS

    public boolean consultarEventosCreados() {
        boolean hayEventos = false;
        for (Evento e : gestorMorosos.getEventos()) {
            if (e.getCreador() == usuario) {
                hayEventos = true;
                System.out.printf("[ID: %d] - %s - Importe Total: %.2f€ %n%n", e.getId(),
                        e.getNombre(), e.getImporteTotal());
            }
        }

        System.out.println();
        if (!hayEventos) {
            System.out.println("No hay eventos creados.");
        }
        return hayEventos;
    }
    //endregion

    //region CONSULTAR EVENTOS DONDE PARTICIPA

    public void consultarEventosDondeParticipo() {
        boolean hayEventos = false;
        for (Evento e : gestorMorosos.getEventos()) {
            for (ParticipanteEvento p : e.getListParticipantes()) {
                if (p.getUsuario() == usuario) {
                    hayEventos = true;
                    System.out.printf("[ID: %d] %S - %.2f€ %S%n%n", e.getId(), e.getNombre(),
                            p.getPago().getImporte(), p.getPago().getEstadoPago());
                }
            }
        }

        System.out.println();
        if (!hayEventos) {
            System.out.println("No participas en ningún evento.\n");
        }
    }
    //endregion

    //region CONSULTAR TODOS LOS EVENTOS

    public void consultarTodosMisEventos() {
        System.out.println("Eventos creados: ");
        consultarEventosCreados();
        System.out.println("Eventos en los que participo: ");
        consultarEventosDondeParticipo();
    }
    //endregion

    //region CONSULTAR PAGOS PENDIENTES
    public boolean consultarPagosPendientes() {
        boolean hayPendientes = false;

        for (Evento e : gestorMorosos.getEventos()) {
            for (ParticipanteEvento p : e.getListParticipantes()) {
                if ((p.getUsuario() == usuario) &&
                        (p.getPago().getEstadoPago() == EstadoPago.PENDIENTE)) {
                    hayPendientes = true;
                    System.out.printf("[ID PAGO: %d] %S - %S %.2f€%n%n", p.getPago().getId(),
                            e.getNombre(),
                            p.getPago().getEstadoPago(), p.getPago().getImporte());
                }
            }
        }
        return hayPendientes;
    }
    //endregion

    //region SALDAR PAGOS
    public void saldarPagos() {
        int id;
        Pago pago;
        if (consultarPagosPendientes()) {

            id = Integer.parseInt(IO.readln("Introduce el ID del pago a confirmar: "));

            pago = gestorMorosos.buscarPago(id);

            pago.setEstadoPago(EstadoPago.PENDIENTE_CONFIRMAR);
            System.out.println(pago.getEstadoPago());

        } else {
            System.out.println("No hay pagos pendientes\n");
        }
    }
    //endregion

    //region CONFIRMAR PAGO

    public void confirmarPagos() {
        Evento evento;
        Pago pago;
        int opcion;
        boolean hayPagos = false;
        consultarEventosCreados();

        int id = Integer.parseInt(IO.readln("Introduce el ID del evento: "));

        evento = gestorMorosos.buscarEvento(id);

        System.out.println("PAGOS PENDIENTES DE CONFIRMAR:");
        for (ParticipanteEvento p : evento.getListParticipantes()) {
            if (p.getPago().getEstadoPago() == EstadoPago.PENDIENTE_CONFIRMAR) {
                System.out.printf("[ID: %d] %S %.2f€%n", p.getPago().getId(),
                        p.getUsuario().getNombre(), p.getPago().getImporte());
                hayPagos = true;
            }
        }

        if (hayPagos) {

            id = Integer.parseInt(IO.readln("Introduce el ID del pago: "));

            pago = gestorMorosos.buscarPago(evento, id);

            Menu.mostrarMenuPago();
            opcion = Integer.parseInt(IO.readln("Selecciona una opcion: "));

            switch (opcion) {
                case 1 -> pago.setEstadoPago(EstadoPago.PAGADO);
                case 2 -> pago.setEstadoPago(EstadoPago.RECHAZADO);
                case 3 -> pago.setEstadoPago(EstadoPago.PENDIENTE_CONFIRMAR);
                default -> System.out.printf("Opción no valida%n");
            }
        } else {
            System.out.println("No hay pagos pendientes de confirmar.\n");
        }
    }
    //endregion

    //region RANKINGS
    public void verRankings() {
        System.out.println("menu ranking y demás movidas");
    }
    //endregion

    //region EXPORTAR MIS EVENTOS

    public void exportarMisEventos() {
        System.out.println("ficheros y tal");
    }
    //endregion

    //region DESACTIVAR USUARIO
    public void desactivarUsuario() {
        usuario.setActivo(false);
    }
    //endregion
}
