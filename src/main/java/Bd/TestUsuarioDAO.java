package Bd;

import Bd.DAO.UsuarioDAO;
import Usuarios.Usuario;

import java.util.List;

public class TestUsuarioDAO {

    public static void main(String[] args) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();

        String nombre = "Usuario Prueba DAO";
        String email = "prueba" + System.currentTimeMillis() + "@test.com";

        System.out.println("================================");
        System.out.println("      TEST USUARIO DAO");
        System.out.println("================================");

        probarBuscarUsuarioInexistente(usuarioDAO, email);
        Usuario usuarioInsertado = probarInsertarUsuario(usuarioDAO, nombre, email);

        if (usuarioInsertado == null) {
            System.out.println("No se puede continuar el test porque no se ha insertado el usuario.");
            return;
        }

        probarBuscarPorEmail(usuarioDAO, email);
        probarBuscarPorId(usuarioDAO, usuarioInsertado.getId());
        probarBuscarTodosActivos(usuarioDAO);

        probarDesactivarUsuario(usuarioDAO, usuarioInsertado.getId());
        probarUsuarioDesactivado(usuarioDAO, email);

        probarReactivarUsuario(usuarioDAO, email);
        probarUsuarioReactivado(usuarioDAO, email);

        System.out.println("\n================================");
        System.out.println("      FIN DEL TEST");
        System.out.println("================================");
    }

    private static void probarBuscarUsuarioInexistente(UsuarioDAO usuarioDAO, String email) {
        System.out.println("\n1. Buscando usuario inexistente...");

        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario == null) {
            System.out.println("Correcto: no existe ningún usuario con ese email.");
        } else {
            System.out.println("Error: el usuario ya existía.");
            mostrarUsuario(usuario);
        }
    }

    private static Usuario probarInsertarUsuario(UsuarioDAO usuarioDAO, String nombre, String email) {
        System.out.println("\n2. Insertando usuario...");

        Usuario usuario = usuarioDAO.insertarUsuario(nombre, email);

        if (usuario != null) {
            System.out.println("Usuario insertado correctamente.");
            mostrarUsuario(usuario);
        } else {
            System.out.println("Error: no se ha insertado el usuario.");
        }

        return usuario;
    }

    private static void probarBuscarPorEmail(UsuarioDAO usuarioDAO, String email) {
        System.out.println("\n3. Buscando usuario por email...");

        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario != null) {
            System.out.println("Usuario encontrado por email.");
            mostrarUsuario(usuario);
        } else {
            System.out.println("Error: no se ha encontrado el usuario por email.");
        }
    }

    private static void probarBuscarPorId(UsuarioDAO usuarioDAO, int id) {
        System.out.println("\n4. Buscando usuario por ID...");

        Usuario usuario = usuarioDAO.buscarPorId(id);

        if (usuario != null) {
            System.out.println("Usuario encontrado por ID.");
            mostrarUsuario(usuario);
        } else {
            System.out.println("Error: no se ha encontrado el usuario por ID.");
        }
    }

    private static void probarBuscarTodosActivos(UsuarioDAO usuarioDAO) {
        System.out.println("\n5. Buscando todos los usuarios activos...");

        List<Usuario> usuariosActivos = usuarioDAO.buscarTodosActivos();

        if (usuariosActivos.isEmpty()) {
            System.out.println("No hay usuarios activos.");
        } else {
            System.out.println("Usuarios activos encontrados: " + usuariosActivos.size());

            for (Usuario usuario : usuariosActivos) {
                mostrarUsuarioResumen(usuario);
            }
        }
    }

    private static void probarDesactivarUsuario(UsuarioDAO usuarioDAO, int id) {
        System.out.println("\n6. Desactivando usuario...");

        boolean desactivado = usuarioDAO.desactivarUsuario(id);

        if (desactivado) {
            System.out.println("Usuario desactivado correctamente.");
        } else {
            System.out.println("Error: no se ha desactivado ningún usuario.");
        }
    }

    private static void probarUsuarioDesactivado(UsuarioDAO usuarioDAO, String email) {
        System.out.println("\n7. Comprobando usuario desactivado...");

        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario != null) {
            mostrarUsuario(usuario);

            if (!usuario.isActivo()) {
                System.out.println("Correcto: el usuario está inactivo.");
            } else {
                System.out.println("Error: el usuario sigue activo.");
            }
        } else {
            System.out.println("Error: no se ha encontrado el usuario desactivado.");
        }
    }

    private static void probarReactivarUsuario(UsuarioDAO usuarioDAO, String email) {
        System.out.println("\n8. Reactivando usuario...");

        Usuario usuario = usuarioDAO.reactivarUsuario(email);

        if (usuario != null) {
            System.out.println("Usuario reactivado correctamente.");
            mostrarUsuario(usuario);
        } else {
            System.out.println("Error: no se ha podido reactivar el usuario.");
        }
    }

    private static void probarUsuarioReactivado(UsuarioDAO usuarioDAO, String email) {
        System.out.println("\n9. Comprobando usuario reactivado...");

        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario != null) {
            mostrarUsuario(usuario);

            if (usuario.isActivo()) {
                System.out.println("Correcto: el usuario está activo otra vez.");
            } else {
                System.out.println("Error: el usuario sigue inactivo.");
            }
        } else {
            System.out.println("Error: no se ha encontrado el usuario reactivado.");
        }
    }

    private static void mostrarUsuario(Usuario usuario) {
        System.out.println("ID: " + usuario.getId());
        System.out.println("Nombre: " + usuario.getNombre());
        System.out.println("Email: " + usuario.getEmail());
        System.out.println("Activo: " + usuario.isActivo());
    }

    private static void mostrarUsuarioResumen(Usuario usuario) {
        System.out.println(
                usuario.getId() + " - " +
                        usuario.getNombre() + " - " +
                        usuario.getEmail() + " - activo: " +
                        usuario.isActivo()
        );
    }
}