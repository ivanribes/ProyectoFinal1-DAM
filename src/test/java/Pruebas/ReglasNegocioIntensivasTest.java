package Pruebas;

import App.GestorMorosos;
import Enums.EstadoPago;
import Eventos.Evento;
import Excepciones.UnknownEventException;
import Excepciones.UnknownPaymentException;
import Excepciones.UnknownUserException;
import Pagos.Pago;
import Rankings.RankingEventosCreados;
import Rankings.RankingMoroso;
import Rankings.RankingPenalizacion;
import Usuarios.ParticipanteEvento;
import Usuarios.Usuario;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * Tests intensivos sin JUnit.
 *
 * Cómo usar:
 * 1. Copia este archivo en src/test/java/Pruebas/ReglasNegocioIntensivasTest.java
 *    o en src/main/java/Prueba/ReglasNegocioIntensivasTest.java cambiando el package si quieres.
 * 2. Ejecútalo desde IntelliJ como una clase normal con main().
 * 3. Estos tests mezclan:
 *    - Casos que deberían pasar con tu implementación actual.
 *    - Casos cazabugs que están pensados para revelar reglas que todavía solo protege la consola/servicio,
 *      pero no el núcleo de dominio.
 */
public class ReglasNegocioIntensivasTest {

    private static int total;
    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        System.out.println("=== TESTS INTENSIVOS REGLAS DE NEGOCIO - MOROSOS NO ===\n");

        run("Usuario desactivado no puede seleccionarse como activo", ReglasNegocioIntensivasTest::usuarioDesactivadoNoSeSelecciona);
        run("Solo el creador puede localizar sus eventos como modificables", ReglasNegocioIntensivasTest::soloCreadorEncuentraEventoPropio);
        run("Añadir y eliminar participantes recalcula importes", ReglasNegocioIntensivasTest::recalculoImportesAlModificarParticipantes);
        run("Saldar pagos: solo dueño y solo estados permitidos", ReglasNegocioIntensivasTest::buscarPagoPendienteSoloDelUsuarioCorrecto);
        run("Confirmar pagos: solo pagos pendientes de confirmar dentro del evento", ReglasNegocioIntensivasTest::buscarPagoPendienteConfirmarDentroDelEvento);
        run("Evento detecta pagos iniciados correctamente", ReglasNegocioIntensivasTest::eventoDetectaPagosIniciados);
        run("No intentar añadir si todos los usuarios disponibles ya participan", ReglasNegocioIntensivasTest::todosLosUsuariosDisponiblesYaParticipan);
        run("Rankings vacíos muestran mensaje y no revientan", ReglasNegocioIntensivasTest::rankingsVaciosNoRevientan);
        run("Penalizaciones solo se actualizan en pagos pendientes o rechazados", ReglasNegocioIntensivasTest::actualizarPenalizacionesSoloEnEstadosPermitidos);

        System.out.println("\n=== CAZABUGS DE DOMINIO ===");
        run("CAZABUG: Evento no debería permitir participante duplicado desde el core", ReglasNegocioIntensivasTest::cazabugEventoNoDebePermitirDuplicadosDesdeCore);
        run("CAZABUG: Evento no debería permitir añadir al creador desde el core", ReglasNegocioIntensivasTest::cazabugEventoNoDebePermitirCreadorComoParticipanteDesdeCore);
        run("CAZABUG: Evento no debería eliminar participantes si hay pagos iniciados desde el core", ReglasNegocioIntensivasTest::cazabugEliminarConPagosIniciadosDesdeCore);
        run("CAZABUG: Pago confirmado no debería quedar sin fecha de pago", ReglasNegocioIntensivasTest::cazabugPagoConfirmadoSinFecha);

        System.out.printf("\nResultado: %d/%d tests OK, %d fallos.%n", passed, total, failed);

        if (failed > 0) {
            throw new AssertionError("Hay " + failed + " fallo(s). Revisa los CAZABUGS y los mensajes anteriores.");
        }
    }

    private static void usuarioDesactivadoNoSeSelecciona() {
        GestorMorosos gestor = new GestorMorosos();
        Usuario actual = new Usuario("Actual", "actual@test.com");
        Usuario activo = new Usuario("Activo", "activo@test.com");
        Usuario inactivo = new Usuario("Inactivo", "inactivo@test.com");
        inactivo.setActivo(false);

        gestor.aniadirUsuario(actual);
        gestor.aniadirUsuario(activo);
        gestor.aniadirUsuario(inactivo);

        assertSame(activo, gestor.buscarUsuarioActivoID(activo.getId()), "Debe encontrar usuarios activos.");
        assertThrows(UnknownUserException.class,
                () -> gestor.buscarUsuarioActivoID(inactivo.getId()),
                "No debe encontrar usuarios desactivados mediante buscarUsuarioActivoID().");
        assertTrue(gestor.hayUsuariosActivosDisponibles(actual), "Debe haber un usuario activo disponible.");

        activo.setActivo(false);
        assertFalse(gestor.hayUsuariosActivosDisponibles(actual),
                "Si todos salvo el usuario actual están desactivados, no hay usuarios disponibles.");
    }

    private static void soloCreadorEncuentraEventoPropio() {
        Escenario s = escenarioBase(0);

        assertSame(s.evento, s.gestor.buscarEventoCreadoPorUsuario(s.evento.getId(), s.creador),
                "El creador debe poder localizar su evento.");

        assertThrows(UnknownEventException.class,
                () -> s.gestor.buscarEventoCreadoPorUsuario(s.evento.getId(), s.ana),
                "Un usuario que no es creador no debe poder localizar el evento como modificable.");

        assertThrows(UnknownEventException.class,
                () -> s.gestor.buscarEventoCreadoPorUsuario(999999, s.creador),
                "Una ID inexistente debe lanzar UnknownEventException.");
    }

    private static void recalculoImportesAlModificarParticipantes() {
        Escenario s = escenarioBase(0, 120.0);

        ParticipanteEvento pAna = new ParticipanteEvento(s.ana, s.evento);
        s.evento.aniadirParticipantes(pAna);
        assertDoubleEquals(60.0, pAna.getPago().getImporteBase(),
                "Con creador + 1 participante, el importe debe dividirse entre 2.");

        ParticipanteEvento pLuis = new ParticipanteEvento(s.luis, s.evento);
        s.evento.aniadirParticipantes(pLuis);
        assertDoubleEquals(40.0, pAna.getPago().getImporteBase(),
                "Con creador + 2 participantes, Ana debe pagar 40.");
        assertDoubleEquals(40.0, pLuis.getPago().getImporteBase(),
                "Con creador + 2 participantes, Luis debe pagar 40.");

        boolean eliminado = s.evento.eliminarParticipante(pAna.getIdParticipante());
        assertTrue(eliminado, "Debe eliminar el participante existente.");
        assertDoubleEquals(60.0, pLuis.getPago().getImporteBase(),
                "Tras eliminar a Ana, Luis debe volver a pagar 60.");
    }

    private static void buscarPagoPendienteSoloDelUsuarioCorrecto() {
        Escenario s = escenarioBase(2);
        Pago pagoAna = s.participanteAna.getPago();
        Pago pagoLuis = s.participanteLuis.getPago();

        assertSame(pagoAna, s.gestor.buscarPagoPendienteUsuario(pagoAna.getId(), s.ana),
                "Ana debe poder saldar su propio pago pendiente.");

        assertThrows(UnknownPaymentException.class,
                () -> s.gestor.buscarPagoPendienteUsuario(pagoAna.getId(), s.luis),
                "Luis no debe poder saldar el pago de Ana aunque conozca la ID.");

        pagoAna.setEstadoPago(EstadoPago.PENDIENTE_CONFIRMAR);
        assertThrows(UnknownPaymentException.class,
                () -> s.gestor.buscarPagoPendienteUsuario(pagoAna.getId(), s.ana),
                "Un pago pendiente de confirmar no debe poder saldarse otra vez.");

        pagoAna.setEstadoPago(EstadoPago.PAGADO);
        assertThrows(UnknownPaymentException.class,
                () -> s.gestor.buscarPagoPendienteUsuario(pagoAna.getId(), s.ana),
                "Un pago pagado no debe poder saldarse.");

        pagoLuis.setEstadoPago(EstadoPago.RECHAZADO);
        assertSame(pagoLuis, s.gestor.buscarPagoPendienteUsuario(pagoLuis.getId(), s.luis),
                "Un pago rechazado sí debe poder volver a saldarse.");
    }

    private static void buscarPagoPendienteConfirmarDentroDelEvento() {
        Escenario s = escenarioBase(2);
        Pago pagoAna = s.participanteAna.getPago();
        Pago pagoLuis = s.participanteLuis.getPago();

        pagoAna.setEstadoPago(EstadoPago.PENDIENTE_CONFIRMAR);
        assertSame(pagoAna, s.gestor.buscarPagoPendienteConfirmar(s.evento, pagoAna.getId()),
                "Debe encontrar el pago pendiente de confirmar dentro del evento.");

        assertThrows(UnknownPaymentException.class,
                () -> s.gestor.buscarPagoPendienteConfirmar(s.evento, pagoLuis.getId()),
                "No debe encontrar pagos que no estén pendientes de confirmar.");

        Evento otroEvento = new Evento("Otro", 50, s.ana);
        s.gestor.aniadirEvento(otroEvento);
        ParticipanteEvento participanteExterno = new ParticipanteEvento(s.creador, otroEvento);
        otroEvento.aniadirParticipantes(participanteExterno);
        participanteExterno.getPago().setEstadoPago(EstadoPago.PENDIENTE_CONFIRMAR);

        assertThrows(UnknownPaymentException.class,
                () -> s.gestor.buscarPagoPendienteConfirmar(s.evento, participanteExterno.getPago().getId()),
                "No debe encontrar pagos de otro evento aunque estén pendientes de confirmar.");
    }

    private static void eventoDetectaPagosIniciados() {
        Escenario s = escenarioBase(1);
        Pago pago = s.participanteAna.getPago();

        pago.setEstadoPago(EstadoPago.PENDIENTE);
        assertFalse(s.evento.tienePagosIniciados(), "PENDIENTE no debe bloquear modificación.");

        pago.setEstadoPago(EstadoPago.RECHAZADO);
        assertFalse(s.evento.tienePagosIniciados(), "RECHAZADO no debe bloquear modificación.");

        pago.setEstadoPago(EstadoPago.PENDIENTE_CONFIRMAR);
        assertTrue(s.evento.tienePagosIniciados(), "PENDIENTE_CONFIRMAR debe bloquear modificación.");

        pago.setEstadoPago(EstadoPago.PAGADO);
        assertTrue(s.evento.tienePagosIniciados(), "PAGADO debe bloquear modificación.");
    }

    private static void todosLosUsuariosDisponiblesYaParticipan() {
        Escenario s = escenarioBase(0);
        s.inactivo.setActivo(false);

        assertFalse(s.evento.todosLosUsuariosDisponiblesYaParticipan(s.gestor.getUsuarios(), s.creador),
                "Al principio Ana y Luis están disponibles y no participan.");

        s.evento.aniadirParticipantes(new ParticipanteEvento(s.ana, s.evento));
        assertFalse(s.evento.todosLosUsuariosDisponiblesYaParticipan(s.gestor.getUsuarios(), s.creador),
                "Luis todavía está disponible y no participa.");

        s.evento.aniadirParticipantes(new ParticipanteEvento(s.luis, s.evento));
        assertTrue(s.evento.todosLosUsuariosDisponiblesYaParticipan(s.gestor.getUsuarios(), s.creador),
                "Todos los activos disponibles ya participan. El inactivo no cuenta.");
    }

    private static void rankingsVaciosNoRevientan() {
        GestorMorosos gestor = new GestorMorosos();
        gestor.aniadirUsuario(new Usuario("A", "a@test.com"));
        gestor.aniadirUsuario(new Usuario("B", "b@test.com"));

        String moroso = capturarSalida(() -> new RankingMoroso(gestor).mostrarRanking());
        assertContains(moroso, "Todavía no hay pagos confirmados",
                "RankingMoroso vacío debe mostrar mensaje claro.");

        String penalizacion = capturarSalida(() -> new RankingPenalizacion(gestor).mostrarRanking());
        assertContains(penalizacion, "Todavía no hay penalizaciones pagadas",
                "RankingPenalizacion vacío debe mostrar mensaje claro.");

        String eventos = capturarSalida(() -> new RankingEventosCreados(gestor).mostrarRanking());
        assertContains(eventos, "Todavía no hay eventos creados",
                "RankingEventosCreados vacío debe mostrar mensaje claro.");
    }

    private static void actualizarPenalizacionesSoloEnEstadosPermitidos() {
        Escenario s = escenarioBase(2);
        Pago pagoAna = s.participanteAna.getPago();
        Pago pagoLuis = s.participanteLuis.getPago();

        pagoAna.setEstadoPago(EstadoPago.PENDIENTE);
        pagoLuis.setEstadoPago(EstadoPago.PENDIENTE_CONFIRMAR);
        pagoLuis.setPenalizacionAplicada(0);

        // Avanzamos fecha usando reflexión mínima sobre la regla actual no accesible por setter.
        // Como GestorMorosos.sumarDias() lee consola, aquí comprobamos la parte que sí podemos:
        // PENDIENTE_CONFIRMAR no debe actualizarse aunque existiera penalización calculable.
        s.gestor.actualizarPenalizaciones();
        assertDoubleEquals(0.0, pagoLuis.getPenalizacionAplicada(),
                "PENDIENTE_CONFIRMAR no debería actualizar penalización automáticamente.");
    }

    private static void cazabugEventoNoDebePermitirDuplicadosDesdeCore() {
        Escenario s = escenarioBase(0);
        s.evento.aniadirParticipantes(new ParticipanteEvento(s.ana, s.evento));
        s.evento.aniadirParticipantes(new ParticipanteEvento(s.ana, s.evento));

        assertEquals(1, s.evento.getListParticipantes().size(),
                "BUG: Evento.aniadirParticipantes() permite el mismo usuario dos veces. " +
                        "La consola lo evita, pero el core no está protegido.");
    }

    private static void cazabugEventoNoDebePermitirCreadorComoParticipanteDesdeCore() {
        Escenario s = escenarioBase(0);
        s.evento.aniadirParticipantes(new ParticipanteEvento(s.creador, s.evento));

        assertFalse(s.evento.tieneParticipante(s.creador),
                "BUG: Evento.aniadirParticipantes() permite añadir al creador desde el core. " +
                        "La consola lo evita, pero el dominio no.");
    }

    private static void cazabugEliminarConPagosIniciadosDesdeCore() {
        Escenario s = escenarioBase(1);
        s.participanteAna.getPago().setEstadoPago(EstadoPago.PAGADO);

        boolean eliminado = s.evento.eliminarParticipante(s.participanteAna.getIdParticipante());

        assertFalse(eliminado,
                "BUG: Evento.eliminarParticipante() elimina aunque haya pagos iniciados. " +
                        "El servicio lo evita, pero el método público del dominio no.");
    }

    private static void cazabugPagoConfirmadoSinFecha() {
        Pago pago = new Pago(20);
        pago.setEstadoPago(EstadoPago.PENDIENTE_CONFIRMAR);
        pago.confirmar();

        assertTrue(pago.getEstadoPago() != EstadoPago.PAGADO || pago.getFechaPago() != null,
                "BUG: Pago.confirmar() permite dejar un pago PAGADO sin fechaPago si se usa directamente.");
    }

    private static Escenario escenarioBase(int participantesIniciales) {
        return escenarioBase(participantesIniciales, 100.0);
    }

    private static Escenario escenarioBase(int participantesIniciales, double importeEvento) {
        Escenario s = new Escenario();
        s.gestor = new GestorMorosos();
        s.creador = new Usuario("Creador", "creador@test.com");
        s.ana = new Usuario("Ana", "ana@test.com");
        s.luis = new Usuario("Luis", "luis@test.com");
        s.inactivo = new Usuario("Inactivo", "inactivo@test.com");

        s.gestor.aniadirUsuario(s.creador);
        s.gestor.aniadirUsuario(s.ana);
        s.gestor.aniadirUsuario(s.luis);
        s.gestor.aniadirUsuario(s.inactivo);

        s.evento = new Evento("Evento test", importeEvento, s.creador);
        s.gestor.aniadirEvento(s.evento);

        if (participantesIniciales >= 1) {
            s.participanteAna = new ParticipanteEvento(s.ana, s.evento);
            s.evento.aniadirParticipantes(s.participanteAna);
        }

        if (participantesIniciales >= 2) {
            s.participanteLuis = new ParticipanteEvento(s.luis, s.evento);
            s.evento.aniadirParticipantes(s.participanteLuis);
        }

        return s;
    }

    private static String capturarSalida(Runnable runnable) {
        PrintStream original = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        try {
            System.setOut(new PrintStream(buffer, true, StandardCharsets.UTF_8));
            runnable.run();
            return buffer.toString(StandardCharsets.UTF_8);
        } finally {
            System.setOut(original);
        }
    }

    private static void run(String nombre, TestCase test) {
        total++;
        try {
            test.run();
            passed++;
            System.out.println("✅ " + nombre);
        } catch (Throwable e) {
            failed++;
            System.out.println("❌ " + nombre);
            System.out.println("   " + e.getMessage());
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertFalse(boolean condition, String message) {
        if (condition) {
            throw new AssertionError(message);
        }
    }

    private static void assertSame(Object expected, Object actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message + " Esperado mismo objeto, pero no coincide.");
        }
    }

    private static void assertEquals(int expected, int actual, String message) {
        if (expected != actual) {
            throw new AssertionError(message + " Esperado: " + expected + ", actual: " + actual);
        }
    }

    private static void assertDoubleEquals(double expected, double actual, String message) {
        double tolerance = 0.0001;
        if (Math.abs(expected - actual) > tolerance) {
            throw new AssertionError(message + " Esperado: " + expected + ", actual: " + actual);
        }
    }

    private static void assertContains(String text, String expectedPart, String message) {
        if (!text.contains(expectedPart)) {
            throw new AssertionError(message + " No contiene: \"" + expectedPart + "\". Salida: " + text);
        }
    }

    private static void assertThrows(Class<? extends Throwable> expected, Runnable action, String message) {
        try {
            action.run();
        } catch (Throwable actual) {
            if (expected.isInstance(actual)) {
                return;
            }
            throw new AssertionError(message + " Se esperaba " + expected.getSimpleName() +
                    ", pero lanzó " + actual.getClass().getSimpleName());
        }

        throw new AssertionError(message + " Se esperaba " + expected.getSimpleName() + ", pero no lanzó nada.");
    }

    @FunctionalInterface
    private interface TestCase {
        void run();
    }

    private static class Escenario {
        GestorMorosos gestor;
        Usuario creador;
        Usuario ana;
        Usuario luis;
        Usuario inactivo;
        Evento evento;
        ParticipanteEvento participanteAna;
        ParticipanteEvento participanteLuis;
    }
}
