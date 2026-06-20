package Menu;

public class Menu {

    public static void mostrarMenuGeneral() {

        System.out.println("""
                === MENU GENERAL ===
                1. Iniciar sesión
                2. Registrar usuario (No implementado)
                3. Sumar dias
                4. Mostrar fecha modificada
                5. Salir
                """);
    }

    public static void mostrarMenuUsuario() {
        System.out.println("""
                 === MENU USUARIO ===
                1. Crear evento
                2. Añadir participantes
                3. Eliminar participante
                4. Consultar eventos creados
                5. Consultar eventos donde participo
                6. Consultar todos mis eventos
                7. Consultar pagos pendientes
                8. Saldar pagos pendientes
                9. Confirmar pagos
                10. Ver rankings
                11. Exportar mis eventos
                12. Deshabilitar usuario falta implementar
                13. Cerrar sesión
                """);
    }

    public static void mostrarMenuPago() {
        System.out.println("""
                === MENU PAGO ===
                1. Confirmar
                2. Rechazar
                3. Volver
                """);
    }

    public static void  mostrarMenuRankings() {
        System.out.println("""
                === MENU RANKINGS ===
                1. RANKING MOROSOS
                2. RANKING EVENTOS CREADOS
                3. RANKING MAS PENALIZACIÓN PAGADA
                4. SALIR
                """);
    }

}
