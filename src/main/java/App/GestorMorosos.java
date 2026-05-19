package App;

import Eventos.Evento;
import Ficheros.GestorFicheros;
import Usuarios.Usuario;
import java.util.ArrayList;
import java.util.Collection;
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


}
