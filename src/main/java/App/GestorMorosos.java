package App;

import Eventos.Evento;
import Ficheros.GestorFicheros;
import Pagos.Pago;
import Usuarios.ParticipanteEvento;
import Usuarios.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestorMorosos {
    private ArrayList<Usuario> usuarios;
    private ArrayList<Evento> eventos;
    private GestorFicheros gestorFicheros;

    //para pruebas de penalización
    private LocalDate fechaModificada;
    //---------------------------

    public GestorMorosos() {
        this.usuarios = new ArrayList<>();
        this.eventos = new ArrayList<>();
        this.gestorFicheros = new GestorFicheros();
        this.fechaModificada = LocalDate.now();
    }

    public LocalDate getFechaModificada() {
        return fechaModificada;
    }

    public List<Usuario> getUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    public List<Evento> getEventos() {
        return Collections.unmodifiableList(eventos);
    }

    public void aniadirUsuario(Usuario user) {
        usuarios.add(user);
    }

    public void aniadirEvento(Evento evento) {
        eventos.add(evento);
    }

    //region MODIFICAR FECHA
    public void sumarDias() {
        int dias = Integer.parseInt(IO.readln("Introduce la cantidad de dias que quieres " +
                "aumentar la fecha: "));

        this.fechaModificada = fechaModificada.plusDays(dias);
    }

    public void mostrarFecha() {
        System.out.println(fechaModificada);
    }
    //enregion

    public Usuario buscarUsuarioID(int id) {
        for (Usuario u : usuarios) {
            if (id == u.getId())  {
                return u;
            }
        }

        System.out.println("No se ha encontrado el usuario");
        return null;
    }

    public Evento buscarEvento(int id) {
        for (Evento e : eventos) {
            if (id == e.getId())  {
                return e;
            }
        }

        System.out.println("No se ha encontrado el evento");
        return null;
    }

    public Pago buscarPago(Evento evento, int idPago) {
        for (ParticipanteEvento p : evento.getListParticipantes()) {
            if (p.getPago().getId() == idPago) {
                return p.getPago();
            }
        }

        System.out.println("No se ha encontrado el pago");
        return null;
    }

    public Pago buscarPago(int idPago) {
        for (Evento e: eventos) {
            for (ParticipanteEvento p : e.getListParticipantes()) {
                if (p.getPago().getId() == idPago) {
                    return p.getPago();
                }
            }
        }

        System.out.println("No se ha encontrado el pago");
        return null;
    }

    public void mostrarUsuarios() {
        for (Usuario u: usuarios) {
            System.out.println("User_ID: " + u.getId() + " --> " + u.getEmail());
        }
    }

    public void actualizarPenalizaciones() {
        for (Evento e : eventos) {
            for (ParticipanteEvento p : e.getListParticipantes()) {
               p.getPago().setPenalizacionAplicada(p.getPago()
                       .calcularPenalizacion(e.getFechaPagoLimite(), fechaModificada));
            }
        }
    }
}
