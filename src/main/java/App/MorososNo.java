package App;

import Enums.EstadoPago;
import Eventos.Evento;
import Usuarios.ParticipanteEvento;
import Usuarios.Usuario;

public class MorososNo {
    public static void main(String[] args) {


        GestorMorosos gestor = new GestorMorosos();

        Aplicacion app = new Aplicacion(gestor, new ServiciosUsuario(gestor));
        app.iniciar();

    }
}
