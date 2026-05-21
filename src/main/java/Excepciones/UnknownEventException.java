package Excepciones;

public class UnknownEventException extends RuntimeException {
    public UnknownEventException() {
        super("No se ha encontrado el evento🗓️⚠️");
    }
}
