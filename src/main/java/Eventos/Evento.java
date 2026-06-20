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
    private ArrayList<ParticipanteEvento> participantes;

    public Evento(String nombre, double importeTotal, Usuario creador) {
        this.id = ++idActual;
        this.nombre = nombre;
        this.importeTotal = importeTotal;
        this.fechaCreacion = LocalDate.now();
        this.fechaPagoLimite = fechaCreacion.plusDays(2);
        this.creador = creador;
        this.participantes = new ArrayList<>();
    }

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
        return participantes.size() +1;
    }

    public List<ParticipanteEvento> getListParticipantes() {
        return Collections.unmodifiableList(participantes);
    }

    public boolean tieneParticipantes() {
        return !participantes.isEmpty();
    }

    public boolean aniadirParticipantes(ParticipanteEvento participante) {

        if (participante == null || participante.getUsuario() == null) {
            return false;
        }

        if (tienePagosIniciados()) {
            return false;
        }

        if (esCreador(participante.getUsuario())) {
            return false;
        }

        if (tieneParticipante(participante.getUsuario())) {
            return false;
        }

        participantes.add(participante);
        recalcularImporte();

        return true;
    }

    public boolean importarParticipante(ParticipanteEvento participante) {

        if (participante == null || participante.getUsuario() == null) {
            return false;
        }

        if (esCreador(participante.getUsuario())) {
            return false;
        }

        if (tieneParticipante(participante.getUsuario())) {
            return false;
        }

        participantes.add(participante);
        recalcularImporte();

        return true;
    }

    private void recalcularImporte() {
        double importeParticipante = importeTotal/getParticipantes();

        for (ParticipanteEvento p : participantes) {
            p.setImporteDebe(importeParticipante);
        }
    }

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

    public ParticipanteEvento buscarParticipantePorId(int idParticipante) {
        for (ParticipanteEvento p : participantes) {
            if (p.getIdParticipante() == idParticipante) {
                return p;
            }
        }

        return null;
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

    public boolean esCreador(Usuario usuario) {
        return this.creador == usuario;
    }

    public boolean tieneParticipante(Usuario usuario) {
        for (ParticipanteEvento participante : participantes) {
            if (participante.getUsuario() == usuario) {
                return true;
            }
        }

        return false;
    }

    public boolean todosLosUsuariosDisponiblesYaParticipan(List<Usuario> usuarios, Usuario usuarioActual) {

        for (Usuario u : usuarios) {
            if (u.isActivo() && u != usuarioActual && !tieneParticipante(u)) {
                return false;
            }
        }

        return true;
    }
}

