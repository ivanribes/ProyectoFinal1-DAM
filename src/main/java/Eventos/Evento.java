package Eventos;

import App.GestorMorosos;
import Usuarios.ParticipanteEvento;
import Usuarios.Usuario;
import java.time.LocalDate;
import java.util.List;

public class Evento {

    private final int id;
    private String nombre;
    private String descripcion;
    private double importeTotal;
    private final LocalDate fechaCreacion;
    private final LocalDate fechaPagoLimite;
    private final Usuario creador;

    public Evento(int id, String nombre, double importeTotal, Usuario creador) {
        this.id = id;
        this.nombre = nombre;
        this.importeTotal = importeTotal;
        this.fechaCreacion = LocalDate.now();
        this.fechaPagoLimite = fechaCreacion.plusDays(2);
        this.creador = creador;
        this.descripcion = null;
    }

    public Evento(int id, String nombre, double importeTotal, Usuario creador,
                  LocalDate fechaCreacion, LocalDate fechaPagoLimite, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.importeTotal = importeTotal;
        this.fechaCreacion = fechaCreacion;
        this.fechaPagoLimite = fechaPagoLimite;
        this.creador = creador;
        this.descripcion = descripcion;
    }

    //region GETTERS
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getImporteTotal() {
        return importeTotal;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDate getFechaPagoLimite() {
        return fechaPagoLimite;
    }

    public Usuario getCreador() {
        return creador;
    }

    public int getParticipantes(GestorMorosos gestorMorosos) {

        return gestorMorosos.obtenerParticipantesDeEvento(id).size() + 1;
    }

    public List<ParticipanteEvento> getListParticipantes(GestorMorosos gestorMorosos) {
        return gestorMorosos.obtenerParticipantesDeEvento(id);
    }
    //endregion

    //region PARTICIPANTES
    public boolean aniadirParticipantes(Usuario usuario, GestorMorosos gestorMorosos) {

        if (esCreador(usuario)) {
            System.out.println("No puedes añadir al creador como participante!");
            return false;
        }

        if (!gestorMorosos.sePuedeModificarParticipantes(id)) {
            return false;
        }

        if (usuarioEsParticipante(usuario, gestorMorosos)) {
            return false;
        }

        gestorMorosos.aniadirParticipanteAEvento(this , usuario);

        actualizarImportesParticipantes(gestorMorosos);
        return true;
    }

    public boolean eliminarParticipante(Usuario usuarioAEliminar, GestorMorosos gestorMorosos) {
        if (!tieneParticipantes(gestorMorosos)) {
            System.out.println("No hay participantes para eliminar");
            return false;
        }

        if (!gestorMorosos.sePuedeModificarParticipantes(id)) {
            System.out.println("No se puede modificar la lista, hay pagos iniciados");
            return false;
        }

        gestorMorosos.eliminarParticipante(usuarioAEliminar, this);
        actualizarImportesParticipantes(gestorMorosos);
        return true;
    }

    public boolean tieneParticipantes(GestorMorosos gestorMorosos) {
        return getParticipantes(gestorMorosos) > 1;
    }

    public boolean usuarioEsParticipante(Usuario usuario, GestorMorosos gestorMorosos) {

        for (ParticipanteEvento p : gestorMorosos.obtenerParticipantesDeEvento(id)) {
            if (p.getUsuario() == usuario) {
                return true;
            }
        }

        return false;
    }

    public boolean esCreador(Usuario usuario) {
        return usuario != null && creador.getId() == usuario.getId();
    }

    private double calcularImporteParticipante(GestorMorosos gestorMorosos) {
        int totalParticipantes = gestorMorosos.obtenerParticipantesDeEvento(id).size() + 1;
        return importeTotal / totalParticipantes;
    }

    private void actualizarImportesParticipantes(GestorMorosos gestorMorosos) {
        double importeBase = calcularImporteParticipante(gestorMorosos);
        gestorMorosos.actualizarImportesParticipantes(id, importeBase);
    }
    //endregion
}
