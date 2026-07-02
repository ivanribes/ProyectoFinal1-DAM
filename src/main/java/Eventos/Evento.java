package Eventos;

import Enums.EstadoPago;
import Usuarios.ParticipanteEvento;
import Usuarios.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
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
    private final ArrayList<ParticipanteEvento> participantes;

    public Evento(String nombre, double importeTotal, Usuario creador) {
        this.id = ++idActual;
        this.nombre = nombre;
        this.importeTotal = importeTotal;
        this.fechaCreacion = LocalDate.now();
        this.fechaPagoLimite = fechaCreacion.plusDays(2);
        this.creador = creador;
        this.participantes = new ArrayList<>();
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
        return participantes.size() + 1;
    }

    public List<ParticipanteEvento> getListParticipantes() {
        return Collections.unmodifiableList(participantes);
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

        participantes.add(participante);
        recalcularImporte();
        return true;
    }

    public boolean importarParticipante(ParticipanteEvento participante) {
        if (participanteInvalido(participante)) {
            return false;
        }

        participantes.add(participante);
        recalcularImporte();
        return true;
    }

    public boolean eliminarParticipante(int idParticipante) {
        if (tienePagosIniciados()) {
            return false;
        }

        ParticipanteEvento participante = buscarParticipantePorId(idParticipante);

        if (participante == null) {
            return false;
        }

        boolean eliminado = participantes.remove(participante);

        if (eliminado) {
            recalcularImporte();
        }

        return eliminado;
    }

    public ParticipanteEvento buscarParticipantePorId(int idParticipante) {
        for (ParticipanteEvento p : participantes) {
            if (p.getIdParticipante() == idParticipante) {
                return p;
            }
        }

        return null;
    }

    public boolean tieneParticipantes() {
        return !participantes.isEmpty();
    }

    public boolean tieneParticipante(Usuario usuario) {
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
                tieneParticipante(participante.getUsuario());
    }

    private void recalcularImporte() {
        double importeParticipante = importeTotal / getParticipantes();

        for (ParticipanteEvento p : participantes) {
            p.setImporteDebe(importeParticipante);
        }
    }
    //endregion

    //region PAGOS
    public boolean tienePagosIniciados() {
        for (ParticipanteEvento participante : participantes) {
            if (participante.getPago() == null) {
                continue;
            }

            EstadoPago estado = participante.getPago().getEstadoPago();

            if (estado == EstadoPago.PAGADO ||
                    estado == EstadoPago.PENDIENTE_CONFIRMAR) {
                return true;
            }
        }

        return false;
    }
    //endregion
}
