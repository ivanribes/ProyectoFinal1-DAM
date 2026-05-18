package App;

import Eventos.Evento;
import Usuarios.Usuario;
import java.util.ArrayList;

public class GestorMorosos {
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    private ArrayList<Evento> eventos = new ArrayList<>();


    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }
}
