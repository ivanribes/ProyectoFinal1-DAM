package App;

import Eventos.Evento;
import Ficheros.GestorFicheros;
import Usuarios.Usuario;
import java.util.ArrayList;

public class GestorMorosos {
    private ArrayList<Usuario> usuarios;
    private ArrayList<Evento> eventos;
    private GestorFicheros gestorFicheros;

    public GestorMorosos() {
        this.usuarios = new ArrayList<>();
        this.eventos = new ArrayList<>();
        this.gestorFicheros = new GestorFicheros();
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public ArrayList<Evento> getEventos() {
        return eventos;
    }

    public void crearEvento(Usuario user, Evento event) {

    }
}
