package Utilidades;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Entrada {

    private static final BufferedReader reader =
            new BufferedReader(new InputStreamReader(System.in));

    private Entrada() {
    }

    public static int leerInt(String mensaje) {

        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(reader.readLine());

            } catch (NumberFormatException e) {
                System.out.println("Debes introducir un número entero.");

            } catch (IOException e) {
                System.out.println("Error al leer la entrada.");
            }
        }
    }

    public static int leerIntPositivo(String mensaje) {

        while (true) {
            int numero = leerInt(mensaje);

            if (Validacion.esPositivo(numero)) {
                return numero;
            }

            System.out.println("El número debe ser mayor que 0.");
        }
    }

    public static double leerDouble(String mensaje) {

        while (true) {
            try {
                System.out.print(mensaje);
                return Double.parseDouble(reader.readLine());

            } catch (NumberFormatException e) {
                System.out.println("Debes introducir un número válido.");

            } catch (IOException e) {
                System.out.println("Error al leer la entrada.");
            }
        }
    }

    public static double leerDoublePositivo(String mensaje) {

        while (true) {
            double numero = leerDouble(mensaje);

            if (Validacion.esPositivo(numero)) {
                return numero;
            }

            System.out.println("El valor debe ser mayor que 0.");
        }
    }

    public static String leerNombre(String mensaje) {

        while (true) {
            try {
                System.out.print(mensaje);
                String nombre = reader.readLine();

                if (Validacion.nombreValido(nombre)) {
                    return nombre.trim();
                }

                System.out.println("El nombre no puede estar vacío.");

            } catch (IOException e) {
                System.out.println("Error al leer la entrada.");
            }
        }
    }

    public static int leerOpcionMenu(String mensaje, int min, int max) {

        while (true) {
            int opcion = leerInt(mensaje);

            if (Validacion.estaEnRango(opcion, min, max)) {
                return opcion;
            }

            System.out.printf("Debes introducir una opción entre %d y %d.%n", min, max);
        }
    }

    public static boolean leerSiNo(String mensaje) {

        while (true) {
            try {
                System.out.print(mensaje);
                String opcion = reader.readLine();

                if (opcion.equalsIgnoreCase("si")) {
                    return true;
                }

                if (opcion.equalsIgnoreCase("no")) {
                    return false;
                }

                System.out.println("Opción no válida.");

            } catch (IOException e) {
                System.out.println("Error al leer la entrada.");
            }
        }
    }
}