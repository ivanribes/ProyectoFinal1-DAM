package Eventos;

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

    public void aniadirParticipantes(ParticipanteEvento participante) {
        participantes.add(participante);
        recalcularImporte();
    }

    private void recalcularImporte() {
        double importeParticipante = importeTotal/getParticipantes();

        for (ParticipanteEvento p : participantes) {
            p.setImporteDebe(importeParticipante);
        }
    }
}

