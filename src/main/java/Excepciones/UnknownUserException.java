package Excepciones;

public class UnknownUserException extends RuntimeException {
    public UnknownUserException() {
        super("No se ha encontrado el usuario👤⚠️");
    }
}
