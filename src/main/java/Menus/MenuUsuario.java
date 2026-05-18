package Menus;

import App.*;
import Usuarios.Usuario;

public class MenuUsuario {
    private Usuario usuario;
    private GestorMorosos gestorMorosos;

    public MenuUsuario(GestorMorosos gestor) {
        this.gestorMorosos = gestor;
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


    public void crearEvento() {

    }

    public void anadirParticipantes() {

    }

    public void consultarEventosCreados() {
    }

    public void consultarEventosDondeParticipo() {
    }

    public void consultarTodosMisEventos() {
    }

    public void consultarPagosPendientes() {
    }

    public void confirmarPagos() {
    }

    public void verRankings() {
    }

    public void exportarMisEventos() {
    }
}
