package Menus;

import App.*;
import Eventos.Evento;
import Usuarios.Usuario;

public class MenuUsuario {
    private Usuario usuario;
    private GestorMorosos gestorMorosos;

    public MenuUsuario(GestorMorosos gestor) {
        this.gestorMorosos = gestor;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void mostrar() {
        System.out.println("""
                1. Crear evento
                2. Añadir participantes
                3. Consultar eventos creados
                4. Consultar eventos donde participo
                5. Consultar todos mis eventos
                6. Consultar pagos pendientes
                7. Confirmar pagos
                8. Ver rankings
                9. Exportar mis eventos
                10. Cerrar sesión
                """);
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

    }

    //endregion

    //region CONSULTAR EVENTOS CREADOS

    public void consultarEventosCreados() {
        for (Evento e : gestorMorosos.getEventos()) {
            if (e.getCreador() == usuario) {
                System.out.printf("[ID: %d] - %s %n",e.getId(), e.getNombre());
            }
        }
    }

    //endregion

    //region CONSULTAR EVENTOS DONDE PARTICIPA

    public void consultarEventosDondeParticipo() {
    }

    //endregion

    //region CONSULTAR TODOS LOS EVENTOS

    public void consultarTodosMisEventos() {
    }

    //endregion


    //region CONSULTAR PAGOS PENDIENTES
    public void consultarPagosPendientes() {
    }

    //endregion

    //region CONFIRMAR PAGO

    public void confirmarPagos() {
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
