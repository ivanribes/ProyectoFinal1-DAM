package Usuarios;

public class Usuario {

    private static int idActual;

    private final int id;
    private String nombre;
    private String email;
    private boolean activo;

    public Usuario(String nombre, String email) {
        this.id = ++idActual;
        this.nombre = nombre;
        this.email = email;
        this.activo = true;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
