package Menu;

public class Menu {

    public static void mostrarMenuGeneral() {

        System.out.println("""
                === MENU GENERAL ===
                1. Iniciar sesion
                -. Falta registrar usuario
                2. Salir
                """);
    }

    public static void mostrarMenuUsuario() {
        System.out.println("""
                 === MENU USUARIO ===
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
                11- Deshabilitar usuario falta implementar
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
}
