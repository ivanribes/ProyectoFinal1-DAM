package App;

public class MorososNo {
    public static void main(String[] args) {

        GestorMorosos gestor = new GestorMorosos();
        Aplicacion app = new Aplicacion(gestor, new ServiciosUsuario(gestor));

        app.iniciar();
    }
}
