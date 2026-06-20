package Utilidades;

public class Validacion {

    private Validacion() {
    }

    public static boolean nombreValido(String nombre) {
        return nombre != null && !nombre.isBlank();
    }

    public static boolean esPositivo(int numero) {
        return numero > 0;
    }

    public static boolean esPositivo(double numero) {
        return numero > 0;
    }

    public static boolean estaEnRango(int numero, int min, int max) {
        return numero >= min && numero <= max;
    }
}