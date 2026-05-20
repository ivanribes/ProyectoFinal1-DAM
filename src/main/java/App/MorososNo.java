package App;

import Prueba.DatosPrueba;

public class MorososNo {
    public static void main(String[] args) {


        GestorMorosos gestor = new GestorMorosos();

        //TODO conectar rankings con servicio de usuario
        Aplicacion app = new Aplicacion(gestor, new ServiciosUsuario(gestor, ranking));
        app.iniciar();

        //DATOS PRUEBA
//        DatosPrueba.cargarDatosRanking(gestor);
    }
}
