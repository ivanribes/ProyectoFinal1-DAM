package Ficheros;


import App.GestorMorosos;
import Enums.EstadoPago;
import Eventos.Evento;
import Excepciones.UnknownEventException;
import Excepciones.UnknownUserException;
import Usuarios.ParticipanteEvento;
import Usuarios.Usuario;

import java.io.*;
import java.time.LocalDate;

public class GestorFicheros {
    private String ruta;
    private GestorMorosos gestorMorosos;

    public GestorFicheros(String ruta, GestorMorosos gestorMorosos) {
        this.ruta = ruta;
        this.gestorMorosos = gestorMorosos;
    }

    //region Generar registro
    public void generarRegistroEvento(Evento evento, Usuario usuario) throws IOException {

        File directorio = crearDirectorio(usuario);

        File archivo = new File(directorio,
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
    //endregion

    //region Leer datos de un fichero
    public void cargarFichero() {

        File datos = new File(ruta);
        String[] partes;

        try {
            if (!datos.exists()) {
                throw new FileNotFoundException("No se ha encontrado el fichero");
            }

            try (BufferedReader reader = new BufferedReader(new FileReader(datos))) {

                String linea;
                while ((linea = reader.readLine()) != null) {

                    if (lineaValida(linea)) {
                        try {
                        partes = linea.split(";", -1);

                        switch (partes[0]) {

                            case "USUARIO" -> registrarUsuario(partes[1], partes[2]);

                            case "EVENTO" -> registrarEvento(partes[1], castToDouble(partes[3]),
                                    castToInt(partes[4]));

                            case "PARTICIPANTE" -> registrarParticipante(castToInt(partes[5]),
                                    castToInt(partes[6]),
                                    castToLocalDate(partes[7]));
                            }
                        } catch (UnknownUserException | UnknownEventException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }

            } catch (IOException e) {
                System.out.println("Error en la lectura del fichero");
            }

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    private boolean lineaValida(String linea) {

        return !linea.isEmpty() &&
                (linea.startsWith("PARTICIPANTE") || linea.startsWith("USUARIO") ||
                        linea.startsWith("EVENTO"));
    }

    private void registrarEvento(String nombre, double importeTotal, int idCreador) {

        Usuario creador = gestorMorosos.buscarUsuarioID(idCreador);

        gestorMorosos.aniadirEvento(new Evento(nombre, importeTotal, creador));
    }

    private void registrarUsuario(String nombre, String email) {

        gestorMorosos.aniadirUsuario(new Usuario(nombre, email));
    }

    private void registrarParticipante(int idUsuario, int idEvento, LocalDate fechaPago) {

        Usuario usuario = gestorMorosos.buscarUsuarioID(idUsuario);
        Evento evento = gestorMorosos.buscarEvento(idEvento);

        ParticipanteEvento participante = new ParticipanteEvento(usuario, evento);

        evento.aniadirParticipantes(participante);

        if (fechaPago != null) {
            participante.getPago().setFechaPago(fechaPago);

            int estadoRandom = (int) (Math.random() * 3);

            switch (estadoRandom) {
                case 0 -> participante.getPago().setEstadoPago(EstadoPago.PAGADO);
                case 1 -> participante.getPago().setEstadoPago(EstadoPago.RECHAZADO);
                case 2 -> participante.getPago().setEstadoPago(EstadoPago.PENDIENTE_CONFIRMAR);
            }

            participante.getPago().setPenalizacionAplicada(participante.getPago()
                    .calcularPenalizacion(evento.getFechaPagoLimite(), fechaPago));
        }
    }

    private int castToInt(String parte) {
        return Integer.parseInt(parte);
    }

    private double castToDouble(String parte) {
        return Double.parseDouble(parte);
    }

    private LocalDate castToLocalDate(String parte) {

        if (parte.isEmpty()) {
            return null;
        }

        return LocalDate.parse(parte);
    }

    //endregion
}
