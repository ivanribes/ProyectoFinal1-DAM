package Eventos;

import Usuarios.ParticipanteEvento;
import Usuarios.Usuario;
import java.time.LocalDate;
import java.util.List;

public class Evento {

    private static int idActual;

    private final int id;
    private String nombre;
    private String descripcion;
    private double importeTotal;
    private final LocalDate fechaCreacion;
    private final LocalDate fechaPagoLimite;
    private final Usuario creador;

    public Evento(String nombre, double importeTotal, Usuario creador) {
        this.id = ++idActual;
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

    public int getParticipantes() {



        return
    }

    public List<ParticipanteEvento> getListParticipantes() {
        return //TODO consulta que devuelva lista;
    }
    //endregion

    //region PARTICIPANTES
    public boolean aniadirParticipantes(ParticipanteEvento participante) {
        if (participanteInvalido(participante)) {
            return false;
        }

        if (tienePagosIniciados()) {
            return false;
        }

        //TODO participantesevento.insertar
        recalcularImporte();
        return true;
    }

    public boolean eliminarParticipante(int idParticipante) {
        if (tienePagosIniciados()) {
            return false;
        }

        ParticipanteEvento participante = buscarParticipantePorId(idParticipante);

        //TODO delete en la tabla participantes + recalcular
    }

    public ParticipanteEvento buscarParticipantePorId(int idParticipante) {
        //todo buscar en bd
    }

    public boolean tieneParticipantes() {
        //TODO mirar si tiene algun participante
    }

    public boolean usuarioEsParticipante(Usuario usuario) {

        //TODO buscar si el usuario es participante
        if (usuario == null) {
            return false;
        }

        for (ParticipanteEvento participante : participantes) {
            if (participante.getUsuario().getId() == usuario.getId()) {
                return true;
            }
        }

        return false;
    }

    public boolean esCreador(Usuario usuario) {
        return usuario != null && creador.getId() == usuario.getId();
    }

    private boolean participanteInvalido(ParticipanteEvento participante) {
        return participante == null ||
                participante.getUsuario() == null ||
                esCreador(participante.getUsuario()) ||
                usuarioEsParticipante(participante.getUsuario());
    }

    public double recalcularImporte() {
        //TODO update en la tabla de participantes
        double importeParticipante = importeTotal / getParticipantes();

        return importeParticipante;
    }
    //endregion

    //region PAGOS
    public boolean tienePagosIniciados() {
        //todo refactor buscando en la bd
    }
    //endregion
}
