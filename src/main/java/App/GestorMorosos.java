package App;

import Eventos.Evento;
import Ficheros.GestorFicheros;
import Pagos.Pago;
import Usuarios.ParticipanteEvento;
import Usuarios.Usuario;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestorMorosos {
    private ArrayList<Usuario> usuarios;
    private ArrayList<Evento> eventos;
    private GestorFicheros gestorFicheros;

    public GestorMorosos() {
        this.usuarios = new ArrayList<>();
        this.eventos = new ArrayList<>();
        this.gestorFicheros = new GestorFicheros();
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

    public void mostrarUsuarios() {
        for (Usuario u: usuarios) {
            System.out.println("User_ID: " + u.getId() + " --> " + u.getEmail());
        }
    }
}
