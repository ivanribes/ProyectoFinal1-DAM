package App;

import Enums.EstadoPago;
import Eventos.Evento;
import Menu.Menu;
import Pagos.Pago;
import Usuarios.ParticipanteEvento;
import Usuarios.Usuario;


public class ServiciosUsuario {
    private Usuario usuario;
    private GestorMorosos gestorMorosos;

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
        consultarEventosCreados();

        int idEvento = Integer.parseInt(IO.readln("Introduce el id del evento: "));
        Evento evento = null;

        for (Evento e : gestorMorosos.getEventos()) {
            if (e.getId() == idEvento) {
                evento = e;
                break;
            }
        }

        if (evento != null) {
            Usuario usuarioAniadir;
            gestorMorosos.mostrarUsuarios();

            do {
                usuarioAniadir = seleccionarUsuario();

                evento.aniadirParticipantes(new ParticipanteEvento(usuarioAniadir, evento,
                        evento.getImporteTotal()/evento.getParticipantes()));
                System.out.printf("%S se ha añadido a %S%n%n", usuarioAniadir.getNombre(),
                        evento.getNombre() );

            } while (IO.readln("Desea introducir mas participantes? (si-no): ")
                    .equalsIgnoreCase("si"));
        } else {
            System.out.println("No se ha encontrado el evento");
        }
    }
    //endregion

    //region CONSULTAR EVENTOS CREADOS

    public void consultarEventosCreados() {
        for (Evento e : gestorMorosos.getEventos()) {
            if (e.getCreador() == usuario) {
                System.out.printf("[ID: %d] - %s - Importe: %.2f€ %n%n",e.getId(), e.getNombre(), e.getImporteTotal());
            }
        }
    }
    //endregion

    //region CONSULTAR EVENTOS DONDE PARTICIPA

    public void consultarEventosDondeParticipo() {
        for (Evento e : gestorMorosos.getEventos()) {
            for (ParticipanteEvento p : e.getListParticipantes()) {
                if (p.getUsuario() == usuario) {
                    System.out.printf("[ID: %d] %S - %.2f€%n%n", e.getId(), e.getNombre(),
                            p.getPago().getImporte());
                }
            }
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
    public void saldarPagosPendientes() {
        int id;
        Pago pago;
        boolean hayPendientes = false;

        for (Evento e : gestorMorosos.getEventos()) {
            for (ParticipanteEvento p : e.getListParticipantes()) {
                if ((p.getUsuario() == usuario) && (!p.getPago().getEstadoPago()
                        .equals(EstadoPago.PAGADO))) {
                    hayPendientes = true;
                    System.out.printf("[ID PAGO: %d] %S - %S %.2f€%n%n", p.getPago().getId(),
                            e.getNombre(),
                            p.getPago().getEstadoPago().toString() ,p.getPago().getImporte());
                }
            }
        }

        if (hayPendientes) {
            id = Integer.parseInt(IO.readln("Introduce el ID del pago a confirmar: "));

            pago = gestorMorosos.buscarPago(id);

            pago.setEstadoPago(EstadoPago.PENDIENTE_CONFIRMAR);
            System.out.println(pago.getEstadoPago());
        } else {
            System.out.println("No hay pagos pendientes");
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
            if (p.getPago().getEstadoPago().equals(EstadoPago.PENDIENTE_CONFIRMAR)) {
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
                case 1-> pago.setEstadoPago(EstadoPago.PAGADO);
                case 2-> pago.setEstadoPago(EstadoPago.RECHAZADO);
                case 3-> pago.setEstadoPago(EstadoPago.PENDIENTE_CONFIRMAR);
                default -> System.out.printf("Opcion no valida%n");
            }
        } else {
            System.out.println("No hay pagos pendientes de confirmar.");
        }
    }
    //endregion

    //region RANKINGS
    public void verRankings() {

    }
    //endregion

    //region EXPORTAR MIS EVENTOS

    public void exportarMisEventos() {

    }
    //endregion


}
