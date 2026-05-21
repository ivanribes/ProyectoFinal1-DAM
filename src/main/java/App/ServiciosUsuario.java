package App;

import Enums.EstadoPago;
import Eventos.Evento;
import Menu.Menu;
import Pagos.Pago;
import Rankings.Ranking;
import Rankings.RankingEventosCreados;
import Rankings.RankingMoroso;
import Rankings.RankingPenalizacion;
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
                    //region CONSULTAR EVENTOS DONDE PARTICIPO

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
                            p.getPago().getPenalizacionAplicada() > 0
                                    ? String.format("%.2f€", p.getPago().getPenalizacionAplicada())
                                    : "Sin penalización",
                            (p.getPago().getImporteBase() + p.getPago().getPenalizacionAplicada()));
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
                        ((p.getPago().getEstadoPago() == EstadoPago.PENDIENTE) ||
                                p.getPago().getEstadoPago() == EstadoPago.RECHAZADO)) {
                    hayPendientes = true;
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
                            p.getPago().getPenalizacionAplicada() > 0
                                    ? String.format("%.2f€", p.getPago().getPenalizacionAplicada())
                                    : "Sin penalización",
                            p.getPago().getImporteBase() + p.getPago().getPenalizacionAplicada(),
                            e.getFechaPagoLimite());
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
            if (pago != null) {
                pago.setEstadoPago(EstadoPago.PENDIENTE_CONFIRMAR);
                pago.setFechaPago(gestorMorosos.getFechaModificada());
            } else {
                System.out.println("No se ha encontrado el pago.");
            }

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
                //region CONFIRMAR PAGOS

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
                        p.getPago().getPenalizacionAplicada() > 0
                                ? String.format("%.2f€", p.getPago().getPenalizacionAplicada())
                                : "Sin penalización",
                        p.getPago().getImporteBase() + p.getPago().getPenalizacionAplicada(),
                        p.getPago().getFechaPago() != null
                                ? p.getPago().getFechaPago()
                                : "No realizado"
                );
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
        Menu.mostrarMenuRankings();

        int opcion = Integer.parseInt(IO.readln("Selecciona una opcion: "));

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
            default -> System.out.println("Opcion no valida.");
        }
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
