package Ficheros;


import Eventos.Evento;
import Usuarios.ParticipanteEvento;
import Usuarios.Usuario;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class GestorFicheros {
    private String ruta;

    public GestorFicheros(String ruta) {
        this.ruta = ruta;
    }

    public void generarRegistroEvento(Evento evento, Usuario usuario) throws IOException {

        File directorio = crearDirectorio(usuario);

        File archivo =
                new File(directorio,
                        evento.getId() + "_" + sanearNombreEvento(evento.getNombre()) + ".csv");

        if (archivo.createNewFile()) {
            System.out.println("Se ha creado el archivo: " + archivo.getName());
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {

            pw.println("sep=;");
            escribirCabeceraEvento(pw);
            escribirDatosEvento(pw, evento);

            escribirCabeceraParticipantes(pw);
            escribirDatosParticipantes(pw, evento);
        }
    }

    private File crearDirectorio(Usuario usuario) {

        File ruta = new File(this.ruta + "Eventos_User_" + usuario.getId() + "/");

        if (ruta.mkdir()) {
            System.out.println("Se ha creado el directorio " + ruta.getName() + "📁✅");
        }

        return ruta;
        //Añadir exception si no se puede crear ni existe??
    }

    private String sanearNombreEvento(String nombre) {
        return nombre.trim().replaceAll("\\s+", "_");
    }

    private void escribirCabeceraEvento(PrintWriter pw) {
        pw.printf("""
                EVENTO:
                id;nombre;importe_total;fecha_creacion;fecha_pago_limite
                """);

    }

    private void escribirDatosEvento(PrintWriter pw, Evento e) {
        pw.printf("%d;%s;%.2f;%s;%s%n%n", e.getId(), e.getNombre(), e.getImporteTotal(),
                e.getFechaCreacion(), e.getFechaPagoLimite());
    }

    private void escribirCabeceraParticipantes(PrintWriter pw) {
        pw.printf("""
                PARTICIPANTES:
                id;nombre;importe;penalizacion;estado_pago
                """);
    }

    private void escribirDatosParticipantes(PrintWriter pw, Evento e) {
        for (ParticipanteEvento p : e.getListParticipantes()) {
            pw.printf("%d;%s;%.2f€;%s;%s%n", p.getUsuario().getId(), p.getUsuario().getNombre(),
                    p.getPago().getImporteBase(), p.getPago().getPenalizacionAplicada(),
                    p.getPago().getEstadoPago());
        }
    }
}
