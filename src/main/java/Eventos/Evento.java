package Eventos;

import Usuarios.Usuario;
import Utilidades.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class Evento {

    private static int idActual = 0;

    private final int id;
    private String nombre;
    private String descripcion;
    private double importeTotal;
    private final LocalDate fechaCreacion;
    private final LocalDate fechaPagoLimite;
    private Usuario creador;
    private ArrayList<ParticipanteEvento> participantes;

    public Evento(String nombre, double importeTotal, Usuario creador) {
        this.id = ++idActual;
        this.nombre = nombre;
        this.importeTotal = importeTotal;
        this.fechaCreacion = LocalDate.now();
        this.fechaPagoLimite = fechaCreacion.plusDays(2);
        this.creador = creador;
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

    public ArrayList<ParticipanteEvento> getParticipantes() {
        return participantes;
    }

    public void aniadirParticipantes(ParticipanteEvento participante) {
        participantes.add(participante);
    }
}

