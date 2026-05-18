package Eventos;

import Usuarios.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;

public class Evento {
    private final int id;
    private String nombre;
    private String descripcion;
    private double cantTotal;
    private final LocalDate fechaCreacion;
    private final LocalDate fechaPagoLimite;
    private Usuario creador;
    private ArrayList<Usuario> participantes;

    public Evento(int id, String nombre, String descripcion, double cantTotal, LocalDate fechaCreacion, LocalDate fechaPagoLimite, Usuario creador, ArrayList<Usuario> participantes) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.cantTotal = cantTotal;
        this.fechaCreacion = fechaCreacion;
        this.fechaPagoLimite = fechaPagoLimite;
        this.creador = creador;
        this.participantes = participantes;
    }
}
